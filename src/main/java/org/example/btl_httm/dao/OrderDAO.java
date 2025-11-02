package org.example.btl_httm.dao;

import org.example.btl_httm.model.Order;
import org.example.btl_httm.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class OrderDAO extends DAO{
    public OrderDAO() {
        super();
    }

    public boolean insertOrder(Order order) {
        String sqlInsertOrder = "INSERT INTO tblOrder (tblUserid, createdTime) VALUES (?, ?)";
        String sqlInsertOrderDetail = "INSERT INTO tblOrderDetail (tblOrderid, tblProductid, quantity, unitPrice) VALUES (?, ?, ?, ?)";

        try {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(sqlInsertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUser().getId());
            ps.setTimestamp(2, new Timestamp(order.getCreatedTime().getTime()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int orderId = rs.getInt(1);

                for (OrderDetail detail : order.getOrderDetailList()) {
                    PreparedStatement psDetail = con.prepareStatement(sqlInsertOrderDetail);
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, detail.getProduct().getId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setFloat(4, detail.getUnitPrice());

                    psDetail.executeUpdate();
                }

                con.commit();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
