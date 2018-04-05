package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

public class RATreeBuilder {
	
	private PlainSelect body;
	private FromObject fromOB;
	private List<SelectItem> selectItems;
	private Limit limit;
	private List<OrderByElement> orderBy;

	public RATreeBuilder(PlainSelect body) {
		this.body = body;
		this.fromOB = new FromObject(body.getFromItem(),body.getJoins());
		this.selectItems = body.getSelectItems();
		this.limit = body.getLimit();
		this.orderBy = body.getOrderByElements();
	}
	
	public Operator resultTree() {
		Expression where = this.body.getWhere();
		Expression having = this.body.getHaving();
		List<Column> groups = this.body.getGroupByColumnReferences();
		SelectOperator selectOP;
		if(where != null) {
			WhereOperator whereOP = new WhereOperator(this.fromOB.result(), where);//this should change position
			if (groups != null) {
				GroupbyOperator groupByOP = new GroupbyOperator(whereOP, having, groups);
				selectOP = new SelectOperator(groupByOP, this.selectItems);//need to modify position of this line
			} else {
				selectOP = new SelectOperator(whereOP, this.selectItems);//need to modify position of this line
			}
		}
		else {
			if (groups != null) {
				GroupbyOperator groupByOP = new GroupbyOperator(this.fromOB.result(), having, groups);
				selectOP = new SelectOperator(groupByOP, this.selectItems);
			} else {
				selectOP = new SelectOperator(this.fromOB.result(), this.selectItems);
			}
		}

		if (orderBy == null && limit == null) {
			return selectOP;
		} else if (orderBy == null && limit != null) {
			return new LimitOP(selectOP, this.limit);
		} else if (orderBy != null && limit == null) {
			return new OrderbyOP(selectOP, orderBy);
		} else {
			OrderbyOP ob = new OrderbyOP(selectOP, orderBy);
			return new LimitOP(ob, limit);
		}
	}
}
