package edu.buffalo.www.cse4562;

import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.parser.*;

import edu.buffalo.www.cse4562.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException{

//        System.out.println("Hello, World");
//
//        // Test CommonsCSV lib
//        Reader in = new FileReader("data/FB.csv");
//        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
//        for (CSVRecord record : records) {
//            String high = record.get("High");
//            String low = record.get("Low");
//
//            System.out.println(high);
//            System.out.println(low);
//        }

        // Test Jsqlparser
        Reader input = new StringReader("SELECT a+b as n FROM R left join S where a = 4");
        CCJSqlParser parser = new CCJSqlParser(input);  
        Statement statement = parser.Statement();

        Schema schema;
        while(statement != null) {
            //system out
        	//.....
        	if(statement instanceof Select) {
        		Select select = (Select)statement;
        		SelectBody body = select.getSelectBody();
        		if(body instanceof PlainSelect) {
        			//iterator
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
