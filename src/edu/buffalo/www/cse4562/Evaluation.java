package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.List;
import java.util.PrimitiveIterator;

public class Evaluation extends Eval {
    private Schema schema;
    private List<PrimitiveValue> tuple;
    private String tableName;

    public Evaluation(Schema schema, String tableName,List<PrimitiveValue> tuple) {
        this.schema = schema;
        this.tuple = tuple;
        this.tableName = tableName;
    }

    @Override
    public PrimitiveValue eval(Column column) {
        String colName = column.getColumnName();
        int colIndex = schema.getColIndex(this.tableName, colName);
        return tuple.get(colIndex);
    }
}
