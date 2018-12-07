package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailibiltyEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.LinkedList;
import java.util.Queue;

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
	private Queue<Message> queue;

	public SellingService(String name) {
		super(name);
		moneyRegister= MoneyRegister.getInstance();
		queue = new LinkedList<Message>();

	}

	@Override
	protected void initialize() {
		while(queue.isEmpty()){
			try{
				this.wait();
			} catch (InterruptedException ignored){}
		}
//		for (int i =0; i < queue.size(); i++){
//			BookOrderEvent bookOrder= (BookOrderEvent)queue.poll();
//			subscribeEvent(bookOrder,message-> {
//				CheckAvailibiltyEvent ev= new CheckAvailibiltyEvent(bookOrder.getBookName());
//				Future<Integer> price= (Future<Integer>)sendEvent(ev);
//
//	}
		}
	}

