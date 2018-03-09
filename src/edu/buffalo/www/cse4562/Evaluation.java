package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;
import java.util.ArrayList;
import java.util.List;

public class Evaluation extends Eval {
    private List<PrimitiveValue> tuple = new ArrayList<PrimitiveValue>();
    private String tableName;

    public Evaluation(String tableName,List<PrimitiveValue> tuple) {
        this.tableName = tableName;
        this.tuple = tuple;
    }
    
    @Override
    public PrimitiveValue eval(Column column) {
        String colName = column.getColumnName();
        int index = Schema.getColIndex(tableName, colName);
        return tuple.get(index);
    }
}
