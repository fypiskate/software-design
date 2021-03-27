package ru.akirakozov.sd.refactoring.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDatabase {

    public ProductsDatabase(){};

    public void createIfNotExists() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public void dropTable(){
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void insert(String name, long price) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAll() {
        return dataStringsFromDB("SELECT * FROM PRODUCT");
    }

    public String getMax() {
        return dataStringsFromDB("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")
                .stream()
                .findFirst()
                .orElse("");
    }

    public String getMin() {
        return dataStringsFromDB("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")
                .stream()
                .findFirst()
                .orElse("");
    }

    public List<String> dataStringsFromDB(String sql) {
        List<String> res = new ArrayList<>();

        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    res.add(name + "\t" + price + "</br>");
                }

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public Long getSum() {
        return dataLongFromDB("SELECT SUM(price) FROM PRODUCT");
    }

    public Long count() {
        return dataLongFromDB("SELECT COUNT(*) FROM PRODUCT");
    }

    public Long dataLongFromDB(String sql) {
        long res = 0L;
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    res = rs.getLong(1);
                }

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
