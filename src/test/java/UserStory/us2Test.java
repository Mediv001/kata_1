package UserStory;

import domain.account.Account;
import domain.bank.BankService;
import domain.operation.Operation;
import domain.operation.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.AccountRepositoryImpl;
import persistence.OperationRepositoryImpl;
import service.BankServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class us2Test {
    private BankService service;

    @BeforeEach
    public void init() {
        service = new BankServiceImpl(
                new AccountRepositoryImpl(),
                new OperationRepositoryImpl()
        );
    }

    @Test
    public void withdrawal_with_nothing() {
        boolean applied = service.withdrawal("client", 100.);
        assert !applied;
    }

    @Test
    public void withdrawal_with_no_client() {
        boolean applied = service.withdrawal("client", 100.);
        assert !applied;
    }

    @Test
    public void withdrawal_with_amount_client() {
        Account testAccount = new Account(100.);
        service = new BankServiceImpl(
                new AccountRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("client", testAccount);
                }}),
                new OperationRepositoryImpl(new ConcurrentHashMap<>() {
                    {
                        put("client", new ArrayList<>() {{
                            add(new Operation(10., LocalDateTime.of(2020, 10, 12, 23, 1), OperationType.DEPOSIT));
                            add(new Operation(10., LocalDateTime.of(2020, 10, 12, 23, 1), OperationType.WITHDRAWAL));
                        }});
                    }
                })
        );

        boolean applied = service.withdrawal("client", 100.);
        assert applied;

        assert service.balance("client", LocalDateTime.now().plusMinutes(1)).equals(0.);
    }

    @Test
    public void withdrawal_with_negative_amount_client() {
        boolean applied = service.withdrawal("client", -100.);
        assert !applied;
    }

    @Test
    public void withdrawal_with_not_enough_start_client() {
        Account testAccount = new Account(100.);
        service = new BankServiceImpl(
                new AccountRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("client", testAccount);
                }}),
                new OperationRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("client", new ArrayList<>() {{
                        add(new Operation(10.,LocalDateTime.of(2020, 10,12,23,1),OperationType.DEPOSIT));
                    }});
                }})
        );

        boolean applied = service.withdrawal("client", 1000.);
        assert !applied;
    }

    @Test
    public void withdrawal_with_not_enough_balance_client() {
        Account testAccount = new Account(100.);
        service = new BankServiceImpl(
                new AccountRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("client", testAccount);
                }}),
                new OperationRepositoryImpl(new ConcurrentHashMap<>() {{
                    put("client", new ArrayList<>() {{
                            add(new Operation(50., LocalDateTime.of(2020, 10, 12, 23, 1), OperationType.WITHDRAWAL));
                    }});
                }})
        );

        boolean applied = service.withdrawal("client", 90.);
        assert !applied;
    }
}
