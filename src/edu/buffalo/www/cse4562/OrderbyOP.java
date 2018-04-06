package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.List;

public class OrderbyOP extends Operator {
    public OrderbyOP(Operator op, List<OrderByElement> orderBy) {

    }
    @Override

    public Tuple result() {
        return null;
    }
}
