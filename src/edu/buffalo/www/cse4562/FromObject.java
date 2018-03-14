package edu.buffalo.www.cse4562;


import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

public class FromObject implements FromItemVisitor {
	private FromItem body;
	private List<String> tablenames = new ArrayList<String>();
	private List<Join> joins;
	
	public FromObject(FromItem body,List<Join> joins) {
		this.body = body;
		this.joins = joins;
		this.body.accept(this);
		for(Join Items:this.joins) {
			Items.getRightItem().accept(this);
		}
	}
	
	public Read GetTable() throws IOException, SQLException {//iterators
		Read reader = new Read(this.tablenames);
		return reader;
	}
	
	public List<String> getName() {
		return this.tablenames;
	}
	
	@Override
	public void visit(Table table) {
		String tablename = table.getName();
		String alias = table.getAlias();
		if(alias != null) {
			Schema.addTable(tablename, alias);
			tablename = alias;
		}
		this.tablenames.add(tablename);
	}
	@Override
	public void visit(SubSelect subselect) {
		String tablename = subselect.getAlias();
		SelectBody subbody = subselect.getSelectBody();
		
		Iterator iterator = new Iterator((PlainSelect)subbody);//result of plain select
		//new iterator have finished generating a new table here
		List<List<PrimitiveValue>> result;
		try {
			result = iterator.Result();
			Schema.addTable(tablename, iterator.getNewColindex(), result);
			this.tablenames.add(tablename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
	}
	



	
}
