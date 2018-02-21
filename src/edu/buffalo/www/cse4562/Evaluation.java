package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.PrimitiveType;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.sql.SQLException;
import java.util.List;

public class Evaluation extends Eval {
    private Schema schema;
    private List<String> tuple;
    private boolean bool;

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
            return new LongValue(Long.parseLong(tuple.get(colIndex)));
        } else if(colType.equals("string") || colType.equals("varchar") || colType.equals("char")) {
            return new StringValue(tuple.get(colIndex));
        } else if(colType.equals("date")) {
            return new DateValue(tuple.get(colIndex));
        } else {
            return new DoubleValue(Double.parseDouble(tuple.get(colIndex)));
        }
    }
}
