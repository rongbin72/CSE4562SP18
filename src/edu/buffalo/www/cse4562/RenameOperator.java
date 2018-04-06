package edu.buffalo.www.cse4562;

public class RenameOperator extends Operator{
	private Operator son;
	private String alias;
	private String origin;
	
	public RenameOperator(Operator son, String alias) {
		
	}
	
	public RenameOperator(Operator son, String alias, String origin) {
		
	}

	@Override
	public Tuple result() {
		if(this.origin == null) {
			//subquery
		}
		else {
			return this.son.result();
		}
	}

}
