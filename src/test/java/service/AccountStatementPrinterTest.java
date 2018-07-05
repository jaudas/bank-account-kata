package service;

import dateUtils.DefaultDateProvider;
import dateUtils.TestDateProvider;
import entity.Account;
import entity.Transaction;
import entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountStatementPrinterTest {

    private AccountStatementPrinter accountStatementPrinter;

    @BeforeEach
    void setUp() {
        accountStatementPrinter = new AccountStatementPrinter();
    }

    @Test
    void should_print_a_deposit_amount_in_the_credit_column() {
        String expected = "|\t2018-06-20T11:30:00Z\t|\t\t100\t|\t\t\t|";

        LocalDateTime dateTime = LocalDateTime.of(2018, 6, 20, 11, 30);

        StringBuilder actual = new StringBuilder();
        Transaction transaction = new Transaction(dateTime.toInstant(ZoneOffset.UTC), BigDecimal.valueOf(100), TransactionType.DEPOSIT);

        accountStatementPrinter.appendTransaction(actual, transaction);

        assertEquals(expected, actual.toString());
    }

    @Test
    void should_print_a_withdrawal_amount_in_the_debit_column() {
        String expected = "|\t2018-06-20T11:30:00Z\t|\t\t\t|\t\t100\t|";

        LocalDateTime dateTime = LocalDateTime.of(2018, 6, 20, 11, 30);

        StringBuilder actual = new StringBuilder();
        Transaction transaction = new Transaction(dateTime.toInstant(ZoneOffset.UTC), BigDecimal.valueOf(100), TransactionType.WITHDRAWAL);

        accountStatementPrinter.appendTransaction(actual, transaction);

        assertEquals(expected, actual.toString());
    }

    @Test
    void should_print_account_statement_in_chronological_order(){
        // Arrange
        StringBuilder expected = new StringBuilder();
        expected.append("|\tTRANSACTION DATE\t\t|\tCREDIT\t|\tDEBIT\t|");
        expected.append(lineSeparator());
        expected.append("|\t2018-06-20T11:30:00Z\t|\t\t100\t|\t\t\t|");
        expected.append(lineSeparator());
        expected.append("|\t2018-06-20T11:40:00Z\t|\t\t\t|\t\t50\t|");
        expected.append(lineSeparator());
        expected.append("|\t2018-06-20T11:50:00Z\t|\t\t\t|\t\t20\t|");
        expected.append(lineSeparator());
        expected.append("ACCOUNT BALANCE : 30");

        LocalDateTime dateTime = LocalDateTime.of(2018, 6, 20, 11, 20);

        TestDateProvider dateProvider = new TestDateProvider(dateTime.toInstant(ZoneOffset.UTC), 600L);

        // Act
        Account account = new Account(dateProvider);

        account.performDeposit(BigDecimal.valueOf(100));
        account.performWithdrawal(BigDecimal.valueOf(50));
        account.performWithdrawal(BigDecimal.valueOf(20));

        String actual = accountStatementPrinter.print(account);

        // Assert
        assertEquals(expected.toString(), actual);
    }

    @Test
    void account_without_transaction_should_print_an_empty_account_statement(){
        StringBuilder expected = new StringBuilder();
        expected.append("|\tTRANSACTION DATE\t\t|\tCREDIT\t|\tDEBIT\t|");
        expected.append(lineSeparator());
        expected.append("ACCOUNT BALANCE : 0");

        Account account = new Account(new DefaultDateProvider());

        String actual = accountStatementPrinter.print(account);

        assertEquals(expected.toString(), actual);
    }
}