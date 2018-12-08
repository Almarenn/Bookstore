package bgu.spl.mics;

import javax.xml.ws.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
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
	private ConcurrentHashMap<Class<? extends Event>, LinkedBlockingQueue<MicroService>> eventToMicrosSrviceQueue; //events and the right MicroServices queue of all micro services that registered to this event
	private ConcurrentHashMap<Class<? extends Broadcast>,List <MicroService>> broadcastToMicroServiceList; // broadcasts and the right MicroServices list of all services that registered to this broadcast
	private ConcurrentHashMap<Event,Future> eventToFuture; // address' of events and the right Future object that was result from it
	private ConcurrentHashMap<MicroService,List <Class<? extends Event>>> microServiceToSubscribedEvents;
	private ConcurrentHashMap<MicroService,List<Class<? extends Broadcast>>> microServiceToSubscribedBroadcasts;
	//semaphore?

	public MessageBusImpl() {
		serviceToMessageQueue= new ConcurrentHashMap<>();
		eventToMicrosSrviceQueue = new ConcurrentHashMap<>();
		broadcastToMicroServiceList= new ConcurrentHashMap<>();
		eventToFuture = new ConcurrentHashMap<>();
		microServiceToSubscribedEvents = new ConcurrentHashMap<>();
		microServiceToSubscribedBroadcasts= new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}

	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		LinkedBlockingQueue<MicroService> q;
		if(!this.eventToMicrosSrviceQueue.containsKey(type)){
			q= new LinkedBlockingQueue<>();
			this.eventToMicrosSrviceQueue.put(type, q);
		}
		else
			q=this.eventToMicrosSrviceQueue.get(type);
		try{
			q.put(m);}
				catch (InterruptedException e){}
		microServiceToSubscribedEvents.get(m).add(type);
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
		microServiceToSubscribedBroadcasts.get(m).add(type);
	}

	@Override
	public synchronized  <T> void complete(Event<T> e, T result) {
		Future f=eventToFuture.get(e);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		List <MicroService>l= broadcastToMicroServiceList.get(b.getClass());
		if(l!=null && !l.isEmpty()) {
			for (MicroService ms : l) {
				LinkedBlockingQueue<Message> messageQueue = serviceToMessageQueue.get(ms);
				synchronized (messageQueue){
				try{
					messageQueue.put(b);}
				catch (InterruptedException e){}
			}}
		}

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		LinkedBlockingQueue<MicroService> microServiceQueue= eventToMicrosSrviceQueue.get(e.getClass());
		if(microServiceQueue==null|| microServiceQueue.isEmpty()){
			return null;
		}
		Future f= new Future();
		eventToFuture.put(e,f);
		synchronized (microServiceQueue){
		MicroService m=microServiceQueue.poll();
		LinkedBlockingQueue<Message> messageQueue= serviceToMessageQueue.get(m);
		try{
				messageQueue.put(e);}
			catch (InterruptedException s){}
		try{
			microServiceQueue.put(m);}
			catch (InterruptedException s){}
		}
		return f;
	}

	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> myQueue= new LinkedBlockingQueue<>();
		serviceToMessageQueue.put(m,myQueue);
		microServiceToSubscribedEvents.put(m,new LinkedList<>());
		microServiceToSubscribedBroadcasts.put(m,new LinkedList<>());
	}

	@Override
	public void unregister(MicroService m) {
		serviceToMessageQueue.remove(m);
		List<Class<? extends Event>> l1=microServiceToSubscribedEvents.get(m);
		List<Class<? extends Broadcast>> l2=microServiceToSubscribedBroadcasts.get(m);
		for(Class<? extends Event> ev:l1){
			LinkedBlockingQueue myQueue= eventToMicrosSrviceQueue.get(ev);
			synchronized (myQueue){
			myQueue.remove(m);
		}}
		for(Class<? extends Broadcast> br:l2){
			List myList= broadcastToMicroServiceList.get(br);
			myList.remove(m);
		}


	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		while (!Thread.currentThread().isInterrupted()) {
			if (!microServiceToSubscribedEvents.get(m).isEmpty()&&!microServiceToSubscribedBroadcasts.get(m).isEmpty()) {
				throw new IllegalStateException();
			}
			else{
				LinkedBlockingQueue<Message> messageQueue = serviceToMessageQueue.get(m);
				if(messageQueue!=null){
				return messageQueue.take();}}
		}
		throw new InterruptedException();
	}


}
