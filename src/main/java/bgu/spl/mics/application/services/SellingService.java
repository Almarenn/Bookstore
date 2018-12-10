package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailibiltyEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.Messages.TickBroadcast;

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
	int recipetNumber=0;
	int tick=1;

	public SellingService(String name) {
		super(name);
		moneyRegister= MoneyRegister.getInstance();

	}

	@Override
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class,event-> {
			int currTick= this.tick;
			Customer c= event.getCustomer();
			try {
				c.getAvailable().acquire();
			}
			catch (InterruptedException e){}
			CheckAvailibiltyEvent ev= new CheckAvailibiltyEvent(event.getBookName(), c);
			Future<Integer> f= (Future<Integer>)sendEvent(ev);
			int price= f.get();
			if(price!=-1){
				moneyRegister.chargeCreditCard(c,price);
				c.getAvailable().release();
				recipetNumber=recipetNumber++;
				OrderReceipt o= new OrderReceipt(this.recipetNumber,this.getName(),c.getId(),event.getBookName(),price,event.getOrderTick(),this.tick,currTick);
				moneyRegister.file(o);
				this.recipetNumber++;
				complete(event,o);
				DeliveryEvent d= new DeliveryEvent(c.getAddress(),c.getDistance());
				sendEvent(d);
			}
	});
		subscribeBroadcast(TickBroadcast.class,broadcast->{
			this.tick=broadcast.get();
		});
		}
	}

