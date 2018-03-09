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
    private HashMap<String, Integer> colIndex;

    public Evaluation(Schema schema, String tableName,List<PrimitiveValue> tuple) {
        this.colIndex = schema.getIndex(tableName);
        this.tuple = tuple;
    }
    
    public Evaluation(HashMap<String, Integer> index, List<PrimitiveValue> tuple) {
    	this.tuple = tuple;
    	this.colIndex = index;
    }
    
    @Override
    public PrimitiveValue eval(Column column) {
        String colName = column.getColumnName();
        int index = colIndex.get(colName);
        return tuple.get(index);
    }
}
