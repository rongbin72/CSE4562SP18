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
	private HashMap<String, Integer> colIndex;
    private HashMap<String, String> colType;
	
	public SelectObject(List<SelectItem> list,Schema schema) {
		this.items = list;
		this.schema = schema;
		this.colIndex = new HashMap<String, Integer>();
		this.colType = new HashMap<String, String>();
	}
	
	public List<String> Result(List<String> tuple) {
		this.tuple = tuple;
		for(SelectItem item:this.items) {
			item.accept(this);
		}
		return this.tempResult;
	}
	
	public void reset() {
		this.tempResult = new ArrayList<String>();
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
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
		String col = e.toString();
		String alias = exp.getAlias();
		Evaluation eval = new Evaluation(this.schema,this.tuple);//
		try {
			this.evalResult = eval.eval(e);
			int index = this.tempResult.size();
<<<<<<< HEAD
			String name = this.evalResult.toString();
=======
			String result = this.evalResult.toString();
>>>>>>> subselect
			if(alias == null) {
				alias = col;
			}
			this.tempResult.add(result);
			this.colIndex.put(alias, index);
			String type;
			String temp = this.evalResult.getType().name();
			if(temp.matches("LONG")) {type = "int";}
			else if(temp.matches("DOUBLE")) {type = "decimal";}
			else if(temp.matches("DATE")) {type = "date";}
			else {type = "STRING";}
			this.colType.put(alias, type);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
}
