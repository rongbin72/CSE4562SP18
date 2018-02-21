package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import org.apache.commons.csv.*;

public class Iterator {
	//deal with each where and select
	private FromObject fromOB; 
	private WhereObject whereOB;
	private SelectObject selectOB;
	private Schema schema;
	private List<List<String>> output = null;
	
	public Iterator(PlainSelect body,Schema schema) {
		this.schema = schema;
		this.fromOB = new FromObject(((PlainSelect) body).getFromItem()); 
		this.whereOB = new WhereObject(((PlainSelect) body).getWhere(),schema);
		this.selectOB = new SelectObject(((PlainSelect) body).getSelectItems(),schema);
	}
	
	public java.util.Iterator<List<String>> Result() throws IOException {
		java.util.Iterator<List<String>> tempIters = this.fromOB.GetTable(schema);
		List<String> tuple = null;
		for(;tempIters.hasNext();) {
			tuple = tempIters.next();
			if(this.whereOB.Result(tuple)) {
				this.output.add(this.selectOB.Result(tuple));
			}
		}
		return this.output.iterator();
	}
	
}
