package edu.buffalo.www.cse4562;

import java.io.Reader;
import java.io.StringReader;
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

        // Test CommonsCSV lib
        BufferedReader in = new BufferedReader(new FileReader("data/R.csv"));
//        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter('|').parse(in);
//        for (CSVRecord record : records) {
//            System.out.println(record.toString());
//        }
        String str;
		while((str = in.readLine())!=null) {
			System.out.println(str.split("|")[0]);
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
