package bgu.spl.mics.application.services;

import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailibiltyEvent;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

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
		subscribeEvent(CheckAvailibiltyEvent.class,event-> {
			int price=(int)inventory.checkAvailabilityAndGetPrice(event.getBookName());
			synchronized (event.getCustomer()){
			if(price!=-1 && event.getCustomer().getAvailableCreditAmount()<=price){
				OrderResult o= inventory.take(event.getBookName());
				if(o==OrderResult.SUCCESSFULLY_TAKEN){
					complete(event,price);
				}
			else
				complete(event,-1);
				}
			}});
		subscribeBroadcast(TerminateBroadcast.class, broadcast->terminate());
	}
}


