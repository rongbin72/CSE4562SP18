package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.select.SelectItem;

public class SelectOperator extends Operator{

	private Operator son;
	private List<SelectItem> items;
	
	public SelectOperator(Operator son, List<SelectItem> items) {
		this.son = son;
		this.items = items;
	}

	@Override
	public Tuple result() {
		// TODO Auto-generated method stub
		return null;
	}

}
