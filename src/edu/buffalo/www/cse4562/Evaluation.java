package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.List;

public class Evaluation extends Eval {
    private Schema schema;
    private List<String> tuple;

    public Evaluation(Schema schema, List<String> tuple) {
        this.schema = schema;
        this.tuple = tuple;
    }

    @Override
    public PrimitiveValue eval(Column column) throws SQLException {
        String colName = column.getColumnName();
        String colType = schema.getColType(colName);
        int colIndex = schema.getColIndex(colName);

        if(colType.equals("int")) {
            return new LongValue(tuple.get(colIndex));
        } else if(colType.equals("string") || colType.equals("varchar") || colType.equals("char")) {
            return new StringValue(tuple.get(colIndex));
        } else if(colType.equals("date")) {
            return new DateValue(tuple.get(colIndex));
        } else {
            return new DoubleValue(tuple.get(colIndex));
        }
    }
}
