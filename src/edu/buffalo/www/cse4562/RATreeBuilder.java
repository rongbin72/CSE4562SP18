package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

public class RATreeBuilder {
	
	private PlainSelect body;
	private FromObject fromOB;
	private List<SelectItem> selectItems;

	public RATreeBuilder(PlainSelect body) {
		this.body = body;
		this.fromOB = new FromObject(body.getFromItem(),body.getJoins());
		this.selectItems = body.getSelectItems();
	}
	
	public Operator resultTree() {
		Expression where = this.body.getWhere();
		Expression having = this.body.getHaving();
		List<Column> groups = this.body.getGroupByColumnReferences();
		if(where != null) {
			WhereOperator whereOP = new WhereOperator(this.fromOB.result(), where);//this should change position
			if(groups != null) {
				//create groups
				if(having != null) {
					//create having
				}
			}
			SelectOperator selectOP = new SelectOperator(whereOP, this.selectItems);//need to modify position of this line
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
