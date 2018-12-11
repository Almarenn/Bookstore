package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{
	private Inventory inventory;

	public InventoryService(String name) {
		super(name);
		this.inventory= Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		System.out.println(getName()+" subscribed to check availability event");
		subscribeEvent(CheckAvailabilityEvent.class, event-> {
			System.out.println(getName()+" got a checkavailabilityevent for the book: "+event.getBookName());
			int price=inventory.checkAvailabilityAndGetPrice(event.getBookName());
			System.out.println(getName()+": the book "+event.getBookName()+ " price is: "+price);
			if(price!=-1 && event.getCustomer().getAvailableCreditAmount()>=price) {
				OrderResult o = inventory.take(event.getBookName());
				if (o == OrderResult.SUCCESSFULLY_TAKEN) {
					System.out.println(getName()+" completed the order for "+event.getBookName());
					complete(event, price);
				}
			}
			else{
				System.out.println("client dont have money or book not available");
				complete(event,-1);}
			});
		subscribeBroadcast(TerminateBroadcast.class, broadcast->terminate());
	}
}


