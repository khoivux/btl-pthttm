package org.example.btl_httm.dao;

import org.example.btl_httm.exception.OutOfStockException;
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

    public boolean insertOrder(Order order)  throws OutOfStockException {
        String sqlInsertOrder = "INSERT INTO tblOrder (tblUserid, createdTime) VALUES (?, ?)";
        String sqlInsertOrderDetail = "INSERT INTO tblOrderDetail (tblOrderid, tblProductid, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        String sqlCheckQuantity = "SELECT quantity, name FROM tblProduct WHERE id = ?";
        String sqlUpdateQuantity = "UPDATE tblProduct SET quantity = quantity - ? WHERE id = ?";

        try {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(sqlInsertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUser().getId());
            ps.setTimestamp(2, new Timestamp(order.getCreatedTime().getTime()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int orderId = rs.getInt(1);

                for (OrderDetail od : order.getOrderDetailList()) {

                    PreparedStatement psCheck = con.prepareStatement(sqlCheckQuantity);
                    psCheck.setInt(1, od.getProduct().getId());
                    ResultSet rsCheck = psCheck.executeQuery();
                    if(rsCheck.next()) {
                        int availableQuantity = rsCheck.getInt("quantity");
                        if(availableQuantity < od.getQuantity()) {
                            con.rollback();
                            throw new OutOfStockException(rsCheck.getString("name") +  " chỉ còn " + availableQuantity + " sản phẩm ");
                        }
                    }

                    PreparedStatement psDetail = con.prepareStatement(sqlInsertOrderDetail);
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, od.getProduct().getId());
                    psDetail.setInt(3, od.getQuantity());
                    psDetail.setFloat(4, od.getUnitPrice());
                    psDetail.executeUpdate();

                    PreparedStatement psUpdate = con.prepareStatement(sqlUpdateQuantity);
                    psUpdate.setInt(1, od.getQuantity());
                    psUpdate.setInt(2, od.getProduct().getId());
                    psUpdate.executeUpdate();
                }

                con.commit();
                return true;
            }

        } catch (OutOfStockException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
