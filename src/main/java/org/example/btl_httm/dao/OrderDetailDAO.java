package org.example.btl_httm.dao;

import org.example.btl_httm.model.OrderDetail;
import org.example.btl_httm.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO extends DAO{
    public OrderDetailDAO() {
        super();
    }
    public List<OrderDetail> getLastOrderDetailsByUserId(int userId, int limit) {
        List<OrderDetail> list = new ArrayList<>();

        String sql =
                "SELECT od.id AS od_id, od.quantity, od.unitPrice, " +
                        "       p.id AS p_id, p.name AS p_name, p.brand AS p_brand, " +
                        "       p.price AS p_price, p.des AS p_des, p.quantity AS p_quantity, " +
                        "       p.categorycode AS p_categorycode " +
                        "FROM tblorderdetail od " +
                        "JOIN tblorder o ON od.tblOrderid = o.id " +
                        "JOIN tblproduct p ON od.tblProductid = p.id " +
                        "WHERE o.tblUserid = ? " +
                        "ORDER BY o.createdTime DESC " +
                        "LIMIT ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // -------- PRODUCT --------
                Product p = new Product();
                p.setId(rs.getInt("p_id"));
                p.setName(rs.getString("p_name"));
                p.setBrand(rs.getString("p_brand"));
                p.setPrice(rs.getFloat("p_price"));
                p.setDes(rs.getString("p_des"));
                p.setQuantity(rs.getInt("p_quantity"));
                p.setCategoryCode(rs.getString("p_categorycode"));

                // -------- ORDERDETAIL --------
                OrderDetail od = new OrderDetail();
                od.setId(rs.getInt("od_id"));
                od.setQuantity(rs.getInt("quantity"));
                od.setUnitPrice(rs.getFloat("unitPrice"));
                od.setProduct(p);

                list.add(od);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
