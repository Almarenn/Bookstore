package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class CheckAvailibiltyEvent implements Event {
    private String bookName;
    private Customer c;

    public CheckAvailibiltyEvent(String bookName, Customer c){
        this.bookName=bookName;
        this.c=c;
    }

    public String getBookName(){
        return this.bookName;
    }

    public Customer getCustomer() {
        return c;
    }
}

