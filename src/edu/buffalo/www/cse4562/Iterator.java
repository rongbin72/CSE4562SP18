package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Iterator {
	//deal with each where and select
	private FromObject fromOB; 
	private WhereObject whereOB;
	private SelectObject selectOB;
	private Schema schema;
	private List<List<String>> output = new ArrayList<List<String>>();
	private PlainSelect body;
	
	public Iterator(PlainSelect body,Schema schema) {
		this.schema = schema;
		this.fromOB = new FromObject(((PlainSelect) body).getFromItem()); 
		this.whereOB = new WhereObject(((PlainSelect) body).getWhere(),schema);
		this.selectOB = new SelectObject(((PlainSelect) body).getSelectItems(),schema);
		this.body = body;
	}
	
	public java.util.Iterator<List<String>> Result() throws IOException, SQLException {
		java.util.Iterator<List<String>> tempIters = this.fromOB.GetTable(schema);
		List<String> tuple = null;
		for(;tempIters.hasNext();) {
			tuple = tempIters.next();
			if(this.whereOB.equals(null)) {  //
				if(this.whereOB.Result(tuple)) {
					this.output.add(this.selectOB.Result(tuple));
				}
			}
			else {
				this.output.add(this.selectOB.Result(tuple));
				
			}
			selectOB = new SelectObject(((PlainSelect) this.body).getSelectItems(),schema);
		}
		return this.output.iterator();
	}
	
	public Schema newSchema() {
		Schema s = new Schema();
		s.init(this.output(),this.selectOB.colIndex(),this.selectOB.coltype(),this.fromOB.getName());
		return s;
	}
	
	public void updataSchema(Schema s) {
		this.schema = s;
	}
	
	public String output() {
		String temp = new String();
		java.util.Iterator<List<String>> table = this.output.iterator();
        for(;table.hasNext();) {
            String line = "";
            for(String cell : table.next()) {
                line = line + cell + " ";
            }
            line = line.trim().replace(" ", "|");
            temp = temp + line + '\n';
        }
        return temp;
	}
	
}
