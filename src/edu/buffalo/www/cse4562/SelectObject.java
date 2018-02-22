package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.*;

public class SelectObject implements SelectItemVisitor{
	private List<SelectItem> items;
	private List<String> tuple;
	private List<String> tempResult = new ArrayList<String>();
	private Schema schema;
	private PrimitiveValue evalResult;
	private HashMap<String, Integer> colIndex = new HashMap<String, Integer>();
    private HashMap<String, String> colType = new HashMap<String, String>();
	
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
	
	public HashMap<String, Integer> colIndex() {
		return this.colIndex;
	}
	
	public HashMap<String, String> coltype() {
		return this.colType;
	}
	@Override
	public void visit(AllColumns allColumns) {
		this.tempResult.addAll(this.tuple);
		this.colIndex = this.schema.getIndex();
		this.colType = this.schema.getType();
		
	}

	@Override
	public void visit(AllTableColumns ATcolumns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SelectExpressionItem exp){
		Expression e = exp.getExpression();
		String alias = exp.getAlias();
		Evaluation eval = new Evaluation(this.schema,this.tuple);//
		try {
			this.evalResult = eval.eval(e);
			int index = this.tempResult.size();
			String name = this.evalResult.toRawString();
			if(alias == null) {
				alias = name;
			}
			this.tempResult.add(name);
			this.colIndex.put(alias, index);
			String type;
			String temp = this.evalResult.getType().name();
			if(temp.matches("LongValue")) {type = "int";}
			else if(temp.matches("DoubleValue")) {type = "decimal";}
			else if(temp.matches("DateValue")) {type = "date";}
			else {type = "string";}
			this.colType.put(alias, type);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
}
