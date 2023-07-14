package domain.operation;

import java.time.LocalDateTime;

public class Operation {
    private Double amount;
    private LocalDateTime date;
    private OperationType type;

    public Operation(Double amount, LocalDateTime date, OperationType type) {
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public OperationType getType() {
        return type;
    }
}
