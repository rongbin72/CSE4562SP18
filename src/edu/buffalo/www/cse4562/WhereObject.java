package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;

public class WhereObject {
	private Expression where;
	
	public WhereObject(Expression where){
		this.where = where;
	}
}
