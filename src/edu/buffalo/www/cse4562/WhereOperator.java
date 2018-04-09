package edu.buffalo.www.cse4562;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.expression.PrimitiveValue.InvalidPrimitive;

public class WhereOperator extends Operator{
	
	private Expression whereCondition;
	private Operator son;

	public WhereOperator(Operator son, Expression where) {
		this.whereCondition = where;
		this.son = son;
	}
	
	public Tuple result(){
		Tuple resultofSon = this.son.result();
		if(resultofSon == null) {
			return null;
		}
		Evaluation eval = new Evaluation(resultofSon);
		try {
			if(eval.eval(this.whereCondition).toBool()) {
				return resultofSon;
			}
			else {
				resultofSon = this.son.result();
				if(resultofSon == null) {return null;}
				eval = new Evaluation(resultofSon);
				boolean evalResult = eval.eval(this.whereCondition).toBool();
				while(!evalResult) {
					resultofSon = this.son.result();
					if(resultofSon == null) {return null;}
					eval = new Evaluation(resultofSon);
					evalResult = eval.eval(this.whereCondition).toBool();
				}
				return resultofSon;
			}
		} catch (InvalidPrimitive e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
