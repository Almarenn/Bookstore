package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class BookOrderEvent implements Event{
    private Customer customer;
    private String bookName;

    public BookOrderEvent(String name, Customer c){
        this.bookName=name;
        this.customer=c;
    }


    public Customer getCustomer(){
        return this.customer;
    }


}
