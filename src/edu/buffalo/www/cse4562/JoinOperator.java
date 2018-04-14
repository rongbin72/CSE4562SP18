package edu.buffalo.www.cse4562;

import java.sql.SQLException;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue.InvalidPrimitive;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

public class JoinOperator extends CrossProductOP{

	private Expression on;
	
	public JoinOperator(Operator leftSon, Operator rightSon, Expression conditions) {
		super(leftSon, rightSon);
		this.on = conditions;
	}
	
	public void addCondition(Expression e) {
		AndExpression andExp = new AndExpression(e,this.on);
		this.on = andExp;
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
			
			boolean evalResult = eval.eval(this.on).toBool();
			while(!evalResult) {
				tmp = super.result();
				if(tmp == null) {return null;}
				eval = new Evaluation(tmp);
				evalResult = eval.eval(this.on).toBool();
			}
			return tmp;
			
		} catch (InvalidPrimitive e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
