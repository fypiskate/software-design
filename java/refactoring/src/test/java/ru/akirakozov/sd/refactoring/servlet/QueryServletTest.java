package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.database.ProductsDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class QueryServletTest {

    QueryServlet queryServlet;

    @Mock
    HttpServletRequest myRequest;

    @Mock
    HttpServletResponse myResponse;

    Writer myWriter;
    ProductsDatabase database;

    @Before
    public void before() throws SQLException {
        MockitoAnnotations.initMocks(this);
        queryServlet = new QueryServlet();
        myWriter = new StringWriter();

        database = new ProductsDatabase();
        database.createIfNotExists();
    }

    @After
    public void after() {
        database.dropTable();
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
                "<h1>Summary price: </h1>\n" +
                "0\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void countForEmptyTableTest() throws IOException {
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "<h1>Number of products: </h1>\n" +
                "0\n" +
                "</body></html>\n", myWriter.toString());

    }

    private void createTable() {
        database.insert("apple", 5);
        database.insert("pear", 10);
        database.insert("banana", 15);
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
                "<h1>Summary price: </h1>\n" +
                "30\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void countForTableTest() throws IOException {
        createTable();
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "<h1>Number of products: </h1>\n" +
                "3\n" +
                "</body></html>\n", myWriter.toString());
    }
}
