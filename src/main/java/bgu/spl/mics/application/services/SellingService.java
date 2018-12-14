package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.Messages.TickBroadcast;

import java.util.concurrent.CountDownLatch;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{
	private MoneyRegister moneyRegister;
	private int recipetNumber=0;
	private int tick=1;
	private CountDownLatch d;

	public SellingService(String name, CountDownLatch d) {
		super(name);
		moneyRegister= MoneyRegister.getInstance();
		this.d=d;
	}

	@Override
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class,event-> {
			System.out.println(getName()+" got a bookorderevent for: "+event.getBookName());
			int currTick= this.tick;
			Customer c= event.getCustomer();
			try {
				c.getAvailable().acquire();
			}
			catch (InterruptedException e){}
			CheckAvailabilityEvent ev= new CheckAvailabilityEvent(event.getBookName(), c);
			Future<Integer> f= (Future<Integer>)sendEvent(ev);
			System.out.println(getName()+" sent a checkavailabilityevent for: "+event.getBookName());
			Integer price = f.get();
			System.out.println(getName()+" got the book price: "+price);
				if (price != -1 && price!=null) {
					moneyRegister.chargeCreditCard(c, price);
					c.getAvailable().release();
					recipetNumber = recipetNumber++;
					OrderReceipt o = new OrderReceipt(this.recipetNumber, this.getName(), c.getId(), event.getBookName(), price, event.getOrderTick(), this.tick, currTick);
					moneyRegister.file(o);
					this.recipetNumber++;
					complete(event, o);
					DeliveryEvent d = new DeliveryEvent(c.getAddress(), c.getDistance());
					sendEvent(d);
				}
				else{
					System.out.println(getName()+" could not order the book: "+event.getBookName());
					complete(event,null);
					System.out.println(getName()+" completed the event with null");
				}
				c.getAvailable().release();
	});
		subscribeBroadcast(TickBroadcast.class,broadcast->{
			this.tick=broadcast.get();
		});
		subscribeBroadcast(TerminateBroadcast.class, broadcast->terminate());
		d.countDown();
	}
}

