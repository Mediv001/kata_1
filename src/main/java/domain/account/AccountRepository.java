package domain.account;

public interface AccountRepository {
    Account getFor(String client);
    void persist(String client, Account account);
}
