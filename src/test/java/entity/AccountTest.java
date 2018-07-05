package entity;

import dateUtils.DefaultDateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(new DefaultDateProvider());
    }

    @Test
    public void at_creation_account_should_have_no_transaction(){
        int nbTransaction = account.getTransactionHistory().size();

        assertEquals(0, nbTransaction);
    }

    @Test
    public void at_creation_account_should_have_zero_as_balance(){
        BigDecimal initialBalance = account.computeBalance();

        assertEquals(BigDecimal.ZERO, initialBalance);
    }

    @Test
    public void getBalance_should_return_sum_of_all_transactions(){
        //given
        account.performDeposit(BigDecimal.valueOf(100));
        account.performWithdrawal(BigDecimal.TEN);
        account.performWithdrawal(BigDecimal.valueOf(50));

        //when
        final BigDecimal accountBalance = account.computeBalance();

        //then
        assertEquals(BigDecimal.valueOf(40), accountBalance);
    }

    @Test
    public void performWithdrawal_should_not_accept_negative_value_for_transaction(){
        Assertions.assertThrows(InvalidParameterException.class, () -> account.performWithdrawal(BigDecimal.valueOf(-6)));
    }

    @Test
    public void performDeposit_should_not_accept_negative_value_for_transaction(){
        Assertions.assertThrows(InvalidParameterException.class, () -> account.performDeposit(BigDecimal.valueOf(-6)));
    }

    @Test
    public void performDeposit_should_update_account_balance(){
        account.performDeposit(BigDecimal.TEN);

        assertEquals(BigDecimal.TEN, account.computeBalance());
    }

    @Test
    public void performWithdrawal_should_not_allow_overdraft(){
        Account accountMock = Mockito.spy(account);

        boolean isWithdrawalSuccess = accountMock.performWithdrawal(BigDecimal.TEN);

        //verify balance is not updated and addTransactionToHistory method never called
        assertEquals(BigDecimal.ZERO, account.computeBalance());
        assertFalse(isWithdrawalSuccess);
        verify(accountMock, Mockito.times(0)).addTransactionToHistory(BigDecimal.TEN, TransactionType.WITHDRAWAL);
    }


}