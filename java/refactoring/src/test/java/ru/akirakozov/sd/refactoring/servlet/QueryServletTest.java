package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class QueryServletTest {

    QueryServlet queryServlet;

    @Mock
    HttpServletRequest myRequest;

    @Mock
    HttpServletResponse myResponse;

    Writer myWriter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        queryServlet = new QueryServlet();
        myWriter = new StringWriter();

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @After
    public void after() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void makeRequest(String command) throws IOException {
        when(myRequest.getParameter("command")).thenReturn(command);
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));

        queryServlet.doGet(myRequest, myResponse);
    }

    @Test
    public void minForEmptyTableTest() throws IOException {
        makeRequest("min");

        assertEquals("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void maxForEmptyTableTest() throws IOException {
        makeRequest("max");

        assertEquals("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void sumForEmptyTableTest() throws IOException {
        makeRequest("sum");

        assertEquals("<html><body>\n" +
                "Summary price: \n" +
                "0\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void countForEmptyTableTest() throws IOException {
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "Number of products: \n" +
                "0\n" +
                "</body></html>\n", myWriter.toString());

    }

    private void createTable() {
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES ('apple', 5), ('pear', 10), ('banana', 15)";

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void minForTableTest() throws IOException {
        createTable();
        makeRequest("min");

        assertEquals("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "apple\t5</br>\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void maxForTableTest() throws IOException {
        createTable();
        makeRequest("max");

        assertEquals("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "banana\t15</br>\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void sumForTableTest() throws IOException {
        createTable();
        makeRequest("sum");

        assertEquals("<html><body>\n" +
                "Summary price: \n" +
                "30\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void countForTableTest() throws IOException {
        createTable();
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "Number of products: \n" +
                "3\n" +
                "</body></html>\n", myWriter.toString());
    }
}
