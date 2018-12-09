package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
//	private DeliveryVehicle[] vehicles;
	private ConcurrentHashMap<DeliveryVehicle,Boolean> vehicles;
//	private boolean[] vehiclesAvailable;
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
		Future<DeliveryVehicle> vehicle= new Future<>();
		boolean found=false;
		while (!found) {
			for(Map.Entry e: this.vehicles.entrySet()){
				if(e.getValue().equals(true)){
					found=true;
					this.vehicles.replace((DeliveryVehicle)e.getKey(),false);
					vehicle.resolve((DeliveryVehicle)e.getKey());
					break;
				}
			}
//			this.vehicles.
//			this.vehicles.replace(vehicle,true);
//			for(int i=0;i<this.vehiclesAvailable.length && !found; i++) {
//				if(this.vehiclesAvailable[i]==true) {
//					found=true;
//					this.vehiclesAvailable[i]=false;
//					vehicle.resolve(vehicles[i]);
//				}
//			}
		}
		return vehicle;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		this.vehicles.replace(vehicle,true);
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for( DeliveryVehicle v:vehicles){
			this.vehicles.put(v,true);
		}
//		this.vehicles= vehicles;
//		for(int i=0;i<this.vehiclesAvailable.length;i++) {
//			vehiclesAvailable[i]=true;
//		}
	}

}
