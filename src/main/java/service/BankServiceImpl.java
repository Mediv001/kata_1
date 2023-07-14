package service;

import domain.account.Account;
import domain.account.AccountRepository;
import domain.bank.BankService;
import domain.operation.Operation;
import domain.operation.OperationRepository;
import domain.operation.OperationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public BankServiceImpl(AccountRepository ar, OperationRepository or) {
        accountRepository = ar;
        operationRepository = or;
    }

    private Double computeBalance(Double start, List<Operation> operations) {
        Double balance = start;
        for (Operation op : operations) {
            switch (op.getType()) {
                case WITHDRAWAL -> balance -= op.getAmount();
                case DEPOSIT -> balance += op.getAmount();
            }
        }

        return balance;
    }

    @Override
    public Double balance(String clientId, LocalDateTime time) {
        List<Operation> operations = operationRepository.getFor(clientId)
                .stream().filter(o -> o.getDate().isBefore(time)).toList();

        Account account = accountRepository.getFor(clientId);

        if (account != null) {
            return computeBalance(account.getStart(), operations);
        }

        return 0.;
    }

    @Override
    public List<Operation> getOperationFor(String clientId, LocalDateTime time) {
        return operationRepository.getFor(clientId).stream().filter(o -> o.getDate().isBefore(time)).collect(Collectors.toList());
    }

    @Override
    public boolean deposit(String clientId, Double amount) {
        Account clientAccount = accountRepository.getFor(clientId);
        if (amount <= 0) {
            return false;
        }

        if (clientAccount == null) {
            accountRepository.persist(clientId, new Account(amount));
        } else {
            operationRepository.persist(clientId, new Operation(amount, LocalDateTime.now(), OperationType.DEPOSIT));
        }

        return true;
    }

    @Override
    public boolean withdrawal(String clientId, Double amount) {
        Account clientAccount = accountRepository.getFor(clientId);
        if (amount <= 0 || clientAccount == null || !clientAccount.hasEnough(amount)) {
            return false;
        }

        Double balance = computeBalance(clientAccount.getStart(), operationRepository.getFor(clientId));
        if (balance < amount) {
            return false;
        }

        operationRepository.persist(clientId, new Operation(amount, LocalDateTime.now(), OperationType.WITHDRAWAL));

        return true;
    }
}
