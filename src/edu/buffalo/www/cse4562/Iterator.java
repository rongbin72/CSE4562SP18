package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.expression.Expression;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Iterator {
	//deal with each where and select
	private FromObject fromOB; 
	private WhereObject whereOB;
	private SelectObject selectOB;
	private Schema schema;
	private String tablename;//name of table made with subselect
	private List<List<String>> output = new ArrayList<List<String>>();
	private PlainSelect body;
	private Expression where;
	
	public Iterator(PlainSelect body,Schema schema) {
		this.schema = schema;
		this.where = ((PlainSelect) body).getWhere();
		this.fromOB = new FromObject(((PlainSelect) body).getFromItem()); 
		this.whereOB = new WhereObject(this.where,schema);
		this.selectOB = new SelectObject(((PlainSelect) body).getSelectItems(),schema);
		this.body = body;
	}
	
	public java.util.Iterator<List<String>> Result() throws IOException, SQLException {

		java.util.Iterator<List<String>> tempIters = this.fromOB.GetTable(schema);// the iteratorable table
		String tableName = this.fromOB.getName();//the table name
		
		List<String> tuple = null;
		
		this.whereOB.setTable(tableName);
		this.selectOB.setTable(tableName);
		
//		if(((PlainSelect) body).getFromItem() instanceof SubSelect){
//			this.whereOB.setTable(this.tablename);
//			this.selectOB.setTable(this.tablename);
//		}
		for(;tempIters.hasNext();) {
			tuple = tempIters.next();
			if(this.where != null) {
				if(this.whereOB.Result(tuple)) {
					this.output.add(this.selectOB.Result(tuple));
				}
			}
			else {
				this.output.add(this.selectOB.Result(tuple));
				
			}
			this.selectOB.reset();
		}
		this.addTable();
		this.schema = this.fromOB.getSchema();
		return this.output.iterator();
	}
	
	public String getTablename() {
		return this.tablename;
	}
	
	public void addTable() {
		this.schema.init(this.selectOB.colIndex(),this.selectOB.coltype(),this.tablename);
	}
	
	public void updataTable(String s) {
		this.tablename = s;
	}
	
	public Schema getSchema() {
		return this.schema;
	}

	
}
