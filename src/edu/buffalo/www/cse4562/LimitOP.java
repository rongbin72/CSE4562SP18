package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.Limit;

public class LimitOP extends Operator{
    private Operator son;
    private Limit limit;

    public LimitOP(Operator op, Limit limit) {
        this.son = op;
        this.limit = limit;
    }

    @Override
    public Tuple result() {
        return null;
    }

}
