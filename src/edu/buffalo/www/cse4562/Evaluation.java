package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;


public class Evaluation extends Eval {
    private Tuple tuple;

    public Evaluation(Tuple tuple) {
        this.tuple = tuple;
    }

    
    @Override
    public PrimitiveValue eval(Column column) {
    	String tableName = column.getTable().getName();
        String colName = column.getColumnName();
        if (tableName == null) {
            //there is not tablename
        	return null;
        }
        else {
        	return this.tuple.getItem(tableName, colName);
        }
    }
}
