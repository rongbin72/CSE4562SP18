package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.*;

public class SelectObject implements SelectItemVisitor{
	private List<SelectItem> items;
	private List<PrimitiveValue> tuple;
	private List<PrimitiveValue> tempTuple;
	private List<Integer> indexResult = new ArrayList<Integer>();
	private HashMap<String, Integer> colIndex;
    private String tablename;
	
	public SelectObject(List<SelectItem> list,Schema schema) {
		this.items = list;
		this.colIndex = new HashMap<String, Integer>();
	}
	
	//return a list include index of a the tuple should be selected
	public List<Integer> Result(List<PrimitiveValue> tuple) {
		this.tuple = tuple;
		for(SelectItem item:this.items) {
			item.accept(this);
		}
		return this.indexResult;
	}
	
	public void reset() {
		this.indexResult = new ArrayList<Integer>();
	}
	
	public HashMap<String, Integer> colIndex() {
		return this.colIndex;
	}
	
	@Override
	public void visit(AllColumns allColumns) {
		for(int i = 0;i<this.tuple.size();i++) {
			this.indexResult.add(i);
		}
		this.colIndex = Schema.getIndxHash(this.tablename);		
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
		Evaluation eval = new Evaluation(this.tablename, this.tuple);
		try {
			this.evalResult = eval.eval(e);
			int index = this.tempResult.size();

			if(alias == null) {
				alias = col;
			}
			this.tempResult.add(result);
			this.colIndex.put(alias, index);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
}
