package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.PrimitiveValue;

public class SelectOperator extends Operator{

	private Operator son;
	
	public SelectOperator(Operator son) {
		this.son = son;
	}

	@Override
	public Tuple result() {
		// TODO Auto-generated method stub
		return null;
	}

}
