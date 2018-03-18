package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

public class GroupbyOperator extends Operator{

	private Expression having;
	private Operator son;
	
	public GroupbyOperator(Operator son, Expression having) {
		this.having = having;
		this.son = son;
	}
	
	public Tuple result(){
		return null;
	}

}
