package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class GroupbyOperator extends Operator{

	private Expression having;
	private List<Column> groups;
	private Operator son;
	
	public GroupbyOperator(Operator son, Expression having, List<Column> groups) {
		this.having = having;
		this.groups = groups;
		this.son = son;
	}
	
	public Tuple result(){
		Tuple resulfofSon = this.son.result();
		if (resulfofSon == null) {
			return null;
		}
		resulfofSon.setGroups(this.groups);
		return resulfofSon;
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
