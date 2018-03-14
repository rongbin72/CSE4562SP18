package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.schema.Column;

public class SelectObject implements SelectItemVisitor {
    private List<SelectItem> items;
    private HashMap<String, List<PrimitiveValue>> tuple;
    private List<PrimitiveValue> line = new ArrayList<>();
    private List<Integer> indexResult = new ArrayList<>();
    private String tableName;
    private HashMap<String, Integer> newIndex = new HashMap<String, Integer>();
    private int position = 0;

    public SelectObject(List<SelectItem> list) {
        this.items = list;
    }

    //return a list include index of a the tuple should be selected
    public List<Integer> Result(HashMap<String, List<PrimitiveValue>> tuple) {
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
        this.indexResult = new ArrayList<>();
        this.line.clear();
        this.position = 0;
    }

    public HashMap<String, List<PrimitiveValue>> getTuple() {
        HashMap<String, List<PrimitiveValue>> ret = new HashMap<>();
        ret.put("*", this.line);
        return ret;
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
            this.indexResult.add(i);
            line.add(tuple.get(this.tableName).get(i));
        }
        this.newIndex = Schema.getIndxHash(this.tableName);
    }

    @Override
    public void visit(AllTableColumns ATcolumns) {
        this.tableName = ATcolumns.getTable().getName();
        List<PrimitiveValue> thisTuple = tuple.get(this.tableName);
        HashMap<String, Integer> colIndex = Schema.getIndxHash(this.tableName);

        for (int i = 0; i < thisTuple.size(); i++) {
            this.indexResult.add(this.position);
            for (String colName : colIndex.keySet()) {
                if (colIndex.get(colName) == i) {
                    newIndex.put(colName, position);
                }
            }
            this.position++;
            this.line.add(thisTuple.get(i));
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
            //get index
            //renew tuple
            //add column
            if (e instanceof Column) {
                table = ((Column) e).getTable().getName();
                col = ((Column) e).getColumnName();
                if (table == null) {
                    assert this.tuple.size() == 1;
                    table = this.tableName;
                }
                if (alias != null) {
                    col = alias;
                    this.line.add(eval.eval(e));
                    Schema.addColumn(table, col);
                }
                this.line.add(eval.eval(e));
            } else {
                if (alias != null) {
                    col = alias;
                }
                table = this.tableName;
                this.line.add(eval.eval(e));
                Schema.addColumn(table, col);
            }
            this.indexResult.add(this.position);
            this.newIndex.put(col, this.position);

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }
}
