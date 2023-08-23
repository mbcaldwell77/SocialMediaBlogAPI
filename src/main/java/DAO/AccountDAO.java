package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    // Get account by account_id
    public Account getAccountById(int account_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Get account by username and password
    public Account getLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Adding a new user
    public Account addAccount(Account account) {

        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();

            ResultSet primeKey = ps.getGeneratedKeys();
            if (primeKey.next()) {
                int newAcctID = primeKey.getInt(1);
                return new Account(newAcctID, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
