package agh.iosr.event.api;

import agh.iosr.event.model.EventMessage;

public interface EventReceiver {

    EventMessage receiveEvent();
}
