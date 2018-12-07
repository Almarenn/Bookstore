package bgu.spl.mics;

import javax.xml.ws.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	private ConcurrentHashMap <Class <? extends Service>, Queue<Message>> serviceToMessageQueue; // services and the right MessageQueue that belongs to the service
	private ConcurrentHashMap<Class<? extends Event>, Queue<MicroService>> eventToMicrosSrviceQueue; //events and the right MicroServices queue of all micro services that registered to this event
	private ConcurrentHashMap<Class<? extends Broadcast>,List <MicroService>> broadcastToMicroServiceList; // broadcasts and the right MicroServices list of all services that registered to this broadcast
	private ConcurrentHashMap<Integer,Future> eventToFuture; // address' of events and the right Future object that was result from it
	//semaphore?

	public MessageBusImpl() {

	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}

	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		Queue<MicroService> q;
		if(!this.eventToMicrosSrviceQueue.containsKey(type)){
			q= new LinkedBlockingDeque<>();
		}
		else
			q=this.eventToMicrosSrviceQueue.get(type);
		this.eventToMicrosSrviceQueue.put(type, q);
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		List<MicroService> l;
		if(!this.broadcastToMicroServiceList.containsKey(type)){
			l= new LinkedList<>();
		}
		else
			l=this.broadcastToMicroServiceList.get(type);
		this.broadcastToMicroServiceList.put(type, l);

	}

	@Override
	public synchronized  <T> void complete(Event<T> e, T result) {
		Integer address= e.hashCode();
		Future f=eventToFuture.get(address);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {


	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future f= new Future();
		Integer address= e.hashCode();
		eventToFuture.put(address,f);
		Queue queue= eventToMicrosSrviceQueue.get(e);
		if(queue.isEmpty()){
			return null;
		}
		queue.add(e);
		return f;
	}

	@Override
	public void register(MicroService m) {


	}

	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
