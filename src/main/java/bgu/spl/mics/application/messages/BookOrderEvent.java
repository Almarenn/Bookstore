package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class BookOrderEvent implements Event {
    private String bookName;

    public BookOrderEvent(String name) {
        this.bookName = name;
    }

    public String getBookName(){
        return this.bookName;
    }
}

