package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;


public class Evaluation extends Eval {
    private Tuple tuple;

    public void init(Tuple tuple) {
        this.tuple = tuple;
    }
    
    @Override
    public PrimitiveValue eval(Column column) throws SQLException {
    	String tableName = column.getTable().getName();
        String colName = column.getColumnName();
        if (tableName == null) {
            // Only one table in tuple
            for (String t:this.tuple.getIndexHash().keySet()) {
                try {
                    return this.tuple.getItem(t, colName);
                } catch (NullPointerException n) {
                    continue;
                }
            }
        }
        if (!this.tuple.getIndexHash().containsKey(tableName)) {
            tableName = "*";
            colName = column.toString();
        }

        return this.tuple.getItem(tableName, colName);
    }
}
