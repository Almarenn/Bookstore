package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.*;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	private HashMap<Integer,String> tickOrders;
	private Customer customer;



	public APIService(String name,Customer customer) {
		super(name);
		this.customer=customer;
		this.tickOrders= customer.getOrderSchedule();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, broadcast-> {
			this.tick=broadcast.get();
			String bookName=findTick(tick);
			if(bookName!=null){
				BookOrderEvent ev=new BookOrderEvent(bookName);
				Future<OrderReceipt> futureObject=sendEvent(ev);
				OrderReceipt OrderReceipt= futureObject.get();
				customer.add(OrderReceipt);
			}
		});

		
	}
	//gets a certain tick as a paramter and returns the book name that should be ordered in that tick, if there is one like this
	private String findTick(int tick){

		Set set = tickOrders.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			if ((int)mentry.getKey()==tick){
				return (String)mentry.getValue();
			}
		}
		return null;
	}
}
