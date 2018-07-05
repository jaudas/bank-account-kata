package service;

import entity.Account;
import entity.Transaction;
import entity.TransactionType;

import static java.lang.System.lineSeparator;

public class AccountStatementPrinter {

    private static final String ACCOUNT_STATEMENT_HEADER = "|\tTRANSACTION DATE\t\t|\tCREDIT\t|\tDEBIT\t|";
    private static final String ACCOUNT_BALANCE_PREFIX = "ACCOUNT BALANCE : ";

    String print(Account account){
        StringBuilder output = new StringBuilder();

        output.append(ACCOUNT_STATEMENT_HEADER);
        output.append(lineSeparator());

        for(Transaction transaction : account.getTransactionHistory()){
            appendTransaction(output, transaction);
            output.append(lineSeparator());
        }

        output.append(ACCOUNT_BALANCE_PREFIX);
        output.append(account.computeBalance());

        return output.toString();
    }

    void appendTransaction(StringBuilder sb, Transaction transaction) {
        sb.append("|\t");
        sb.append(transaction.getDate());
        sb.append("\t|\t\t");

        if(TransactionType.DEPOSIT.equals(transaction.getType()))
            sb.append(transaction.getAmount());

        sb.append("\t|\t\t");

        if(TransactionType.WITHDRAWAL.equals(transaction.getType()))
            sb.append(transaction.getAmount());

        sb.append("\t|");
    }
}
