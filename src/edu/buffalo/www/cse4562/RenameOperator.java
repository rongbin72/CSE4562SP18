package edu.buffalo.www.cse4562;

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
		Schema.addtableAlias(alias, origin);
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
			return resultofSon;
		}
		else {
			return resultofSon;
		}
	}

}
