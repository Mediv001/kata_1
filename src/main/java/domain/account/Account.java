package domain.account;

public class Account {
    private Double amount;
    private Double start;

    public Account(Double amount) {
        this.amount = amount;
        this.start = amount;
    }

    public boolean hasEnough(Double amount) {
        return this.amount >= amount;
    }

    public Double getStart() {
        return start;
    }
}
