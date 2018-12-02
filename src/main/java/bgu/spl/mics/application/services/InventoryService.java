package bgu.spl.mics.application.services;

import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;

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

	private Queue<Message> queue;
	private Inventory inventory;

	public InventoryService(String name) {
		super(name);
		this.inventory= Inventory.getInstance();
		queue = new LinkedList<Message>();
	}

	@Override
	protected void initialize() {
		while(queue.isEmpty()){
			try{
				this.wait();
			} catch (InterruptedException ignored){}
		}
		for (int i =0; i < queue.size(); i++){
			subscribeEvent(queue.poll(),message-> {
				if(inventory.checkAvailabiltyAndGetPrice(queue.poll().getbook())!=-1){
					return true;
				}
				else
					return false;

				}
			});
		}

		
	}

}
