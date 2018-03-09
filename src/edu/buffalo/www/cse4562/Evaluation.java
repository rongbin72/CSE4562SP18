package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.PrimitiveIterator;

public class Evaluation extends Eval {
    private List<PrimitiveValue> tuple;
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
