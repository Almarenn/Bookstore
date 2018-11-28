package Java;

import bgu.spl.mics.Future;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FutureTest {
    private Future<String> f;
    @Before
    public void setup(){
        Future<String> f= new Future<>();
    }

    @Test
    public void get() throws Exception {
        f.resolve("string");
        assertEquals("get function didn't return the result object",f.get(),"string");
    }

    @Test
    public void resolve() throws Exception {
        f.resolve("string");
        assertTrue("accepted filed wasn't changed",f.isDone());
        assertEquals("get function didn't return the result object",f.get(),"string");
    }

    @Test
    public void isDone() throws Exception {
        f.resolve("string");
        assertTrue("accepted filed wasn't changed",f.isDone());
    }

    @Test
    public void get1() throws Exception {
        f.resolve("string");
        assertEquals("get function didn't return the result object",f.get(),"string");
    }

}