package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Customer;

public class BookOrderEvent implements Event {
    private String book;
    private Customer customer;
    private int orderTick;

    public BookOrderEvent(String b, Customer c, int orderTick) {
        this.book = b;
        this.customer= c;
        this.orderTick=orderTick;
    }

    public String getBookName(){
        return this.book;
    }

    public Customer getCustomer(){return this.customer;}

    public int getOrderTick(){return this.orderTick;}
}


