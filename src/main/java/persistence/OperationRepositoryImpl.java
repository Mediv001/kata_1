package persistence;

import domain.operation.Operation;
import domain.operation.OperationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperationRepositoryImpl implements OperationRepository {
    Map<String, List<Operation>> operations;

    public OperationRepositoryImpl() {
        this.operations = new ConcurrentHashMap<>();
    }

    public OperationRepositoryImpl(Map<String, List<Operation>> operations) {
        this.operations = operations;
    }

    @Override
    public List<Operation> getFor(String client) {
        return operations.getOrDefault(client, Collections.emptyList());
    }

    @Override
    public void persist(String client, Operation op) {
        List<Operation> ops = operations.getOrDefault(client, new ArrayList<>());
        ops.add(op);

        operations.put(client, ops);
    }
}
