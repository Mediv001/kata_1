package domain.bank;

import domain.operation.Operation;

import java.time.LocalDateTime;
import java.util.List;

public interface BankService {
    Double balance(String clientId, LocalDateTime time);
    List<Operation> getOperationFor(String clientId, LocalDateTime time);
    boolean deposit(String clientId, Double amount);
    boolean withdrawal(String clientId, Double amount);
}
