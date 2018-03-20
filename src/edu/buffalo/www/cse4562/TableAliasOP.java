package edu.buffalo.www.cse4562;

public class TableAliasOP extends Operator{
	
	private String originName = "*";
	private String aliasName;
	private Operator son;
	
	public TableAliasOP(String originName, String aliasName, Operator son) {
		this.originName = originName;
		this.aliasName = aliasName;
		this.son = son;
	}
	
	public TableAliasOP(String aliasName, Operator son) {
		this.aliasName = aliasName;
		this.son = son;
	}
	
	@Override
	public Tuple result() {
		// TODO Auto-generated method stub
		return null;
	}

}
