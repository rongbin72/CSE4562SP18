package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javafx.scene.Scene;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

public class SelectObject implements SelectItemVisitor{
	private List<SelectItem> items;
	private List<PrimitiveValue> tuple;
	private List<Integer> indexResult = new ArrayList<>();
    private String tablename;
    private HashMap<String, Integer> newIndex = new HashMap<String, Integer>();
    private int position = 0;
	
	public SelectObject(List<SelectItem> list) {
		this.items = list;
	}
	
	//return a list include index of a the tuple should be selected
	public List<Integer> Result(List<PrimitiveValue> tuple) {
		this.tuple = tuple;
		for(SelectItem item:this.items) {
			item.accept(this);
			this.position ++;
		}
		Schema.addTable("*", newIndex);
		return this.indexResult;
	}
	
	public HashMap<String, Integer> colIndex(){
		return this.newIndex;
	}
	
	public void reset() {
		this.indexResult = new ArrayList<Integer>();
		this.position = 0;
	}
	
	public List<PrimitiveValue> getTuple(){
		return this.tuple;
	}
	
	public void setTable(String tablename) {
		this.tablename = tablename;
	}
	
	@Override
	public void visit(AllColumns allColumns) {
		for(int i = 0;i<this.tuple.size();i++) {
			this.indexResult.add(i);
		}
		this.newIndex = Schema.getIndxHash(this.tablename);
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
		String table;
		Evaluation eval = new Evaluation(this.tablename, this.tuple);
		try {
			//get index
			//renew tuple
			//add column
			if(e instanceof Column) {
				table = ((Column) e).getTable().getName();
				col = ((Column) e).getColumnName();
				if(table == null) {
					table = this.tablename;
				}
				if(alias != null) {
					col = alias;
					this.tuple.add(eval.eval(e));
					Schema.addColumn(table, col);
				}
			}
			else {
				if(alias != null) {
					col = alias;
				}
				table = this.tablename;
				this.tuple.add(eval.eval(e));
				Schema.addColumn(table, col);
			}
			this.indexResult.add(Schema.getColIndex(table, col));
			this.newIndex.put(col, this.position);


		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
}
