/* * ATM Example system - file Deposit.java    * * copyright (c) 2001 - Russell C. Bjork * */ package edu.gordon.atm.transaction;import edu.gordon.banking.Receipt;import edu.gordon.atm.physical.*;import edu.gordon.banking.AccountInformation;import edu.gordon.banking.Card;import edu.gordon.banking.Message;import edu.gordon.banking.Money;/** Representation for a deposit transaction */public class Deposit extends Transaction{    /** Constructor     *     *  @param edu.gordon.atm the ATM used to communicate with customer     *  @param session the session in which the transaction is being performed     *  @param card the customer's card     *  @param pin the PIN entered by the customer     */    public Deposit(ReceiptPrinter receiptPrinter, NetworkToBank networkToBank, CashDispenser cashDispenser, EnvelopeAcceptor envelopeAcceptor, String bankName, int id, String place, CardReader cardReader, CustomerConsole customerConsole, Card card, int pin)    {        super(receiptPrinter, networkToBank, cashDispenser, envelopeAcceptor, bankName, id, place, cardReader, customerConsole, card, pin);    }        /** Get specifics for the transaction from the customer     *     *  @return message to bank for initiating this transaction     *  @exception CustomerConsole.Cancelled if customer cancelled this transaction     */    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled    {    	//TODO    	//Set the from value by default.    	to = -1;    	    	//Upon JUnit test, prevent the system from sending request to graphical interface.    	if (!jUnitTest)    	{    		customerConsole.readMenuChoice("deposit_to", "Account to deposit to", AccountInformation.ACCOUNT_NAMES);    	}    	else    	{    		to = 1;    	}    	    	//Prevent the system from running until we receive a response.    	while (to == -1)    	{    		System.out.println("Deposit to block");    	}    	    	//-100 means the user cancelled the dialog, throw an exception.    	if (to == -100)    	{    		throw new CustomerConsole.Cancelled();    	}    	    	//Set the from value by default.       	amount = null;       	    	//Upon JUnit test, prevent the system from sending request to graphical interface.    	if (!jUnitTest)    	{    		customerConsole.readAmount(this, "Amount to deposit");    	}    	else    	{    		amount = new Money(40);    	}    	    	//If we didn't receive any money, it means the user cancelled the dialog.    	while (amount == null)    	{    		throw new CustomerConsole.Cancelled();    		//System.out.println("Deposit amount block");    	}    	        /*to = customerConsole.readMenuChoice(            "Account to deposit to",            AccountInformation.ACCOUNT_NAMES);        amount = customerConsole.readAmount("Amount to deposit");*/                return new Message(Message.INITIATE_DEPOSIT, card, pin, serialNumber, -1, to, amount);    }        /** Complete an approved transaction     *     *  @return receipt to be printed for this transaction     *  @exception CustomerConsole.Cancelled if customer cancelled or      *             transaction timed out     */    protected Receipt completeTransaction() throws CustomerConsole.Cancelled    {    	//TODO        this.envelopeAcceptor.acceptEnvelope();               Transaction.messageFormat = null;        this.networkToBank.sendMessage(new Message(Message.COMPLETE_DEPOSIT,card, pin, serialNumber, -1, to, amount), balances);        while(Transaction.messageFormat == null)        {    		System.out.println("MessageFormat deposit");        }                /*MessageFormat messageFormat = this.networkToBank.sendMessage(            new Message(Message.COMPLETE_DEPOSIT,                        card, pin, serialNumber, -1, to, amount),             balances);*/                  if (messageFormat.getBalances() != null && messageFormat.getBalances().getAvailable() != null && messageFormat.getBalances().getTotal() != null)        {            balances = reConvertBalances(messageFormat.getBalances());	        }        return new Receipt(this.bankName, this.id, this.place, this.getSerialNumber(), this.card, this.balances) {            {                detailsPortion = new String[2];                detailsPortion[0] = "DEPOSIT TO: " +                                     AccountInformation.ACCOUNT_ABBREVIATIONS[to];                detailsPortion[1] = "AMOUNT: " + amount.toString();            }        };    }        /** Account to deposit to     */     public static int to;        /** Amount of money to deposit     */    public static Money amount;            }