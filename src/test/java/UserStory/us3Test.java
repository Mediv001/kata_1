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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class us3Test {

    private BankService service;

    @BeforeEach
    public void init() {
        service = new BankServiceImpl(
                new AccountRepositoryImpl(
                        new ConcurrentHashMap<>() {{
                            put("TEST_1", new Account(100.));
                            put("TEST_2", new Account(200.));
                        }}
                ),
                new OperationRepositoryImpl(
                        new ConcurrentHashMap<>() {{
                            put("TEST_1", Arrays.asList(
                                    new Operation(10., LocalDateTime.of(2020, 10, 12, 23, 1), OperationType.DEPOSIT),
                                    new Operation(10., LocalDateTime.of(2022, 10, 12, 23, 1), OperationType.DEPOSIT),
                                    new Operation(10., LocalDateTime.of(2021, 10, 12, 23, 1), OperationType.WITHDRAWAL),
                                    new Operation(10., LocalDateTime.now(), OperationType.DEPOSIT),
                                    new Operation(10., LocalDateTime.now().plusYears(1), OperationType.DEPOSIT)
                            ));
                        }}
                )
        );
    }

    @Test
    public void history_balance_without_ops() {
        Double actual = service.balance("TEST_2", LocalDateTime.now().plusMinutes(1));

        assert actual.equals(200.);
    }

    @Test
    public void history_balance_at_a_time() {
        Double actual = service.balance("TEST_1", LocalDateTime.now().plusMinutes(1));

        assert actual.equals(120.);
    }

    @Test
    public void history_operation_at_a_time() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
        List<Operation> operations = service.getOperationFor("TEST_1", time);

        assert operations.size() == 4;
        for (Operation operation: operations) {
            assert operation.getDate().isBefore(time);
        }
    }
}
