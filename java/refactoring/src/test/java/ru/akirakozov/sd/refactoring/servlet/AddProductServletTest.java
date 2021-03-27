package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.database.ProductsDatabase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AddProductServletTest {

    AddProductServlet addServlet;

    @Mock
    HttpServletRequest myRequest;

    @Mock
    HttpServletResponse myResponse;

    Writer myWriter;
    List<String> products;
    List<Integer> prices;
    ProductsDatabase database;

    @Before
    public void before() throws SQLException {
        MockitoAnnotations.initMocks(this);
        addServlet = new AddProductServlet();
        myWriter = new StringWriter();
        products = new ArrayList<>();
        prices = new ArrayList<>();

        database = new ProductsDatabase();
        database.createIfNotExists();

    }

    @After
    public void after() {
        database.dropTable();
    }


    private void makeRequest(String name, int price) throws IOException {
        when(myRequest.getParameter("name")).thenReturn(name);
        when(myRequest.getParameter("price")).thenReturn(price + "");
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        addServlet.doGet(myRequest, myResponse);
    }

    @Test
    public void addOneProduct() throws IOException {
        makeRequest("apple", 5);
        assertEquals("OK\n", myWriter.toString());
    }

    @Test
    public void addManyProducts() throws IOException {
        makeRequest("apple", 5);
        makeRequest("pear", 10);
        makeRequest("banana", 15);
        assertEquals("OK\nOK\nOK\n", myWriter.toString());
    }

    @Test
    public void addWithNullName() throws IOException {
        makeRequest(null, 5);
        assertEquals("OK\n", myWriter.toString());
    }

}
