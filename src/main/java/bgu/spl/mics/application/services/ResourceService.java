package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.releaseVehicleEvent;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService {
	
	private ResourcesHolder r;

	public ResourceService(String name) {
		super(name);
		this.r= ResourcesHolder.getInstance();
	}
	
	//signing for events and broadcasts
	@Override
	protected void initialize() {
//		subscribeEvent(findVehicleEvent.class, event1->{
//			Future<DeliveryVehicle> v = r.acquireVehicle();
//		} );
//		subscribeEvent(releaseVehicleEvent.class, event->{
//
//
//
//		} );
	}

	
	
	
	

}
