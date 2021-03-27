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
import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GetProductsServletTest {

    GetProductsServlet getServlet;

    @Mock
    HttpServletRequest myRequest;
    @Mock

    HttpServletResponse myResponse;

    Writer myWriter;
    ProductsDatabase database;

    @Before
    public void before() throws SQLException {
        MockitoAnnotations.initMocks(this);
        getServlet = new GetProductsServlet();
        myWriter = new StringWriter();
        database = new ProductsDatabase();

        database.createIfNotExists();
    }

    @After
    public void after() {
        database.dropTable();
    }

    private void createSimpleTable() {
        database.insert("apple", 5);
        database.insert("pear", 10);
        database.insert("banana", 15);
    }

    @Test
    public void testGetTreeProducts() throws IOException {
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        createSimpleTable();
        getServlet.doGet(myRequest, myResponse);

        assertEquals(
                "<html><body>\napple\t5</br>\npear\t10</br>\nbanana\t15</br>\n</body></html>\n",
                myWriter.toString());
    }


}


