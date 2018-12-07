package bgu.spl.mics;

import javax.xml.ws.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	private ConcurrentHashMap <MicroService, LinkedBlockingQueue<Message>> serviceToMessageQueue; // services and the right MessageQueue that belongs to the service
	private ConcurrentHashMap<Class<? extends Event>, Queue<MicroService>> eventToMicrosSrviceQueue; //events and the right MicroServices queue of all micro services that registered to this event
	private ConcurrentHashMap<Class<? extends Broadcast>,List <MicroService>> broadcastToMicroServiceList; // broadcasts and the right MicroServices list of all services that registered to this broadcast
	private ConcurrentHashMap<Event,Future> eventToFuture; // address' of events and the right Future object that was result from it
	private LinkedList<MicroService>subscribedServices;
	//semaphore?

	public MessageBusImpl() {
		serviceToMessageQueue= new ConcurrentHashMap<>();
		eventToMicrosSrviceQueue = new ConcurrentHashMap<>();
		broadcastToMicroServiceList= new ConcurrentHashMap<>();
		eventToFuture = new ConcurrentHashMap<>();
		subscribedServices= new LinkedList<>();
	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}

	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		Queue<MicroService> q;
		if(!this.eventToMicrosSrviceQueue.containsKey(type)){
			q= new LinkedBlockingQueue<>();
			this.eventToMicrosSrviceQueue.put(type, q);
		}
		else
			q=this.eventToMicrosSrviceQueue.get(type);
		q.add(m);
		if (!subscribedServices.contains(m)){
			subscribedServices.add(m);
		}
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		List<MicroService> l;
		if(!this.broadcastToMicroServiceList.containsKey(type)){
			l= new LinkedList<>();
			this.broadcastToMicroServiceList.put(type,l);
		}
		else
			l=this.broadcastToMicroServiceList.get(type);
		l.add(m);
		if (!subscribedServices.contains(m)){
			subscribedServices.add(m);
		}

	}

	@Override
	public synchronized  <T> void complete(Event<T> e, T result) {
		Future f=eventToFuture.get(e);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		List <MicroService>l= broadcastToMicroServiceList.get(b);
		if(l!=null && !l.isEmpty()) {
			for (MicroService ms : l) {
				Queue<Message> messageQueue = serviceToMessageQueue.get(ms);
				messageQueue.add(b);
			}
		}

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<MicroService> microServiceQueue= eventToMicrosSrviceQueue.get(e);
		if(microServiceQueue==null|| microServiceQueue.isEmpty()){
			return null;
		}
		Future f= new Future();
		eventToFuture.put(e,f);
		MicroService m=microServiceQueue.poll();
		Queue<Message> messageQueue= serviceToMessageQueue.get(m);
		messageQueue.add(e);
		microServiceQueue.add(m);
		return f;
	}

	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> myQueue= new LinkedBlockingQueue<>();
		serviceToMessageQueue.put(m,myQueue);
	}

	@Override
	public void unregister(MicroService m) {
		serviceToMessageQueue.remove(m);

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!subscribedServices.contains(m)) {
			throw new IllegalStateException();
		}
		LinkedBlockingQueue<Message> messageQueue= serviceToMessageQueue.get(m);
		return messageQueue.take();
	}
	

}
