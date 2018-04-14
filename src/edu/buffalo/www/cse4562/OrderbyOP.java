package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.*;

public class OrderbyOP extends Operator {
    private Operator son;
    private List<OrderByElement> orderBy;
    private LinkedList<Tuple> tableContent = new LinkedList<>();
    private boolean isReadAll = false;

    public OrderbyOP(Operator op, List<OrderByElement> orderBy) {
        this.son = op;
        this.orderBy = orderBy;
    }

    @Override
    public Tuple result() {
        if (!this.isReadAll) {
            // In case table has no content
            Tuple tuple = this.son.result();
            if (tuple == null) return null;

            boolean isAsc = this.orderBy.get(0).isAsc();
            Column col = (Column) this.orderBy.get(0).getExpression();
            String colName = col.toString();
            int colIndex = tuple.getIndexHash().get("*").get(colName);

            while (tuple != null) {
                this.tableContent.add(tuple);
                tuple = this.son.result();
            }
            this.isReadAll = true;

            Helper.sort(this.tableContent, colIndex, isAsc);

            return this.tableContent.poll();
        } else {
            return this.tableContent.poll();
        }
    }

	@Override
	public Operator getSon() {
		return this.son;
	}

	@Override
	public void setSon(Operator son) {
		// TODO Auto-generated method stub
		
	}
}
