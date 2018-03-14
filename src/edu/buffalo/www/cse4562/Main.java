package edu.buffalo.www.cse4562;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.parser.*;


public class Main {

    public static void main(String[] args) throws IOException, ParseException, SQLException {

    	Helper.prompt();
//    	Reader r = new InputStreamReader(System.in);

        Reader r = new StringReader("CREATE TABLE R(A integer, B integer);" + "CREATE TABLE PLAYERS(" +
                                            "ID STRING, " +
                                            "FIRSTNAME STRING, " +
                                            "LASTNAME STRING, " +
                                            "FIRSTSEASON double, " +
                                            "LASTSEASON integer, " +
                                            "WEIGHT integer, " +
                                            "BIRTHDATE date);" +
											"SELECT S.* from (SELECT A+B AS C FROM R WHERE C>5) S, PLAYERS;"
//                                            "FROM (SELECT FIRSTNAME, LASTSEASON, ID, FIRSTSEASON FROM PLAYERS) Q WHERE Q.FIRSTSEASON >= 1980 ORDER BY Q.FIRSTSEASON DESC LIMIT 3;" +
//                                             "SELECT A+B as C from R where C > 6 ORDER BY C LIMIT 3;" +
//                                            "SELECT A as C FROM R where C > 4;" +
//                                             "SELECT S.A from R as S ORDER BY S.A DESC LIMIT 6;"
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
        			PlainSelect sel = (PlainSelect) body;
        			Iterator iterator = new Iterator(sel);
        			Helper.output(iterator.Result(), sel.getOrderByElements(), sel.getLimit());
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
