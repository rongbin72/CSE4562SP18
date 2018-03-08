package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

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
	private Expression where;
	
	public Iterator(PlainSelect body,Schema schema) {
		this.schema = schema;
		this.where = body.getWhere();
		this.fromOB = new FromObject(body.getFromItem()); 
		this.whereOB = new WhereObject(this.where,schema);
		this.selectOB = new SelectObject(body.getSelectItems(),schema);
	}
	
	public List<List<PrimitiveValue>> Result() throws IOException, SQLException {

		Read tempIters = this.fromOB.GetTable(schema);// the iteratorable table
		String tableName = this.fromOB.getName();//the table name
		
		List<String> tuple = null;
		
		this.whereOB.setTable(tableName);
		this.selectOB.setTable(tableName);
		
		List<PrimitiveValue> line = tempIters.ReadLine();
		while(line != null) {
			if(this.where != null) {
				if(this.whereOB.Result(line)) {
					this.output.add(this.selectOB.Result(line));
				}
			}
			else {
				this.output.add(this.selectOB.Result(line));
				
			}
			this.selectOB.reset();
			line = tempIters.ReadLine();
		}
		this.addTable();
		this.schema = this.fromOB.getSchema();
		return this.output;
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
