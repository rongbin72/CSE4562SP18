package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    /**
     * Always call <code>flsuh()</code> after <code>println()</code>
     * 
     * @param str The <code>String</code> to be printed
     */
    public static void print(String str) {
        System.out.println(str);
        System.out.flush();
    }

    /**
     * Print the prompt <code>$> </code>
     */
    public static void prompt() {
        System.out.println("$> ");
        System.out.flush();
    }
    
    /**
     * Print out result
     * 
     * @param table iterator of two dimentional list of output table
     */
    public static void output(java.util.Iterator<List<String>> table) {
        for(;table.hasNext();) {
            List<String> row = table.next();
            print(String.join("|", row));
        }
    }

    /**
     *
     * @param tableName table name of current table
     * @param line string returned by <code>readline</code>
     * @return tuple of <code>PrimitiveValue</code>
     */
    public static List<PrimitiveValue> toPrimitive(String tableName, String line) {
        List<PrimitiveValue> tuple = new ArrayList<>();

        return tuple;
    }
    
}
