package com.zuora.domain.events;

import com.zuora.ddd.domain.DomainEvent;

/**
 * @author by zy.
 */
public class SetupAccessPathsDomainEvent extends DomainEvent{

    public SetupAccessPathsDomainEvent(Object source) {
        super(source);
    }
}
