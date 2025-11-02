package org.example.btl_httm.dao;


import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
    protected Connection con;

    public DAO() {
        try {
            String url = "jdbc:mysql://localhost:3306/btl_httm";
            String username = "root";
            String password = "khoi21102004";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}