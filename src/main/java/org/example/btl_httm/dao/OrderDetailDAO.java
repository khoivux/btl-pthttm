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
                        "       p.id, p.name, p.brand, p.price, p.des, p.quantity AS pquantity, p.categorycode  " +
                        " FROM tblorderdetail od " +
                        " JOIN tblorder o ON od.tblOrderid = o.id " +
                        " JOIN tblproduct p ON od.tblProductid = p.id " +
                        " WHERE o.tblUserid = ? " +
                        " ORDER BY o.createdTime DESC " +
                        " LIMIT ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setBrand(rs.getString("brand"));
                p.setPrice(rs.getFloat("price"));
                p.setDes(rs.getString("des"));
                p.setQuantity(rs.getInt("pquantity"));
                p.setCategoryCode(rs.getString("categorycode"));

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
