package persistence;

import domain.account.Account;
import domain.account.AccountRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepositoryImpl implements AccountRepository {

    Map<String, Account> clients;

    public AccountRepositoryImpl() {
        clients = new ConcurrentHashMap<>();
    }

    public AccountRepositoryImpl(Map<String, Account> baseClients) {
        this.clients = baseClients;
    }

    @Override
    public void persist(String client, Account account) {
        clients.put(client, account);
    }

    @Override
    public Account getFor(String client) {
        return clients.getOrDefault(client, null);
    }
}
