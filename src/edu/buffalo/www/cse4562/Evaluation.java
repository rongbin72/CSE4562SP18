package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.List;

public class Evaluation extends Eval {
    private Schema schema;
    private List<String> tuple;
    private String tableName;

    public Evaluation(Schema schema, String tableName,List<String> tuple) {
        this.schema = schema;
        this.tuple = tuple;
        this.tableName = tableName;
    }

    @Override
    public PrimitiveValue eval(Column column) throws SQLException {
        String colName = column.getColumnName();
        String colType = schema.getColType(this.tableName, colName);
        int colIndex = schema.getColIndex(this.tableName, colName);

        if(Helper.equals(colType, "int")) {
            return new LongValue(tuple.get(colIndex));
        } else if(Helper.equals(colType, "decimal")) {
            return new DoubleValue(tuple.get(colIndex));
        } else if(Helper.equals(colType, "date")) {
            return new DateValue(tuple.get(colIndex));
        } else {
            return new StringValue(tuple.get(colIndex).replaceAll("^'*|'*$", ""));
        }
    }
}
