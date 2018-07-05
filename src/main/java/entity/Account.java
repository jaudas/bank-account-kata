package entity;

import dateUtils.DateProvider;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static entity.TransactionType.*;

public class Account {

    private final List<Transaction> history = new ArrayList<>();

    private final DateProvider dateProvider;

    public Account(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    /**
     * get transactions for this account
     * @return unmodifiable list of previous transactions
     */
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * perform deposit transaction
     * @param amount amount to be added on the current account
     */
    public void performDeposit(BigDecimal amount){
        checkInputValidity(amount);
        addTransactionToHistory(amount, DEPOSIT);
    }

    /**
     * perform withdrawal transaction
     * @param amount amount to be withdrawn from the current account
     * @return true if transaction as correctly been executed
     */
    public boolean performWithdrawal(BigDecimal amount){
        checkInputValidity(amount);

        if (!hasEnoughMoney(amount)) {
            return false;
        }

        addTransactionToHistory(amount, WITHDRAWAL);

        return true;
    }

    private void checkInputValidity(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException();
    }

    private boolean hasEnoughMoney(BigDecimal amountToBeWithdraw) {
        return computeBalance().compareTo(amountToBeWithdraw) > 0;
    }

    void addTransactionToHistory(BigDecimal amount, TransactionType type){
        Instant date = dateProvider.getCurrentTime();
        Transaction transaction = new Transaction(date, amount, type);

        this.history.add(transaction);
    }

    /**
     * calculate account balance regarding previous transactions
     * @return balance
     */
    public BigDecimal computeBalance(){
        BigDecimal balance = BigDecimal.ZERO;

        for(Transaction transaction : history) {
            switch(transaction.getType()) {
                case WITHDRAWAL:
                    balance = balance.subtract(transaction.getAmount());
                    break;
                case DEPOSIT:
                    balance = balance.add(transaction.getAmount());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        return balance;
    }
}
