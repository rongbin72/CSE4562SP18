package edu.buffalo.www.cse4562;


import net.sf.jsqlparser.expression.PrimitiveValue;
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
	private Schema schema;
	
	public FromObject(FromItem body) {
		this.body = body;
		this.body.accept(this);
	}
	
	public String getName() {
		return this.tablenames;
	}
	
	public Read GetTable(Schema S) throws IOException, SQLException {//iterators
		this.schema = S;
		if(!this.ifsubselect) {
			//there's not subselect
			//when iterating out of subselect, the table has to change
			
			Read reader = new Read(new File(S.getPath(this.tablenames)),this.schema, this.tablenames);
			return reader;
		}
		else {
			Iterator iterator = new Iterator((PlainSelect)this.subbody,S);//result of plain select
			iterator.updataTable(tablenames);
			List<List<PrimitiveValue>> iterTable = iterator.Result();
			//this.tablenames = iterator.getTablename();//getnewtable
			this.schema = iterator.getSchema();
			Read reader = new Read(iterTable, this.tablenames);
			return reader;
		}
	}
	
	public Schema getSchema() {
		return this.schema;
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
