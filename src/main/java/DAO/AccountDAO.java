package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class AccountDAO {
    public Account insertAccount(Account account, String username, String password) {
        if (username == null || username.isBlank()) { 
            return null;
        }
        if (password == null || password.length() < 4) { 
            return null;
        }

        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("None of the Rows Affected ---> Account Creation Failed !!!.");
            }

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setAccount_id(rs.getInt(1));
                } else {
                    throw new SQLException("Account Creation Failed !!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return account;
    }


    public Account loginAccount(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        Account account = null;

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int accountId = rs.getInt("account_id");
                    String Username = rs.getString("username");
                    String Password = rs.getString("password");

                    account = new Account(accountId, Username, Password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    
    public List<Account> accountList() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int accountId = rs.getInt("account_id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");

                    Account account = new Account(accountId, username, password);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        Account account = null;

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, accountId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");

                    account = new Account(accountId, username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    
    public void updateAccount(int account_id, Account account) {
        String sql = "UPDATE account SET username = ?, password = ? WHERE account_id = ?";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setInt(3, account_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
