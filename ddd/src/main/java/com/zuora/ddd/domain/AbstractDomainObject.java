package com.zuora.ddd.domain;

import java.io.Serializable;
import java.util.Optional;


/**
 * @author by zy.
 */
public abstract class AbstractDomainObject implements Serializable{
    private static final long serialVersionUID = 1L;


    protected <R> Optional<R> publishEvent(DomainEvent domainEvent){
        return DefaultDomainEventPublisher
                .getInstance()
                .publishEvent(domainEvent);
    }
}