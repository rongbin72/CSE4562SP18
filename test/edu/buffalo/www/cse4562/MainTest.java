package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private String getSchema() throws IOException {
        List<String> schema = Files.readAllLines(Paths.get("data/schema.sql"));
        String creatTable = "";
        for(String line : schema) {
            creatTable += line;
        }
        return creatTable;
    }

    private String getQuery(int index) throws IOException {
        List<String> queries = Files.readAllLines(Paths.get("data/queries.sql"));
        return queries.get(index);
    }

    /**
     *
     * @param sql SQL queries except create table
     * @param expected expected queries result
     * @throws ParseException
     * @throws SQLException
     * @throws IOException
     */
    private void testFlow(String sql, String expected) throws ParseException, SQLException, IOException {
        String create = getSchema();
        sql = create + sql;

        // init stdin and stdout
        ByteArrayInputStream in = new ByteArrayInputStream(sql.getBytes());
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        String[] args = {"args"};  // Meaningless args pass to main to run
        System.setIn(in);  // set stdin
        System.setOut(printStream);

        Main.main(args);

        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test subselect with where statement
     */
    @Test
    @DisplayName("Sample")
    void sample() throws ParseException, SQLException, IOException {
        /*
        There should be a space at the end of each line except for the last line
        Each statement keyword(select, from, wHeRe, etc.) should be case insensitive
        while statement argument should be case sensitive
         */

        String sql = getQuery(0);

        /*
        Be careful with space and line separator, keep this format
        String should be surrounded by ' ' => 'String'
         */
        String expected =
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "3.0|7.0|43.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "4.0|7.0|40.0\r\n" +
                "$> \r\n";

        testFlow(sql, expected);
    }
}