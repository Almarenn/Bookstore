package Java;
import static org.junit.Assert.*;

import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBusImpl;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.mics.application.services.SellingService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
public class MessageBusImplTest {

    private MessageBusImpl bus= new MessageBusImpl();


    ExampleEvent event = new ExampleEvent("sender");
    ExampleBroadcast broadcast = new ExampleBroadcast("sender");
    SellingService seller = new SellingService();
    
    @Test
    public void subscribeEvent() {
        bus.subscribeEvent(event, seller);
    }
    @Test
    public void subscribeBroadcast() {
        bus.subscribeEvent(broadcast, seller);
    }
    @Test
    public void complete() {
        String result="yes";
        Future<String> f= seller.sendEvent(event);
        bus.complete(event, result);
        assertEquals(f.get(),result);
        assertTrue(f.isDone());
    }
    @Test
    public void sendBroadcast() {

        // TODO Auto-generated method stub
    }

    @Test
    public <T> Future<T> sendEvent() {
        // TODO Auto-generated method stub
        return null;
    }
    @Test
    public void register() {
        // TODO Auto-generated method stub
    }
    @Test
    public void unregister() {
        // TODO Auto-generated method stub
    }
    @Test
    public Message awaitMessage() throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

}