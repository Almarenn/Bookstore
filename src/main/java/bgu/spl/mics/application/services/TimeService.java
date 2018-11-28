package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private int tick;
	private Timer timer;
	private int speed;
	private int duration;

	public TimeService(int speed, int duration) {
		super("Change_This_Name");
		this.tick=1;
		timer= new Timer();
		this.speed = speed;
		this.duration= duration;
	}

	@Override
	protected void initialize() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				tick++;
				if (tick < duration) {
					sendBroadcast(new TickBroadcast(tick));
				}
				terminate();
			}
		}, speed);
	}

}
