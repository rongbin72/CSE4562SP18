package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    /**
     *
     * @param sql SQL queries except create table
     * @param expected expected queries result
     * @throws ParseException
     * @throws SQLException
     * @throws IOException
     */
    private void testFlow(String sql, String expected) throws ParseException, SQLException, IOException {
        String create =
                "CREATE table R(A INT, B iNt);" +
                "crEaTe tAbLE PLAYERS(" +
                "ID STRInG, " +
                "FIRSTNAME sTriNg, " +
                "LASTNAME STRING, " +
                "FIRSTSEASON DECIMAL, " +
                "LASTSEASON iNt, " +
                "WEIGHT deCiMal, " +
                "BIRTHDATE dATe);";

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
        String sql =
                "select FIRSTNAME, ID, FIRSTSEASON " +
                "FROM (SELECT FIRSTNAME, LASTSEASON, ID, FIRSTSEASON FrOm PLAYERS) Q " +
                "wHeRe Q.FIRSTSEASON > 1990;";

        /*
        Be careful with space and line separator, keep this format
        String should be surrounded by ' ' => 'String'
         */
        String expected =
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "'T;ariq abc'|'ABDULTA01'|1997.0\r\n" +
                "'S.hareef abc'|'ABDURSH01'|1996.0\r\n" +
                "'A=lex abc'|'ACKERAL01'|2005.0\r\n" +
                "$> \r\n";

        testFlow(sql, expected);
    }
}