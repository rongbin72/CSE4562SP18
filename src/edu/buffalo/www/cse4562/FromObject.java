package edu.buffalo.www.cse4562;


import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FromObject implements FromItemVisitor {
	private FromItem body;
	private String tablenames;
	private boolean ifsubselect;
	private SelectBody subbody;
	
	public FromObject(FromItem body) {
		this.body = body;
		this.body.accept(this);
	}
	
	public String getName() {
		return this.tablenames;
	}
	
	public java.util.Iterator<List<String>> GetTable(Schema S) throws IOException, SQLException {//iterators
		if(!this.ifsubselect) {
			//there's not subselect
			//when iterating out of subselect, the table has to change
			
			Read reader = new Read(new File(S.getPath(this.tablenames)));
			String str;
			List<List<String>> tempIter = new ArrayList<List<String>>();
			while((str = reader.ReadLine())!=null) {
				tempIter.add(Arrays.asList(str.split("\\|"))); 
			}
			return tempIter.iterator();
		}
		else {
			Iterator iterator = new Iterator((PlainSelect)this.subbody,S);//result of plain select
			java.util.Iterator<List<String>> iter = iterator.Result();
			this.tablenames = iterator.getTablename();//getnewtable
			return iter;
		}
	}
	
	@Override
	public void visit(Table table) {
		String alias = table.getAlias();
		if(alias == null) {
			this.tablenames = table.getName();
		}
		else {
			this.tablenames = alias;
		}
	}
	@Override
	public void visit(SubSelect subselect) {
		this.ifsubselect = true;
		this.tablenames = subselect.getAlias();
		this.subbody = subselect.getSelectBody();
		
	}
	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
	}
	



	
}
