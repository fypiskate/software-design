package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabase;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    ProductsDatabase database = new ProductsDatabase();
    HtmlBuilder builder = new HtmlBuilder();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            builder.addH1("Product with max price: ");
            String maxProduct = database.getMax();
            builder.addContent(maxProduct);

        } else if ("min".equals(command)) {
            builder.addH1("Product with min price: ");
            String minProduct = database.getMin();
            builder.addContent(minProduct);

        } else if ("sum".equals(command)) {
            builder.addH1("Summary price: ");
            Long sumProducts = database.getSum();
            builder.addContent(sumProducts.toString());

        } else if ("count".equals(command)) {
            builder.addH1("Number of products: ");
            Long countProducts = database.count();
            builder.addContent(countProducts.toString());

        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.getWriter().println(builder.toString());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
