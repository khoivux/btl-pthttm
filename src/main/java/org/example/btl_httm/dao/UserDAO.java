package org.example.btl_httm.dao;

import org.example.btl_httm.model.RegisterDTO;
import org.example.btl_httm.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO extends DAO{
    public UserDAO() {
        super();
    }
    public User authenticate(User loginInfo) {
        String sql = "SELECT * FROM tblUser WHERE username = ? AND password = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, loginInfo.getUsername());
            ps.setString(2, loginInfo.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int  register(RegisterDTO registerDTO) {
        String sql = "INSERT INTO tblUser (username, password, fullname, email, phoneNumber) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, registerDTO.getUsername());
            ps.setString(2, registerDTO.getPassword());
            ps.setString(3, registerDTO.getFullname());
            ps.setString(4, registerDTO.getEmail());
            ps.setString(5, registerDTO.getPhoneNumber());
            int affectedRows = ps.executeUpdate();

            if(affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    int userId = rs.getInt(1);
                    return userId;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
