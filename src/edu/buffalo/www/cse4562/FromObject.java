package edu.buffalo.www.cse4562;


import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		String alias = table.getAlias();
		if(alias != null) {
			Schema.addtableAlias(alias,table.getName());
			Read reader = null;
			try {
				reader = new Read(table);
			} catch (IOException e) {
				e.printStackTrace();
			}
			RenameOperator rename = new RenameOperator(reader,alias,table.getName());
			this.tableList.add(rename);
		}		
		else {
			Read reader = null;
			try {
				reader = new Read(table);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.tableList.add(reader);
		}
	}
	
	@Override
	public void visit(SubSelect subselect) {
		String alias = subselect.getAlias();
		SelectBody subBody = subselect.getSelectBody();
		PlainSelect sel = (PlainSelect)subBody;
		RATreeBuilder builder = new RATreeBuilder((PlainSelect)subBody);
		Operator tree = builder.resultTree();
		RenameOperator rename = new RenameOperator(tree,alias);
		this.tableList.add(rename);
		
	}
	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
		
	}
	



	
}
