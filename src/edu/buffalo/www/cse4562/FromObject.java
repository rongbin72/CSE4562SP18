package edu.buffalo.www.cse4562;


import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

public class FromObject implements FromItemVisitor {
	
	private List<Operator> tableList = new ArrayList<Operator>();
	
	public FromObject(FromItem fromBody,List<Join> joins) {
		if(joins == null) {
			fromBody.accept(this);
		}
		else {
			fromBody.accept(this);
            for(Join Items:joins) {
                Items.getRightItem().accept(this);
            }
		}
	}
	
	private CrossProductOP product() {
		Operator lhS = this.tableList.get(0);
		this.tableList.remove(0);
		if(this.tableList.size() == 1) {
			Operator rhS = this.tableList.get(0);
			return new CrossProductOP(lhS, rhS);
		}
		Operator rhS = this.product();
		return new CrossProductOP(lhS, rhS);
	}
	
	public Operator result() {
		if(this.tableList.size() == 1) {
			return this.tableList.get(0);
		}
		return product();
	}
	
	@Override
	public void visit(Table table) {
		Read reader = new Read(table);
		this.tableList.add(reader);
	}
	@Override
	public void visit(SubSelect subselect) {
		String tablename = subselect.getAlias();
		SelectBody subBody = subselect.getSelectBody();
		SyntaxTreeBuilder builder = new SyntaxTreeBuilder((PlainSelect)subBody);
		Operator tree = builder.resultTree();
		this.tableList.add(tree);
		//need to add new table index into schema here, with the select items
		
	}
	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
	}
	



	
}
