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
                "CREATE table R(A INT, B int);" +
                "create TABLE PLAYERS(" +
                "ID STRING, " +
                "FIRSTNAME string, " +
                "LASTNAME STRING, " +
                "FIRSTSEASON DECIMAL, " +
                "LASTSEASON int, " +
                "WEIGHT decimal, " +
                "BIRTHDATE date);";

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
        // There should be a space at the end of each line except for the last line
        String sql =
                "select FIRSTNAME, ID, FIRSTSEASON " +
                "FROM (SELECT FIRSTNAME, LASTSEASON, ID, FIRSTSEASON from PLAYERS) Q " +
                "where Q.FIRSTSEASON >= 200;";

        // be careful about line separator
        String expected =
                "$> \r\n" +
                "$> \r\n" +
                "$> \r\n" +
                "'Alaa abc'|'ABDELAL01'|1990.0\r\n" +
                "'Kareem abc'|'ABDULKA01'|1969.0\r\n" +
                "'M'ahmo abc'|'ABDULMA01'|1990.0\r\n" +
                "'T;ariq abc'|'ABDULTA01'|1997.0\r\n" +
                "'S.hareef abc'|'ABDURSH01'|1996.0\r\n" +
                "'T,om abc'|'ABERNTO01'|1976.0\r\n" +
                "'J-ohn abc'|'ABRAMJO01'|1946.0\r\n" +
                "'A=lex abc'|'ACKERAL01'|2005.0\r\n" +
                "'Donald abc'|'ACKERDO01'|1953.0\r\n" +
                "'Mark abc'|'ACRESMA01'|1987.0\r\n" +
                "'Charles abc'|'ACTONCH01'|1967.0\r\n" +
                "$> \r\n";

        testFlow(sql, expected);
    }
}