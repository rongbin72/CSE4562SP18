package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue.InvalidPrimitive;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

public class JoinOperator extends CrossProductOP {

	private Expression on;
	private Evaluation eval;
	private boolean isFirstLine = true;
	private List<Tuple> table = new ArrayList<>();
	private Iterator<Tuple> tableIterator;

	public JoinOperator(Operator leftSon, Operator rightSon, Expression conditions) {
		super(leftSon, rightSon);
		this.on = conditions;
		this.eval = new Evaluation();
	}

	public void addCondition(Expression e) {
		AndExpression andExp = new AndExpression(e, this.on);
		this.on = andExp;
	}

	public Tuple result() {
		if (isFirstLine) {
			Tuple resultOfSon = super.result();
			if (resultOfSon == null) {
				return null;
			}

			this.eval.init(resultOfSon);
			while (resultOfSon != null) {
				try {
					if (eval.eval(this.on).toBool()) {
						table.add(resultOfSon);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				resultOfSon = super.result();
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
}
