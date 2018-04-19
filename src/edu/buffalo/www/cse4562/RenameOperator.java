package edu.buffalo.www.cse4562;

import java.util.HashMap;

public class RenameOperator extends Operator{
	private Operator son;
	private String alias;
	private String origin;
	
	public RenameOperator(Operator son, String alias) {
		this.son = son;
		this.alias = alias;
	}
	
	public RenameOperator(Operator son, String alias, String origin) {
		this.son = son;
		this.alias = alias;
		this.origin = origin;
		Schema.addTableAlias(alias, origin);
	}
	
	public String nameGetter() {
		return this.alias;
	}

	@Override
	public Tuple result() {
		Tuple resultofSon = this.son.result();
		if(resultofSon == null) {
			return null;
		}
		else if(this.origin == null) {
			//subquery
			resultofSon.rename(this.alias);
//			resultofSon.setColAliasMap(new HashMap<>());
			return resultofSon;
		}
		else {
			resultofSon.rename(this.alias);
//			resultofSon.setColAliasMap(new HashMap<>());
			return resultofSon;
		}
	}

	@Override
	public Operator getSon() {
		return this.son;
	}

	@Override
	public void setSon(Operator son) {
		// TODO Auto-generated method stub
		
	}

}
