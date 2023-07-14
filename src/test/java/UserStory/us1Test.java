package UserStory;

import domain.account.Account;
import domain.bank.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.AccountRepositoryImpl;
import persistence.OperationRepositoryImpl;
import service.BankServiceImpl;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class us1Test {

    private BankService service;

    @BeforeEach
    public void init() {
        service = new BankServiceImpl(
                new AccountRepositoryImpl(),
                new OperationRepositoryImpl()
        );
    }

    @Test
    public void deposit_with_nothing() {
        boolean applied = service.deposit("TEST_1", 0.);
        assert !applied;
    }

    @Test
    public void deposit_with_no_client() {
        boolean applied = service.deposit("TEST_2", 100.);
        assert applied;

        Double balance = service.balance("TEST_2", LocalDateTime.now().plusMinutes(1));
        assert balance.equals(100.);
    }

    @Test
    public void deposit_with_amount_client() {
        Account testAccount = new Account(100.);
        service = new BankServiceImpl(
                new AccountRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("TEST_3", testAccount);
                }}),
                new OperationRepositoryImpl()
        );

        boolean applied = service.deposit("TEST_3", 20.);
        assert applied;

        assert service.balance("TEST_3", LocalDateTime.now().plusMinutes(1)).equals(120.);
    }

    @Test
    public void deposit_with_negative_amount() {
        Account testAccount = new Account(100.);
        service = new BankServiceImpl(
                new AccountRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("TEST_4", testAccount);
                }}),
                new OperationRepositoryImpl()
        );

        boolean applied = service.deposit("TEST_4", -20.);
        assert !applied;
    }
}