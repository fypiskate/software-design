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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        addServlet = new AddProductServlet();
        myWriter = new StringWriter();
        products = new ArrayList<>();
        prices = new ArrayList<>();
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
