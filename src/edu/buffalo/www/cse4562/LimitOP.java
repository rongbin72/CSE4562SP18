package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.Limit;

public class LimitOP extends Operator{
    public LimitOP(Operator op, Limit limit) {

    }

    @Override
    public Tuple result() {
        return null;
    }

}
