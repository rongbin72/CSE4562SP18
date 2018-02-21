package edu.buffalo.www.cse4562;

import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;

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
	
	public java.util.Iterator<List<String>> GetTable(Schema S) throws IOException, SQLException {//iterators
		if(!this.ifsubselect) {
			//there's not subselect
			BufferedReader in = new BufferedReader(new FileReader(S.getPath()));
			String str;
			List<List<String>> tempIter = new ArrayList<List<String>>();
			java.util.Iterator<List<String>> iter;
			while((str = in.readLine())!=null) {
				tempIter.add(Arrays.asList(str.split("\\|"))); 
			}
			iter = tempIter.iterator();
			return iter;
		}
		else {
			Iterator iterator = new Iterator((PlainSelect)this.subbody,S);
			return iterator.Result();
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
