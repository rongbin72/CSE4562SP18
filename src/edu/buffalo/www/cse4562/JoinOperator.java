package edu.buffalo.www.cse4562;

import java.sql.SQLException;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue.InvalidPrimitive;

public class JoinOperator extends CrossProductOP{

	private Expression on;
	
	public JoinOperator(Operator leftSon, Operator rightSon, Expression conditions) {
		super(leftSon, rightSon);
		this.on = conditions;
	}

	public Tuple result() {
		Tuple tmp = super.result();
		if(tmp == null) {
			return null;
		}
		Evaluation eval = new Evaluation(tmp);// need to add
		try {
			if(eval.eval(this.on).toBool()) {
				return tmp;
			}
		} catch (InvalidPrimitive e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.result();
	}
}
