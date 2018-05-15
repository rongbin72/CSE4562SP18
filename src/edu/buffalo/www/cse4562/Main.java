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
import java.io.StringReader;


public class Main {

    public static void main(String[] args) throws ParseException, IOException {
        Helper.prompt();
//        Reader r = new InputStreamReader(System.in);
        Reader r = new StringReader("CREATE TABLE PART (P_PARTKEY INTEGER PRIMARY KEY, P_NAME STRING, P_MFGR STRING, P_BRAND STRING, P_TYPE STRING, P_SIZE INTEGER, P_CONTAINER STRING, P_RETAILPRICE DOUBLE, P_COMMENT STRING, PRIMARY KEY (P_PARTKEY));\r\n" + 
        		"CREATE TABLE SUPPLIER (S_SUPPKEY INTEGER PRIMARY KEY, S_NAME STRING, S_ADDRESS STRING, S_NATIONKEY INTEGER REFERENCES NATION, S_PHONE STRING, S_ACCTBAL DOUBLE, S_COMMENT STRING, PRIMARY KEY (S_SUPPKEY), INDEX IDX(S_NATIONKEY));\r\n" + 
        		"CREATE TABLE PARTSUPP (PS_PARTKEY INTEGER REFERENCES PART, PS_SUPPKEY INTEGER REFERENCES SUPPLIER, PS_AVAILQTY INTEGER, PS_SUPPLYCOST DOUBLE, PS_COMMENT STRING, INDEX IDX(PS_PARTKEY,PS_SUPPKEY));\r\n" + 
        		"CREATE TABLE CUSTOMER (C_CUSTKEY INTEGER PRIMARY KEY, C_NAME STRING, C_ADDRESS STRING, C_NATIONKEY INTEGER REFERENCES NATION, C_PHONE STRING, C_ACCTBAL DOUBLE, C_MKTSEGMENT STRING, C_COMMENT STRING, PRIMARY KEY (C_CUSTKEY), INDEX IDX(C_NATIONKEY));\r\n" + 
        		"CREATE TABLE ORDERS (O_ORDERKEY INTEGER PRIMARY KEY, O_CUSTKEY INTEGER REFERENCES CUSTOMER, O_ORDERSTATUS STRING, O_TOTALPRICE DOUBLE, O_ORDERDATE DATE, O_ORDERPRIORITY STRING, O_CLERK STRING, O_SHIPPRIORITY INTEGER, O_COMMENT STRING, PRIMARY KEY (O_ORDERKEY), INDEX IDX(O_CUSTKEY));\r\n" + 
        		"CREATE TABLE LINEITEM (L_ORDERKEY INTEGER REFERENCES ORDERS, L_PARTKEY INTEGER REFERENCES PART, L_SUPPKEY INTEGER REFERENCES SUPPLIER, L_LINENUMBER INTEGER, L_QUANTITY DOUBLE, L_EXTENDEDPRICE DOUBLE, L_DISCOUNT DOUBLE, L_TAX DOUBLE, L_RETURNFLAG STRING, L_LINESTATUS STRING, L_SHIPDATE DATE, L_COMMITDATE DATE, L_RECEIPTDATE DATE, L_SHIPINSTRUCT STRING, L_SHIPMODE STRING, L_COMMENT STRING, PRIMARY KEY (L_ORDERKEY, L_LINENUMBER), INDEX IDX(L_ORDERKEY,L_PARTKEY,L_SUPPKEY));\r\n" + 
        		"CREATE TABLE NATION(N_NATIONKEY INTEGER PRIMARY KEY, N_NAME STRING, N_REGIONKEY INTEGER REFERENCES REGION, N_COMMENT STRING, PRIMARY KEY (N_NATIONKEY), INDEX IDX(N_REGIONKEY));\r\n" + 
        		"CREATE TABLE REGION(R_REGIONKEY INTEGER PRIMARY KEY, R_NAME STRING, R_COMMENT STRING, PRIMARY KEY (R_REGIONKEY));"
        		+ "SELECT S_SUPPKEY, L_RETURNFLAG, L_LINESTATUS, SUM(L_QUANTITY) AS SUM_QTY, SUM(L_EXTENDEDPRICE) AS SUM_BASE_PRICE, SUM(L_EXTENDEDPRICE*(1-L_DISCOUNT)) AS SUM_DISC_PRICE, SUM(L_EXTENDEDPRICE*(1-L_DISCOUNT)*(1+L_TAX)) AS SUM_CHARGE, AVG(L_QUANTITY) AS AVG_QTY, AVG(L_EXTENDEDPRICE) AS AVG_PRICE, AVG(L_DISCOUNT) AS AVG_DISC FROM LINEITEM, SUPPLIER WHERE L_SUPPKEY = S_SUPPKEY AND L_QUANTITY > 25 GROUP BY L_RETURNFLAG, L_LINESTATUS,S_SUPPKEY ORDER BY L_RETURNFLAG, L_LINESTATUS, S_SUPPKEY LIMIT 300;"
                                            );

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
