package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class SelectObject implements SelectItemVisitor {
    private List<SelectItem> items;
    private HashMap<String, List<PrimitiveValue>> tuple = new HashMap<>();
//    private HashMap<String, List<PrimitiveValue>> line = new HashMap<>();
    private HashMap<Integer, HashMap<String, Integer>> indexResult = new HashMap<>();
    private String tableName;
    private HashMap<String, Integer> newIndex = new HashMap<String, Integer>();
    private int position = 0;

    public SelectObject(List<SelectItem> list) {
        this.items = list;
    }

    // {result_tuple_index : {table_name : table_index}}
    public HashMap<Integer, HashMap<String, Integer>> Result(HashMap<String, List<PrimitiveValue>> tuple) {
        this.tuple = tuple;

        for (SelectItem item : this.items) {
            item.accept(this);
            this.position++;
        }
        Schema.addTable("*", newIndex);
        return this.indexResult;
    }

    public HashMap<String, Integer> colIndex() {
        return this.newIndex;
    }

    public void reset() {
        this.indexResult.clear();
        this.tuple.clear();
        this.position = 0;
    }

    public HashMap<String, List<PrimitiveValue>> getTuple() {
//        HashMap<String, List<PrimitiveValue>> ret = new HashMap<>();
//        if (this.tuple.size() == 1) {
//            ret.put(this.tuple.keySet().iterator().next(), this.line);
//        } else {
//            ret.put("*" , this.line);
//        }
        return this.tuple;
    }

    public void setTable(List<String> tableNameList) {
        if (tableNameList.size() == 1) {
            this.tableName = tableNameList.get(0);
        }
        // else: get table name from AllTableColumns or SelectExpressionItem
    }

    @Override
    public void visit(AllColumns allColumns) {
        // In this case, there should be only one table
        assert this.tuple.size() == 1;
        assert this.tableName != null;

        for (int i = 0; i < this.tuple.get(this.tableName).size(); i++) {
            HashMap<String, Integer> tupleIndex = new HashMap<>();
            tupleIndex.put(this.tableName, i);
            this.indexResult.put(i, tupleIndex);
        }
        this.newIndex = Schema.getIndxHash(this.tableName);
    }

    @Override
    public void visit(AllTableColumns ATcolumns) {
        String table = ATcolumns.getTable().getName();
        List<PrimitiveValue> thisTuple = tuple.get(table);
        HashMap<String, Integer> colIndex = Schema.getIndxHash(table);

        for (int i = 0; i < thisTuple.size(); i++) {
            HashMap<String, Integer> tupleIndex = new HashMap<>();
            tupleIndex.put(table, i);
            this.indexResult.put(this.position, tupleIndex);
            for (String colName : colIndex.keySet()) {
                if (colIndex.get(colName) == i) {
                    newIndex.put(colName, position);
                }
            }
            this.position++;
        }
        this.position--;
    }

    @Override
    public void visit(SelectExpressionItem exp) {
        Expression e = exp.getExpression();
        String col = e.toString();
        String alias = exp.getAlias();
        String table;
        Evaluation eval = new Evaluation(this.tuple);
        try {
            /*
              Update tuple and add column
              Example: <R.A>, <R.A AS R_A>
             */
            if (e instanceof Column) {
                table = ((Column) e).getTable().getName();
                col = ((Column) e).getColumnName();
                if (table == null) {
                    assert this.tuple.size() == 1;
                    table = this.tableName;
                }
                if (alias != null) {
                    col = alias;
                    this.tuple.get(table).add(eval.eval(e));
                    Schema.addColumn(table, col);
                }
            }
            /*
              Example: <R.A + R.B AS AB>
             */
            else {
                if (alias != null) {
                    col = alias;
                }
                // TODO select (R.A + S.B) AS X  -- no such test case in checkpoint 2
                // assume only one table
                assert tuple.size() == 1;
                table = this.tableName;
                this.tuple.get(table).add(eval.eval(e));
                Schema.addColumn(table, col);
            }
            HashMap<String, Integer> tupleIndex = new HashMap<>();
            tupleIndex.put(table, Schema.getColIndex(table, col));
            this.indexResult.put(this.position, tupleIndex);
            this.newIndex.put(col, this.position);

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }
}
