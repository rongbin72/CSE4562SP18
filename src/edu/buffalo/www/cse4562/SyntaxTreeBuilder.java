package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class SyntaxTreeBuilder {
	
	private PlainSelect body;
	private FromObject fromOB;

	public SyntaxTreeBuilder(PlainSelect body) {
		this.body = body;
		this.fromOB = new FromObject(body.getFromItem(),body.getJoins());
	}
	
	public Operator resultTree() {
		Expression where = this.body.getWhere();
		Expression having = this.body.getHaving();
		List<Column> groups = this.body.getGroupByColumnReferences();
		if(where != null) {
			WhereOperator whereOP = new WhereOperator(this.fromOB.result(), where);
			if(groups != null) {
				//create groups
				if(having != null) {
					//create having
				}
			}
			SelectOperator selectOP = new SelectOperator(whereOP);//need to modify position of this line
			return selectOP;
		}
		else {
			if(groups != null) {
				//create groups
				if(having != null) {
					//create having
				}
			}
		}
		return null;
	}
}
