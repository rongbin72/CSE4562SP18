package edu.buffalo.www.cse4562;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.parser.*;

import edu.buffalo.www.cse4562.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, SQLException {

    	Helper.prompt();
    	Reader r = new InputStreamReader(System.in);
//        Reader r = new StringReader("CREATE TABLE PLAYERS(" +
//                                            "ID string, " +
//                                            "FIRSTNAME string, " +
//                                            "LASTNAME string, " +
//                                            "FIRSTSEASON int, " +
//                                            "LASTSEASON int, " +
//                                            "WEIGHT int, " +
//                                            "BIRTHDATE date);" +
//                                            "SELECT ID, LASTNAME, WEIGHT, BIRTHDATE " +
//                                            "FROM PLAYER " +
//                                            "WHERE WEIGHT > 200");

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
        	Helper.prompt();
        	statement = parser.Statement();
        }

    }
}
