package entity;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {
    private final Instant date;
    private final BigDecimal amount;
    private final TransactionType type;

    public Transaction(Instant date, BigDecimal amount, TransactionType type) {
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Instant getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}
