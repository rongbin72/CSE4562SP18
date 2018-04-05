package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

public class GroupbyOperator extends Operator{

	private Expression having;
	private Operator son;
	
	public GroupbyOperator(Operator son, Expression having, List<Column> groups) {
		this.having = having;
		this.son = son;
	}
	
	public Tuple result(){
		return null;
	}

}
