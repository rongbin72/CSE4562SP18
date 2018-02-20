package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;

public class Iterator {
	//deal with each where and select
	private FromObject fromOB; 
	private WhereObject whereOB;
	private SelectObject selectOB;
	
	public Iterator(PlainSelect body) {
		FromObject fromOB = new FromObject(((PlainSelect) body).getFromItem()); 
		WhereObject whereOB = new WhereObject(((PlainSelect) body).getWhere());
		SelectObject selectOB = new SelectObject(((PlainSelect) body).getSelectItems());
	}
	
	//return table
}
