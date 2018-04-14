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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class Group {
    private List<PrimitiveValue> tuple;
    private int cnt;
    private HashMap<Integer, String> funcMap = new HashMap<>();
    private List<SelectItem> selectItems;
    private Tuple t;

    public Group(List<SelectItem> selectItems) {
        this.cnt = 0;
        this.selectItems = selectItems;
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

    public void fold(Tuple tuple) throws SQLException {
        List<PrimitiveValue> line = tuple.getTuple();
        this.t = tuple;
        boolean isFirstLine = false;
        for (int i = 0; i < line.size(); i++) {
            if (this.funcMap.containsKey(i)) {
                String func = this.funcMap.get(i);
                Evaluation eval = new Evaluation(tuple);
                switch (func) {
                    case "SUM":
                        if (this.tuple == null) {
                            this.tuple = line;
                            isFirstLine = true;
                        } else {
                            if (isFirstLine) break;
                            Expression add = new Addition(line.get(i), this.tuple.get(i));
                            PrimitiveValue value = eval.eval(add);
                            line.set(i, value);
                        }
                        break;
                    case "AVG":
                        if (this.tuple == null) {
                            this.tuple = line;
                        } else {
                            Expression add2 = new Addition(line.get(i), this.tuple.get(i));
                            line.set(i, eval.eval(add2));
                        }
                        break;
                }
            }
        }
        this.tuple = line;
        this.cnt++;
    }

    public Tuple result() throws SQLException {
        for (int i = 0; i < this.tuple.size(); i++) {
            if (this.funcMap.containsKey(i)) {
                String func = this.funcMap.get(i);
                Evaluation eval = new Evaluation(this.t);
                if (func.equals("AVG")) {
                    Expression div = new Division(this.tuple.get(i), new LongValue(this.cnt));
                    this.tuple.set(i, eval.eval(div));
                }
            }
        }
        this.t.setTuple(this.tuple);
        return this.t;
    }
}
