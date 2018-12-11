package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import java.util.*;

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
	private int tick=1;
	private HashMap<Integer, List<String>> tickOrders;
	private Customer customer;
	private List<Future<OrderReceipt>> receipts= new LinkedList<>();

	public APIService(String name,Customer customer) {
		super(name);
		this.customer=customer;
		tickOrders= new HashMap<>();
		for(OrderSchedule o:customer.getOrderSchedule()) {
			if(!this.tickOrders.containsKey(o.tick)){
				this.tickOrders.put(o.tick,new LinkedList<>());
				this.tickOrders.get(o.tick).add(o.bookTitle);
			}
			else{
				this.tickOrders.get(o.tick).add(o.bookTitle);
			}
		}
	}

	@Override
	protected void initialize() {
		System.out.println(getName()+" tickorders: "+tickOrders);
		System.out.println(getName()+" subscribed to tickbroadcast");
		subscribeBroadcast(TickBroadcast.class, broadcast-> {
			this.tick=broadcast.get();
			System.out.println(getName()+" got tickbroadcast: "+this.tick);
			List<String> books= this.tickOrders.get(this.tick);
			System.out.println(getName()+" books to order in "+tick+": "+books);
			if(books!=null) {
				for (String s : books) {
					BookOrderEvent ev = new BookOrderEvent(s, customer, this.tick);
					System.out.println(getName() + " sent bookorderevent: " + s);
					Future<OrderReceipt> futureObject = sendEvent(ev);
					System.out.println(getName()+" created future receipt for: "+s);
					receipts.add(futureObject);
					System.out.println(getName()+" added future receipt for: "+s);

				}
				System.out.println("amount of waiting receipts: "+receipts.size());
				for(Future<OrderReceipt> f : receipts) {
					System.out.println(getName() + " waiting for receipt");
					OrderReceipt orderReceipt = f.get();
					System.out.println(getName() + " got the receipt!!");
					if (orderReceipt != null) {
						customer.addReceipt(orderReceipt);}
				}
			}
		});
		subscribeBroadcast(TerminateBroadcast.class, broadcast->terminate());
	}
}
