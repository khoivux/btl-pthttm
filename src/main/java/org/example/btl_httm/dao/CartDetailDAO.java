package org.example.btl_httm.dao;

import org.example.btl_httm.model.CartDetail;
import org.example.btl_httm.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class CartDetailDAO extends DAO{
    public CartDetailDAO() {
        super();
    }
    public boolean addOrUpdateCartDetail(CartDetail cartDetail) {
        String sql = " INSERT INTO tblCartDetail (quantity, tblProductId, tblCartid) VALUES (?, ?, ?)" +
        " ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartDetail.getQuantity());
            ps.setInt(2, cartDetail.getProduct().getId());
            ps.setInt(3, cartDetail.getCartId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCartDetail(List<Integer> idList) {
        String sqlIds = concatString(idList);
        String sql = "DELETE FROM tblCartDetail WHERE id in (" + sqlIds + ")";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < idList.size(); i++) {
                ps.setInt(i + 1, idList.get(i));
            }
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CartDetail> getByIdList(List<Integer> idList) {
        String sqlIds = concatString(idList);
        String sql = "SELECT cd.id AS cartDetailId, cd.quantity, " +
                " p.id AS productId, p.name, p.price" +
                " FROM tblCartDetail cd " +
                " JOIN tblProduct p ON cd.tblProductid = p.id " +
                " WHERE cd.id IN (" + sqlIds + ")";
        List<CartDetail> results = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < idList.size(); i++) {
                ps.setInt(i + 1, idList.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (results == null) {
                    results = new java.util.ArrayList<>();
                }
                CartDetail cd = new CartDetail();
                cd.setId(rs.getInt("cartDetailId"));
                cd.setQuantity(rs.getInt("quantity"));

                Product p = new Product();
                p.setId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getFloat("price"));

                cd.setProduct(p);
                results.add(cd);
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String concatString (List<Integer> idList) {
        String res = "";
        for (int i = 0; i < idList.size(); i++) {
            res += "?";
            if (i < idList.size() - 1) {
                res += ", ";
            }
        }
        return res;
    }
}
