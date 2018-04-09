package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderbyOP extends Operator {
    private Operator son;
    private List<OrderByElement> orderBy;
    private List<Tuple> table = new ArrayList<>();
    private Iterator<Tuple> itor;
    private boolean flag = true;
    // TODO make flag more readable, use LinkedList instead of List and Iterator

    public OrderbyOP(Operator op, List<OrderByElement> orderBy) {
        this.son = op;
        this.orderBy = orderBy;
    }

    @Override
    public Tuple result() {
        if (this.flag) {
            Tuple tuple = this.son.result();
            if (tuple == null) {
                return null;
            }
            boolean isAsc = orderBy.get(0).isAsc();
            Column col = (Column) orderBy.get(0).getExpression();
//            if()
            String colName = col.toString();
            int colIndex = tuple.getIndexHash().get(tuple.getTableName()).get(colName);

            while (tuple != null) {
                this.table.add(tuple);
                tuple = this.son.result();
            }

            this.flag = false;
            Helper.cmp(table, colIndex, isAsc);
            this.itor = table.iterator();

            if (this.itor.hasNext()) {
                return this.itor.next();
            } else {
                return null;
            }
        } else {
            if (this.itor.hasNext()) {
                return this.itor.next();
            } else {
                return null;
            }
        }
    }
}
