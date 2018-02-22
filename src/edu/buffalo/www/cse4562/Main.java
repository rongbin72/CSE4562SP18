package edu.buffalo.www.cse4562;

import java.io.*;
import java.sql.SQLException;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.parser.*;


public class Main {

    public static void main(String[] args) throws IOException, ParseException, SQLException {

    	Helper.prompt();
<<<<<<< HEAD

    	Reader r = new InputStreamReader(System.in);
=======
//    	Reader r = new InputStreamReader(System.in);
>>>>>>> cf8adcae925e938bc629731cfdbd6f0aa336979e
//        Reader r = new StringReader("CREATE TABLE PLAYERS(" +
//                                            "ID string, " +
//                                            "FIRSTNAME string, " +
//                                            "LASTNAME string, " +
//                                            "FIRSTSEASON decimal, " +
//                                            "LASTSEASON decimal, " +
//                                            "WEIGHT int, " +
//                                            "BIRTHDATE date);" +
<<<<<<< HEAD
//                                            "SELECT ID,LASTNAME " +
//                                            "FROM PLAYERS;"
//                                            );
=======
//                                            "SELECT ID, LASTNAME, WEIGHT, BIRTHDATE " +
//                                            "FROM PLAYER " +
//                                            "WHERE WEIGHT > 200");
    	//Reader r = new InputStreamReader(System.in);
        Reader r = new StringReader("CREATE TABLE PLAYERS(" +
                                            "ID string, " +
                                            "FIRSTNAME string, " +
                                            "LASTNAME string, " +
                                            "FIRSTSEASON int, " +
                                            "LASTSEASON int, " +
                                            "WEIGHT int, " +
                                            "BIRTHDATE date);" +
                                            "SELECT FIRSTNAME,LASTNAME " +
                                            "FROM (select FIRSTNAME,LASTNAME from PLAYERS) Q;" 
                                            );
>>>>>>> cf8adcae925e938bc629731cfdbd6f0aa336979e

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
        			System.out.println();
        		}
        	}

        	else {
        		
        	}
        	Helper.prompt();
        	statement = parser.Statement();
        }

    }
}
