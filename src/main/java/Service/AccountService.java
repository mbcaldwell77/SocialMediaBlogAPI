package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO;

    // Empty constructor
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Constructor with provided accountDAO
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Get account by account_id
    public Account getAccountById(int account_id) {
        return accountDAO.getAccountById(account_id);
    }

    // Get account by username and password
    public Account getLogin(String username, String password) {

        return accountDAO.getLogin(username, password);
    }

    // Adding a user
    public Account addAccount(Account account) {
        Account persistedAccount = accountDAO.addAccount(account);

        return persistedAccount;
    }

}
