package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.eval.*;

public class WhereObject {
	private Expression where;
	private boolean bool;
	private Iterable<String> tuple;
	
	public WhereObject(Expression where){
		this.where = where;
		this.tuple = tuple;
	}

	public boolean Result(Iterable<String> tuple) {
		return bool;
	}

}
