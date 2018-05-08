package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class Main {

    public static void main(String[] args) throws ParseException, IOException {
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
        int createTableCount = 0;
        while (statement != null) {
            if (statement instanceof CreateTable) {
                CreateTable create = (CreateTable) statement;
                Schema.addTable(create);
                createTableCount ++;
            } else if (statement instanceof Select) {
                Select select = (Select) statement;
                SelectBody body = select.getSelectBody();
                if (body instanceof PlainSelect) {
                    PlainSelect sel = (PlainSelect) body;
                    RATreeBuilder builder = new RATreeBuilder(sel);
                    Operator RATree = builder.resultTree();
                    Optimizer opt = new Optimizer(sel.getWhere(), RATree);
                    RATree = opt.resultTree();
                    try {
                        Helper.output(RATree);
                    } catch (OutOfMemoryError ignored) {}
                }
            }
            if (createTableCount < 8) {
                statement = parser.Statement();
            } else {
                // 5 min to do pre-process here
                Schema.buildIndex();

                Helper.prompt();
                statement = parser.Statement();
            }
        }
    }
}
