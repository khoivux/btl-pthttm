package org.example.btl_httm.dao;

import org.example.btl_httm.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO extends DAO{
    public ProductDAO() {
        super();
    }

    public List<Product> getProductsByIdList(List<Integer> idList) {
        List<Product> results = new ArrayList<>();

        if (idList == null || idList.isEmpty()) {
            return results;
        }

        String sqlIds = "";
        for (int i = 0; i < idList.size(); i++) {
            sqlIds += "?";
            if (i < idList.size() - 1) {
                sqlIds += ", ";
            }
        }

        String sql = "SELECT id, brand, name, price, des, quantity FROM tblProduct WHERE id IN (" + sqlIds + ")";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 0; i < idList.size(); i++) {
                ps.setInt(i + 1, idList.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setBrand(rs.getString("brand"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getFloat("price"));
                p.setDes(rs.getString("des"));
                p.setQuantity(rs.getInt("quantity"));
                results.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    public Product getById(int id) {

        String sql = "SELECT id, brand, name, price, des, quantity FROM tblProduct WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setBrand(rs.getString("brand"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getFloat("price"));
                p.setDes(rs.getString("des"));
                p.setQuantity(rs.getInt("quantity"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
