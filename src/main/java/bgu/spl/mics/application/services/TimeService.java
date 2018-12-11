package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import java.util.Timer;
import java.util.TimerTask;

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

	public TimeService(String name, int speed, int duration) {
		super(name);
		this.tick=1;
		timer= new Timer();
		this.speed = speed;
		this.duration= duration;
	}

	@Override
	protected void initialize() {
		terminate();
		TimerTask t = new TimerTask() {
			@Override
			public void run() {
				if(tick<duration){
				System.out.println("Tick"+tick);
				sendBroadcast(new TickBroadcast(tick));
				tick++;
			}
				else {
					timer.cancel();
					timer.purge();
					sendBroadcast(new TerminateBroadcast());
					terminate();
				}
		}};
		timer.scheduleAtFixedRate(t,0,speed);
		terminate();
	}
}
