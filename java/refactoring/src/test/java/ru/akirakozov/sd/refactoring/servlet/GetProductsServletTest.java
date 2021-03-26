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

public class GetProductsServletTest {

    GetProductsServlet getServlet;

    @Mock
    HttpServletRequest myRequest;
    @Mock

    HttpServletResponse myResponse;

    Writer myWriter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        getServlet = new GetProductsServlet();
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

    private void createSimpleTable() {
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES ('apple', 5), ('pear', 10), ('banana', 15)";

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement s = c.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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


