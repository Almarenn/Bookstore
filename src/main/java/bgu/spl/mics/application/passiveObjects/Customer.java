package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer {
	private int id;
	private String name;
	private String address;
	private int distance;
	private List<OrderReceipt> receipts;
	private int creditCard;
	private int availableAmount;
	private HashMap<Integer,List<String>> orderSchedule ;

	public Customer(int id, String name, String address, int distance, int creditCardNum, int amount, HashMap<Integer,List<String>> orderSchedule){
		this.id=id;
		this.name=name;
		this.address=address;
		this.distance=distance;
		receipts = new ArrayList<>();
		creditCard = creditCardNum;
		availableAmount=amount;
		this.orderSchedule = orderSchedule;
	}

	/**
	 * Retrieves the name of the customer.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the ID of the customer  .
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retrieves the address of the customer.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Retrieves the distance of the customer from the store.
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Retrieves a list of receipts for the purchases this customer has made.
	 * <p>
	 * @return A list of receipts.
	 */
	public List<OrderReceipt> getCustomerReceiptList() {
		return receipts;
	}

	/**
	 * Retrieves the amount of money left on this customers credit card.
	 * <p>
	 * @return Amount of money left.
	 */
	public int getAvailableCreditAmount() {
		return availableAmount;
	}

	/**
	 * Retrieves this customers credit card serial number.
	 */
	public int getCreditNumber() {
		return creditCard;
	}

	public synchronized void pay(int i) {
		if(i>=availableAmount) {
			availableAmount = availableAmount-i;
		}
	}

	public HashMap<Integer,List<String>>  getOrderSchedule() {
		return orderSchedule;
	}

	public void addReceipt(OrderReceipt orderReceipt){
		this.receipts.add(orderReceipt);
	}
}