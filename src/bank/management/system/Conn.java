package bank.management.system;

import java.sql.*;

public class Conn {

    Connection c;
    Statement s;

    public Conn() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database connection URL
            String url = "jdbc:mysql://localhost:3306/bankmanagementsystem?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "password";

            // Create connection
            c = DriverManager.getConnection(url, user, password);

            // Create statement
            s = c.createStatement();

            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}