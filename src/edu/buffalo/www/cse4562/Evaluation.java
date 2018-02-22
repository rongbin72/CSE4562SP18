package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.List;

public class Evaluation extends Eval {
    private Schema schema;
    private List<String> tuple;
    private String tablename;

    public Evaluation(Schema schema, String tablename,List<String> tuple) {
        this.schema = schema;
        this.tuple = tuple;
        this.tablename = tablename;
    }

    @Override
    public PrimitiveValue eval(Column column) throws SQLException {
        String colName = column.getColumnName();
        String colType = schema.getColType(colName);
        int colIndex = schema.getColIndex(colName);

        if(colType.equals("int")) {
            return new LongValue(tuple.get(colIndex));
        } else if(colType.equals("decimal")) {
            return new DoubleValue(tuple.get(colIndex));
        } else if(colType.equals("date")) {
            return new DateValue(tuple.get(colIndex));
        } else {
            return new StringValue(tuple.get(colIndex));
        }
    }
}
