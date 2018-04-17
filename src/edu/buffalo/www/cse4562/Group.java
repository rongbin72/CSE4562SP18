package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
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
    private Evaluation eval = new Evaluation();

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
        for (int i = 0; i < line.size(); i++) {
            if (this.funcMap.containsKey(i)) {
                String func = this.funcMap.get(i);
                this.eval.init(tuple);
                switch (func) {
                    default:
                        if (this.line == null) {
                            this.line = line;
                            isFirstLine = true;
                        } else {
                            if (isFirstLine) break;
                            Expression add = new Addition(line.get(i), this.line.get(i));
                            line.set(i, eval.eval(add));
                        }
                        break;
                }
            }
        }
        this.line = line;
        this.cnt++;
    }

    public Tuple result() throws SQLException {
        for (int i = 0; i < this.line.size(); i++) {
            if (this.funcMap.containsKey(i)) {
                String func = this.funcMap.get(i);
                if (func.equals("AVG")) {
                    this.eval.init(this.tuple);
                    Expression div = new Division(this.line.get(i), new LongValue(this.cnt));
                    this.line.set(i, eval.eval(div));
                }
            }
        }
        this.tuple.setTuple(this.line);
        return this.tuple;
    }
}
