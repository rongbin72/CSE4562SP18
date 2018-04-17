package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Group {
    private List<PrimitiveValue> line;
    private int cnt;
    private HashMap<Integer, String> funcMap = new HashMap<>();
    private Tuple tuple;

    Group(List<SelectItem> selectItems) {
        this.cnt = 0;
        // Build FuncMap => {columnIndex : FuncName, ...}
        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem select = selectItems.get(i);
            if (select instanceof SelectExpressionItem) {
                SelectExpressionItem exp = (SelectExpressionItem) select;
                Expression e = exp.getExpression();
                if (e instanceof Function) {
                    Function f = (Function) e;
                    this.funcMap.put(i, f.getName());
                }
            }
        }
    }

    void fold(Tuple tuple) throws SQLException {
        List<PrimitiveValue> line = tuple.getTuple();
        this.tuple = tuple;
        boolean isFirstLine = false;
        for (int i : this.funcMap.keySet()) {
            String func = this.funcMap.get(i);
            switch (func) {
                default:
                    if (this.line == null) {
                        this.line = line;
                        isFirstLine = true;
                    } else {
                        if (isFirstLine) break;
                        double add = line.get(i).toDouble() + this.line.get(i).toDouble();
                        line.set(i, new DoubleValue(add));
                    }
                    break;
            }
        }
        this.line = line;
        this.cnt++;
    }

    public Tuple result() throws SQLException {
        for (int i : this.funcMap.keySet()) {
            String func = this.funcMap.get(i);
            if (func.equals("AVG")) {
                double div = this.line.get(i).toDouble() / this.cnt;
                this.line.set(i, new DoubleValue(div));
            }
        }
        this.tuple.setTuple(this.line);
        return this.tuple;
    }
}
