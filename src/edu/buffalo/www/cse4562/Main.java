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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws ParseException {
    	Helper.prompt();
    	Reader r = new InputStreamReader(System.in);
//        Reader r = new StringReader("CREATE TABLE R(A integer, B integer, C integer);" + "CREATE TABLE PLAYERS(" +
//                                            "ID STRING, " +
//                                            "FIRSTNAME STRING, " +
//                                            "LASTNAME STRING, " +
//                                            "FIRSTSEASON double, " +
//                                            "LASTSEASON integer, " +
//                                            "WEIGHT integer, " +
//                                            "BIRTHDATE date);" +
//											"SELECT R1.A, R2.B FROM R R1, R R2, R R3 WHERE R1.A = R2.A;"
//                                            );

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
					RATreeBuilder builder = new RATreeBuilder(sel);
					Operator RATree = builder.resultTree();
					Optimizer opt = new Optimizer(sel.getWhere(),RATree);
					RATree = opt.resultTree();
					Helper.output(RATree);
//					System.out.println();
//					Helper.output(RATree);

//        			Iterator iterator = new Iterator(sel);
//        			Helper.output(iterator.Result(), sel.getOrderByElements(), sel.getLimit());
//        			Schema.reset(1);
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
