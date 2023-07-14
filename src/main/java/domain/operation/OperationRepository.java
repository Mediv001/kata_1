package domain.operation;

import java.util.List;

public interface OperationRepository {
    List<Operation> getFor(String client);
    void persist(String client, Operation op);
}
