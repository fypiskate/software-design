package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    ProductsDatabase database = new ProductsDatabase();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        response.getWriter().println("<html><body>");

        if ("max".equals(command)) {
            response.getWriter().println("<h1>Product with max price: </h1>");
            String maxProduct = database.getMax();
            response.getWriter().println(maxProduct);

        } else if ("min".equals(command)) {
            response.getWriter().println("<h1>Product with min price: </h1>");
            String minProduct = database.getMin();
            response.getWriter().println(minProduct);

        } else if ("sum".equals(command)) {
            response.getWriter().println("<h1>Summary price: </h1>");
            Long sumProducts = database.getSum();
            response.getWriter().println(sumProducts);

        } else if ("count".equals(command)) {
            response.getWriter().println("<h1>Number of products: </h1>");
            Long countProducts = database.count();
            response.getWriter().println(countProducts);

        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.getWriter().println("</body></html>");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
