/* * ATM Example system - file NetworkToBank.java * * copyright (c) 2001 - Russell C. Bjork * */ package edu.gordon.atm.physical;import java.net.InetAddress;import com.google.common.eventbus.EventBus;import edu.gordon.atm.broadcaster.BroadcastBalances;import edu.gordon.atm.broadcaster.BroadcastMessage;import edu.gordon.atm.broadcaster.MessageFormat;import edu.gordon.atm.transaction.Transaction;import edu.gordon.banking.Balances;import edu.gordon.banking.Message;import edu.gordon.pubsub.upper.SendMessageComponent;import edu.gordon.pubsub.upper.SendMessageEvent;/** Manager for the ATM's network connection.  In a real ATM, this would  *  manage a physical device; in this edu.gordon.simulation,  it uses classes  *  in package edu.gordon.simulation to simulate the device. */ public class NetworkToBank{	private EventBus eventBus;    /** Constructor     *     *  @param log the log in which to record sending of messages and responses     *  @param bankAddress the network address of the bank     */    public NetworkToBank(Log log, InetAddress bankAddress)    {        NetworkToBank.log = log;        eventBus = new EventBus();    	eventBus.register(new SendMessageComponent());    }        /** Open connection to bank at system startup     */    public void openConnection()    {        // Since the network is simulated, we don't have to do anything    }        /** Close connection to bank at system shutdown     */    public void closeConnection()    {        // Since the network is simulated, we don't have to do anything    }        /** Send a message to bank     *     *  @param message the message to send     *  @param balances (out) balances in customer's account as reported     *         by bank     *  @return status code returned by bank     */    public void sendMessage(Message message, Balances balances)    {    	log.logSend(message);        eventBus.post(new SendMessageEvent("", convertMessage(message), convertBalances(balances)));        // Log sending of the message                // Simulate the sending of the message - here is where the real code        // to actually send the message over the network would go        //Object[] objList = new Object[]{message,balances};        //MessageFormat messageFormat = listener.ListenSendMessage(convertMessage(message), convertBalances(balances));        // Log the response gotten back        //return messageFormat;    }        public static void receiveSendMessage(MessageFormat messageFormat)    {        log.logResponse(messageFormat.getStatus());        Transaction.messageFormat = messageFormat;        //transaction.messageFormat = messageFormat;    }        // Log into which to record messages    	private BroadcastBalances convertBalances(Balances balances)     {    	BroadcastBalances broadcastBalances = new BroadcastBalances();    	broadcastBalances.setBalances(balances.getTotal(), balances.getAvailable());    	return broadcastBalances;	}	private BroadcastMessage convertMessage(Message message)     {    	return new BroadcastMessage(message.getMessageCode(), message.getCard(), message.getPIN(), message.getSerialNumber(), message.getFromAccount(), message.getToAccount(), message.getAmount());	}	private static Log log;}