package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evaluation extends Eval {
    private HashMap<String, List<PrimitiveValue>> tuple;

    public Evaluation(HashMap<String, List<PrimitiveValue>> tuple) {
        this.tuple = tuple;
    }
    
    @Override
    public PrimitiveValue eval(Column column) {
    	String tableName = column.getTable().getName();
        String colName = column.getColumnName();
        if (tableName == null) {
            // in this case, tuple size has to be 1
            assert tuple.size() == 1;
            tableName = tuple.keySet().iterator().next();
        }
        int index = Schema.getColIndex(tableName, colName);
        return tuple.get(tableName).get(index);
    }
}
