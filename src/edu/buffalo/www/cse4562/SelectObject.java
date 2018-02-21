package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.*;

public class SelectObject implements SelectItemVisitor{
	private List<SelectItem> items;
	private List<String> tuple;
	private List<String> tempResult = null;
	private Schema schema;
	private PrimitiveValue evalResult;
	
	public SelectObject(List<SelectItem> list,Schema schema) {
		this.items = list;
		this.schema = schema;
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
		Evaluation eval = new Evaluation(this.schema,this.tuple);
		try {
			this.evalResult = eval.eval(e);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
