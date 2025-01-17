package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.accountList();
    }

    public Account addAccount(Account account, String username, String password) {
        return accountDAO.insertAccount(account, username, password);
    }

    public Account updateAccount(int accountId, Account account) {
        if (accountDAO.getAccountById(accountId) == null) {
            return null;
        }

        accountDAO.updateAccount(accountId, account);
        return account;
    }

    public Account loginAccount(String username, String password) {
        
        return accountDAO.loginAccount(username, password);
    }
}
