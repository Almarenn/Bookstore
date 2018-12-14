package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {

	private static class ResourcesHolderHolder {
		private static ResourcesHolder instance = new ResourcesHolder();
	}
//	private LinkedBlockingQueue <Future> futures= new LinkedBlockingQueue<>();
	private LinkedBlockingQueue <DeliveryVehicle> vehicles= new LinkedBlockingQueue<>();
//	private ConcurrentHashMap<DeliveryVehicle,Boolean> vehicles = new ConcurrentHashMap<>();

	/**
     * Retrieves the single instance of this class.
     */

	public static ResourcesHolder getInstance() {
		return ResourcesHolderHolder.instance;
	}

		/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> vehicle = new Future<>();
		DeliveryVehicle d= vehicles.poll();
//		if(d==null){
//			try {
//				futures.put(vehicle);
//			}
//			catch (InterruptedException e){}
//		}
		vehicle.resolve(d);

//		boolean found = false;
//		while (!found) {
//			for (Map.Entry e : this.vehicles.entrySet()) {
//				if (e.getValue().equals(true)) {
//					found = true;
//					this.vehicles.replace((DeliveryVehicle) e.getKey(), false);
//					vehicle.resolve((DeliveryVehicle) e.getKey());
//					break;
//				}
//			}
//		}
	return vehicle;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		System.out.println("Released vehicle!");
		try {
			vehicles.put(vehicle);
			}
		catch (InterruptedException e){}
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for (DeliveryVehicle v : vehicles) {
			try {
				this.vehicles.put(v);
			}
			catch(InterruptedException e){}
		}
	}
}
