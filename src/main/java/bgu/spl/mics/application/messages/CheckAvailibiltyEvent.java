package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class CheckAvailibiltyEvent implements Event {
    private String bookName;

    public CheckAvailibiltyEvent(String bookName){
        this.bookName=bookName;
    }
}
