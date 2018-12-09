package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class releaseVehicleEvent implements Event {

    private DeliveryVehicle vehicleToRelease;

    public releaseVehicleEvent(DeliveryVehicle v){
        vehicleToRelease = v;
    }

    public DeliveryVehicle getVehicleToRelease(){
        return vehicleToRelease;
    }
}
