package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailibiltyEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

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

	public SellingService(String name) {
		super(name);
		moneyRegister= MoneyRegister.getInstance();

	}

	@Override
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class,event-> {
			CheckAvailibiltyEvent ev= new CheckAvailibiltyEvent(event.getBookName());
			Future<Integer> f= (Future<Integer>)sendEvent(ev);
			int price= f.get();
			if(price!=-1){

			}

	}

		}
	}

