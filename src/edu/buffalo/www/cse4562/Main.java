package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.Union;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;


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
											"SELECT S.* from (SELECT A+B AS C FROM R WHERE C=7) S, PLAYERS;"
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
