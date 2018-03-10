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
    	//Reader r = new InputStreamReader(System.in);

        Reader r = new StringReader("CREATE TABLE R(A int, B int);" + "CREATE TABLE PLAYERS(" +
                                            "ID STRING, " +
                                            "FIRSTNAME STRING, " +
                                            "LASTNAME STRING, " +
                                            "FIRSTSEASON DECIMAL, " +
                                            "LASTSEASON int, " +
                                            "WEIGHT int, " +
                                            "BIRTHDATE date);" +
                                            //"SELECT FIRSTNAME, ID, FIRSTSEASON " +
                                            //"FROM (SELECT FIRSTNAME, LASTSEASON, ID, FIRSTSEASON FROM PLAYERS) Q WHERE Q.FIRSTSEASON >= 200;" +
                                             "SELECT S.A from R as S;" 
                                            //"SELECT A as C FROM R where C > 4;"
                                            );

        CCJSqlParser parser = new CCJSqlParser(r);
         
        Statement statement = parser.Statement();

        while(statement != null) {
            //system out
        	//.....
        	if(statement instanceof CreateTable) {
        		// do something with create table
				CreateTable create = (CreateTable) statement;
				Schema.addTable(create);
        	} else if(statement instanceof Select) {
        		Select select = (Select)statement;
        		SelectBody body = select.getSelectBody();
        		if(body instanceof PlainSelect) {
        			Iterator iterator = new Iterator((PlainSelect)body);
        			Helper.output(iterator.Result());
        			Schema.reset(1);
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
