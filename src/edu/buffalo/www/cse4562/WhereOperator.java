package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

public class WhereOperator extends Operator{
	
	private Expression whereCondition;
	private Operator son;
	
	public WhereOperator(Operator son, Expression where) {
		this.whereCondition = where;
		this.son = son;
	}
	
	public Tuple result() {
		return null;
	}
}
