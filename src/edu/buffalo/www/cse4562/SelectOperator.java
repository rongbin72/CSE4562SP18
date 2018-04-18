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
	private Tuple resultTuple;
    private Tuple resultOfSon;
	private Evaluation eval;
    private HashMap<Integer, Group> groupMap = new HashMap<>();
	private boolean isFunc = false;
	private boolean isReadAll = false;
	private LinkedList<Tuple> table = new LinkedList<>();

    SelectOperator(Operator son, List<SelectItem> items) {
		this.son = son;
		this.items = items;
		this.eval = new Evaluation();
		for (SelectItem item : this.items) {
            if(item instanceof SelectExpressionItem) {
            	SelectExpressionItem exp = (SelectExpressionItem) item;
				if (exp.getExpression() instanceof Function) {
					this.isFunc = true;
				}
            	String alias = exp.getAlias();
            	if(alias != null) {
            		Schema.addColAlias(alias, exp.getExpression());//add alias to alias map in schema
            	}
            }
        }
	}

	private void loop() {
		for(SelectItem item:this.items) {
			item.accept(this);
		}
		this.eval.init(resultTuple);
        int key = 0;
        if (this.resultOfSon.haveGroups()) {
            List<Column> columns = this.resultOfSon.getGroups();
			for (Column c : columns) {
                key += this.eval.eval(c).hashCode();
            }
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
		if (isFunc) {
			if (!isReadAll) {
                this.resultOfSon = this.son.result();
                if (this.resultOfSon == null)
                    return null;

                this.resultTuple = new Tuple();
                this.eval.init(this.resultOfSon);
				loop();
                while ((this.resultOfSon = this.son.result()) != null) {
                    this.eval.init(this.resultOfSon);
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
            this.resultOfSon = this.son.result();
            if (this.resultOfSon == null)
                return null;
            this.resultTuple = new Tuple();
            this.eval.init(this.resultOfSon);
			for(SelectItem item:this.items) {
				item.accept(this);
			}
			return this.resultTuple;
		}
	}

	@Override
	public void visit(AllColumns allCol) {
        this.resultTuple = this.resultOfSon;
	}


	@Override
	public void visit(AllTableColumns allTableCol) {
		String tableName = allTableCol.getTable().getName();
        this.resultTuple.addAllColumn(tableName, this.resultOfSon);
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
