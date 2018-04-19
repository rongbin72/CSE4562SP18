package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WhereOperator extends Operator{
	
	private Expression whereCondition;
	private Operator son;
	private Evaluation eval;
	private List<Tuple> table = new ArrayList<>();
	private Iterator<Tuple> tableIterator;
	private boolean isFirstLine = true;

	public WhereOperator(Operator son, Expression where) {
		this.whereCondition = where;
		this.son = son;
		this.eval = new Evaluation();
	}

	public Tuple result(){
		if (isFirstLine) {
			Tuple resultOfSon = this.son.result();
			if (resultOfSon == null) {
				return null;
			}

			this.eval.init(resultOfSon);
			while (resultOfSon != null) {
				try {
					if (eval.eval(this.whereCondition).toBool()) {
						table.add(resultOfSon);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				resultOfSon = this.son.result();
				eval.init(resultOfSon);
			}
			this.tableIterator = this.table.iterator();
			isFirstLine = false;
			if (this.tableIterator.hasNext()) {
				return new Tuple(tableIterator.next());
			} else {
				this.tableIterator = this.table.iterator();
				return null;
			}
		} else {
			if (this.tableIterator.hasNext()) {
				return new Tuple(tableIterator.next());
			} else {
				this.tableIterator = this.table.iterator();
				return null;
			}
		}
	}

	@Override
	public Operator getSon() {
		return this.son;
	}

	@Override
	public void setSon(Operator son) {
		this.son = son;
		
	}
}
