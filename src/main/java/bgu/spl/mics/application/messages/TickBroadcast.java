package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

public class TickBroadcast implements Broadcast {

    private int tick;

    public TickBroadcast(int tick){
        this.tick=tick;
    }

    public int get(){
        return this.tick;
    }


}
