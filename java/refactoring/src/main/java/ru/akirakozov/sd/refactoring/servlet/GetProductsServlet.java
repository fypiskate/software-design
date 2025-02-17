package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabase;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    ProductsDatabase database = new ProductsDatabase();
    HtmlBuilder builder = new HtmlBuilder();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> products = database.getAll();
        for (String item : products){
            builder.addContent(item);
        }
        response.getWriter().println(builder.toString());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
