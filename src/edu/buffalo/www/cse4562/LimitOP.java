package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.Limit;

public class LimitOP extends Operator{
    private Operator son;
    private Limit limit;
    private long cnt;

    public LimitOP(Operator op, Limit limit) {
        this.son = op;
        this.limit = limit;
        this.cnt = limit.getRowCount();
    }

    @Override
    public Tuple result() {
        if (cnt > 0) {
            cnt--;
            return this.son.result();
        }
        return null;
    }

	@Override
	public Operator getSon() {
		return this.son;
	}

	@Override
	public void setSon(Operator son) {
		
	}

}
