package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{
	private int tick;
	private HashMap<Integer, List<String>> tickOrders;
	private Customer customer;
	private List<Future<OrderReceipt>> receipts;



	public APIService(String name,Customer customer) {
		super(name);
		this.customer=customer;
		this.tickOrders= customer.getOrderSchedule();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, broadcast-> {
			this.tick=broadcast.get();
			List<String> books= this.tickOrders.get(this.tick);
			if(books!=null){
				for(String s: books){
					BookOrderEvent ev=new BookOrderEvent(s);
					Future<OrderReceipt> futureObject=sendEvent(ev);
					receipts.add(futureObject);
				}
				for(Future<OrderReceipt> f:receipts){
					OrderReceipt OrderReceipt= f.get();
					customer.addReceipt(OrderReceipt);
				}
			}
		});
	}
}
