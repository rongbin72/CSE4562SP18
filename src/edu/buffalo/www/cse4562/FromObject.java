package edu.buffalo.www.cse4562;

import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

public class FromObject implements FromItemVisitor {
	private FromItem body;
	private Reader tables;
	private String tablenames;
	private boolean ifsubselect;
	private SelectBody subbody;
	
	public FromObject(FromItem body) {
		this.body = body;
		this.body.accept(this);
	}
	
	public Reader GetTable(Schema S) throws FileNotFoundException {
		if(!this.ifsubselect) {
			//there's not subselect
			Reader in = new FileReader(S.getPath());
			return in;
		}
		else {
			Iterator iterator = new Iterator((PlainSelect)this.subbody);
			String output = iterator.Output();
			Reader in = new StringReader(output);
			return in;
		}
	}
	public String GetTableName() {
		return this.tablenames;
	}
	@Override
	public void visit(Table table) {
		this.tablenames = table.getName();
		String alias = table.getAlias();
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
