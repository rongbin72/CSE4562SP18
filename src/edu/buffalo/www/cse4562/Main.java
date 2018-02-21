package edu.buffalo.www.cse4562;

import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
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
    public static void main(String[] args) throws IOException, ParseException, SQLException{
        Reader r = new StringReader("Create Table R(A int,B int);select A from (Select A,B from R) Q where A>4;");
        CCJSqlParser parser = new CCJSqlParser(r);
         
        Statement statement = parser.Statement();

        Schema schema = new Schema();
        while(statement != null) {
            //system out
        	//.....
        	if(statement instanceof CreateTable) {
        		// do something with create table
				CreateTable create = (CreateTable) statement;
				schema.init(create);
        	} else if(statement instanceof Select) {
        		Select select = (Select)statement;
        		SelectBody body = select.getSelectBody();
        		if(body instanceof PlainSelect) {
        			Iterator iterator = new Iterator((PlainSelect)body, schema);
        			Helper h = new Helper();
        			h.output(iterator.Result());
        		}
        		else if(body instanceof Union) {
        			//do something with union
        		}
        		else {
        			//cannot handle
        		}
        	}

        	else {
        		//cannot handle
        	}
        	statement = parser.Statement();
        }

    }
}
