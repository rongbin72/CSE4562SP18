package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SelectOperator extends Operator implements SelectItemVisitor{

	private Operator son;
	private List<SelectItem> items;
	private List<Expression> selectExps;
	private Tuple resultTuple;
	private Tuple resultofSon;
	private Evaluation eval;
	private HashMap<String, Group> groupMap = new HashMap<>();
	private boolean isFunc = false;
	private boolean isReadAll = false;
	private LinkedList<Tuple> table = new LinkedList<>();
	
	public SelectOperator(Operator son, List<SelectItem> items) {
		this.son = son;
		this.items = items;
		for (SelectItem item : this.items) {
            if(item instanceof SelectExpressionItem) {
            	SelectExpressionItem exp = (SelectExpressionItem) item;
				if (exp.getExpression() instanceof Function) {
					this.isFunc = true;
				}
            	String alias = exp.getAlias();
            	if(alias != null) {
            		Schema.addcolAlias(alias, exp.getExpression());//add alias to alias map in schema
            	}
            }
        }
	}

	private void loop() {
		for(SelectItem item:this.items) {
			item.accept(this);
		}

		List<Column> columns = this.resultofSon.getGroups();
		String key = "";
		Evaluation e = new Evaluation(this.resultTuple);
		if (this.resultofSon.haveGroups()) {
			for (Column c : columns) {
				try {
					key += e.eval(c).toRawString() + ",";
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			key = "*";
		}

		if (groupMap.containsKey(key)) {
			try {
				this.groupMap.get(key).fold(this.resultTuple);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} else {
			Group group = new Group(this.items);
			try {
				group.fold(this.resultTuple);
				this.groupMap.put(key, group);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public Tuple result() {
		this.resultofSon = this.son.result();
		if(this.resultofSon == null) {
			return null;
		}
		this.resultTuple = new Tuple();

		if (isFunc) {
			if (!isReadAll) {
				this.eval = new Evaluation(this.resultofSon);
				loop();
				while ((this.resultofSon = this.son.result()) != null) {
					this.eval = new Evaluation(this.resultofSon);
					this.resultTuple = new Tuple();
					loop();
				}
				for (Group g : groupMap.values()) {
					try {
						this.table.add(g.result());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				this.isReadAll = true;
				return this.table.poll();
			} else {
				return this.table.poll();
			}


		} else {
			this.eval = new Evaluation(this.resultofSon);
			for(SelectItem item:this.items) {
				item.accept(this);
			}
			return this.resultTuple;
		}
	}

	@Override
	public void visit(AllColumns allCol) {
		this.resultTuple = this.resultofSon;
//		this.resultTuple.mergeTable(resultofSon);
	}


	@Override
	public void visit(AllTableColumns allTableCol) {
		String tableName = allTableCol.getTable().getName();
		this.resultTuple.addAllColumn(tableName, this.resultofSon);
//		this.resultTuple.mergeTable(this.resultofSon.subTuple(table));
	}

	@Override
	public void visit(SelectExpressionItem exp) {
		Expression e = exp.getExpression();
		if (e instanceof Function) {
			Expression f = ((Function) e).getParameters().getExpressions().get(0);
			String colName = exp.getAlias() == null ? e.toString() : exp.getAlias();

			try {
				PrimitiveValue value = this.eval.eval(f);
				this.resultTuple.addColumn(colName, value);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				String colName = e.toString();
				String alias = exp.getAlias();
				if (alias != null) {
					colName = alias;
				}
				PrimitiveValue value = this.eval.eval(e);
				this.resultTuple.addColumn(colName, value);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public Operator getSon() {
		return this.son;
	}

	@Override
	public void setSon(Operator son) {
		this.son = son;
		
	}
}
