package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.*;

public class SelectObject implements SelectItemVisitor{
	private List<SelectItem> items;
	private List<String> tuple;
	private List<String> tempResult = null;
	
	public SelectObject(List<SelectItem> list,Schema schema) {
		this.items = list;
	}
	
	public List<String> Result(List<String> tuple) {
		this.tuple = tuple;
		for(SelectItem item:this.items) {
			item.accept(this);
		}
		return this.tempResult;
	}

	@Override
	public void visit(AllColumns allColumns) {
		this.tempResult.addAll(this.tuple);
		
	}

	@Override
	public void visit(AllTableColumns ATcolumns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SelectExpressionItem exp) {
		Expression e = exp.getExpression();
		//need to get the expression value
		
	}
}
