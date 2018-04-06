package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.List;

public class OrderbyOP extends Operator {
    private Operator son;
    private List<OrderByElement> orderBy;
    public OrderbyOP(Operator op, List<OrderByElement> orderBy) {
        this.son = op;
        this.orderBy = orderBy;
    }
    @Override

    public Tuple result() {
        return null;
    }
}
