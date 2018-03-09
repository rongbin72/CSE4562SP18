package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Iterator {
	//deal with each where and select
	private FromObject fromOB; 
	private WhereObject whereOB;
	private SelectObject selectOB;
	private List<List<String>> output = new ArrayList<List<String>>();
	private Expression where;
	
	public Iterator(PlainSelect body) {
		this.where = body.getWhere();
		this.fromOB = new FromObject(body.getFromItem()); 
		this.whereOB = new WhereObject(this.where);
		this.selectOB = new SelectObject(body.getSelectItems());
	}
	
	public List<List<PrimitiveValue>> Result() throws IOException, SQLException {

		Read tempIters = this.fromOB.GetTable(schema);// the iteratorable table
		String tableName = this.fromOB.getName();//the table name
		
		List<String> tuple = null;

		this.selectOB.setTable(tableName);
		//pass value to select
		//get value 
		//pass value to where
		//select
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
	
	public HashMap<String, Integer> getNewColindex(){
		return this.selectOB.colIndex();
	}


	
}
