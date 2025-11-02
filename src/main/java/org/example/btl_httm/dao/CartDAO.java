package org.example.btl_httm.dao;

import org.example.btl_httm.model.Cart;
import org.example.btl_httm.model.CartDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends DAO{
    public CartDAO() {
        super();
    }
    public boolean insertCart(Cart cart) {
        String sql = "INSERT INTO tblCart (id, lastModifiedTime) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, cart.getId());
            ps.setDate(2, cart.getLastModifiedTime());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Cart getCartById(int id) {
        String sqlSelectCart = "SELECT id, lastModifiedTime FROM tblCart WHERE id = ?";

        String sqlSelectCartDetail = "SELECT cd.id AS detailId, cd.quantity, " +
                " p.id AS productId, p.name, p.price" +
                " FROM tblCartDetail cd " +
                " JOIN tblProduct p ON cd.tblProductid = p.id " +
                " WHERE cd.tblCartid = ?";

        Cart cart = null;
        try {
            PreparedStatement ps = con.prepareStatement(sqlSelectCart);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setLastModifiedTime(rs.getDate("lastModifiedTime"));
            }
            if(cart == null) return null;

            List<CartDetail> cartDetailList = new ArrayList<>();
            ps = con.prepareStatement(sqlSelectCartDetail);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartDetail cd = new CartDetail();
                cd.setId(rs.getInt("detailId"));
                cd.setQuantity(rs.getInt("quantity"));

                ProductDAO productDAO = new ProductDAO();
                cd.setProduct(productDAO.getById(rs.getInt("productId")));
                cd.setTotalPrice(cd.getQuantity() * cd.getProduct().getPrice());
                cartDetailList.add(cd);
            }

            cart.setCartDetailList(cartDetailList);
            return cart;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
