package edu.buffalo.www.cse4562;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.parser.*;

import edu.buffalo.www.cse4562.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException{

        System.out.println("Hello, World");
        
		BufferedReader in = new BufferedReader(new FileReader("data/R.csv"));
		String str;
		List<List<String>> tempIter = new ArrayList<List<String>>();
		java.util.Iterator<List<String>> iter;
		while((str = in.readLine())!=null) {
			tempIter.add(Arrays.asList(str.split("\\|")));
		}
		iter = tempIter.iterator();
		for(;iter.hasNext();) {
			System.out.println(iter.next());
		}
        
        // Test Jsqlparser
        Reader input = new StringReader("SELECT S+E as N as n FROM i as I where A = 4");
        CCJSqlParser parser = new CCJSqlParser(input);  
        Statement statement = parser.Statement();

        Schema schema = null;
        while(statement != null) {
            //system out
        	//.....
        	if(statement instanceof Select) {
        		Select select = (Select)statement;
        		SelectBody body = select.getSelectBody();
        		if(body instanceof PlainSelect) {
        			Iterator iterator = new Iterator((PlainSelect)body, schema);
        		}
        		else if(body instanceof Union) {
        			//do something with union
        		}
        		else {
        			//cannot handle
        		}
        	}
        	else if(statement instanceof CreateTable) {
        		// do something with create table
				CreateTable create = (CreateTable) statement;
				schema = new Schema(create);
        	}
        	else {
        		//cannot handle
        	}
        }

    }
}
