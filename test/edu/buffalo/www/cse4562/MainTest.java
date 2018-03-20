package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private String schema;
    private List<String> queries;
    private Connection con;

    MainTest() throws IOException, SQLException {
        // get schema
        List<String> schema = Files.readAllLines(Paths.get("data/schema.sql"));
        StringBuilder createTable = new StringBuilder();
        for(String line : schema) {
            createTable.append(line);
        }
        this.schema = createTable.toString();
        // get queries
        this.queries = Files.readAllLines(Paths.get("data/queries.sql"));
        // db init
        this.con = DriverManager.getConnection("jdbc:sqlite:data/test.db");
    }

    private String getAnswer(String sql) throws SQLException {
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        StringBuilder answer = new StringBuilder();
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i));
            }
            String line = String.join("|", row);
            answer.append(line).append(System.lineSeparator());
        }

        stmt.close();
        return answer.toString();
    }

    /**
     *
     * @param sql SQL queries except create table
     * @param expected expected queries result
     * @param isOrder True if query has order by else False
     * @throws ParseException
     * @throws SQLException
     * @throws IOException
     */
    private void testFlow(String sql, String expected, boolean isOrder) throws ParseException, SQLException, IOException {
        sql = this.schema + sql;

        // init stdin and stdout
        ByteArrayInputStream in = new ByteArrayInputStream(sql.getBytes());
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        String[] args = {"args"};  // Meaningless args pass to main to run
        System.setIn(in);  // set stdin
        System.setOut(printStream);

        edu.buffalo.www.cse4562.Main.main(args);

        String actual = out.toString();
        // remove unnecessary chars
        actual = actual.replaceAll("\\$>\\s\r?\n|'", "");

        if (isOrder) {
            assertEquals(expected, actual);
        } else {
            Set<String> actualSet = new HashSet<>(Arrays.asList(actual.split("\r\n")));
            Set<String> expectedSet = new HashSet<>(Arrays.asList(expected.split("\r\n")));
            assertEquals(expectedSet, actualSet);
        }
    }

    @Test
    @DisplayName("Query 1")
    void query1() throws ParseException, SQLException, IOException {
        String sql = queries.get(0);
        String expected = getAnswer(sql);
        boolean isOrder = sql.toUpperCase().contains("ORDER BY");
        testFlow(sql, expected, isOrder);
    }

    @Test
    @DisplayName("Query 2")
    void query2() throws ParseException, SQLException, IOException {
        String sql = queries.get(1);
        String expected = getAnswer(sql);
        boolean isOrder = sql.toUpperCase().contains("ORDER BY");
        testFlow(sql, expected, isOrder);
    }

    @Test
    @DisplayName("Query 3")
    void query3() throws ParseException, SQLException, IOException {
        String sql = queries.get(2);
        String expected = getAnswer(sql);
        boolean isOrder = sql.toUpperCase().contains("ORDER BY");
        testFlow(sql, expected, isOrder);
    }

    @Test
    @DisplayName("Query 4")
    void query4() throws ParseException, SQLException, IOException {
        String sql = queries.get(3);
        String expected = getAnswer(sql);
        boolean isOrder = sql.toUpperCase().contains("ORDER BY");
        testFlow(sql, expected, isOrder);
    }

    @Test
    @DisplayName("Query 5")
    void query5() throws ParseException, SQLException, IOException {
        String sql = queries.get(4);
        String expected = getAnswer(sql);
        boolean isOrder = sql.toUpperCase().contains("ORDER BY");
        testFlow(sql, expected, isOrder);
        this.con.close();
    }

}