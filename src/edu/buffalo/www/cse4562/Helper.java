package edu.buffalo.www.cse4562;

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
     * @param table two dimentional list of output table
     */
    public static void output(java.util.Iterator<List<String>> table) {

        for(;table.hasNext();) {
            String line = "";
            for(String cell : table.next()) {
                line = line + cell + " ";
            }
            line = line.trim().replace(" ", "|");
            print(line);
        }
    }
}
