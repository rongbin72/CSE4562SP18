package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;


public class Evaluation extends Eval {
    private Tuple tuple;

    void init(Tuple tuple) {
        this.tuple = tuple;
    }
    
    @Override
    public PrimitiveValue eval(Column column) throws SQLException {
    	String tableName = column.getTable().getName();
        String colName = column.getColumnName();
        if (tableName == null) {
            for (String t : this.tuple.getIndexHash().keySet()) {
                if (this.tuple.getIndexHash().get(t).containsKey(colName)) {
                    return this.tuple.getItem(t, colName);
                }
            }
        } else if (!this.tuple.getIndexHash().containsKey(tableName)) {
            tableName = "*";
            colName = column.toString();
        }

        return this.tuple.getItem(tableName, colName);
    }
}
