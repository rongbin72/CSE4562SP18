package edu.buffalo.www.cse4562;

import java.util.List;
import java.util.concurrent.Callable;

import net.sf.jsqlparser.expression.PrimitiveValue;

public class ProductThread implements Callable<Tuple>{
	private Operator op;
	
	public ProductThread(Operator op) {
		this.op = op;
	}
	@Override
	public Tuple call() throws Exception {
		return op.result();
	}

}
