package com.zuora.port.adapter.resources;

import com.zuora.ddd.domain.DefaultDomainEventPublisher;
import com.zuora.ddd.domain.DomainEventListener;
import com.zuora.ddd.domain.EventResult;
import com.zuora.domain.PathStatistics;
import com.zuora.domain.events.SetupAccessPathsDomainEvent;

import java.util.Optional;

/**
 * @author by zy.
 */
public class PathStatisticsEventLog implements DomainEventListener<SetupAccessPathsDomainEvent>{
    private static volatile PathStatisticsEventLog instance = null;
    private PathStatisticsEventLog(){}

    public static final PathStatisticsEventLog getInstance(){
        if (instance == null){
            synchronized (PathStatisticsEventLog.class){
                if (instance == null){
                    instance = new PathStatisticsEventLog();
                }
            }
        }
        return instance;
    }

    public static final void registerSelf(){
        DefaultDomainEventPublisher.getInstance()
                .registerEventListener(PathStatisticsEventLog.getInstance());
    }

    @Override
    public  EventResult handleEvent(SetupAccessPathsDomainEvent setupAccessPathsDomainEvent,
                                          EventResult prevResult) {
        PathStatistics pathStatistics = (PathStatistics) setupAccessPathsDomainEvent.getSource();
        System.out.println("PathStatistics has been inited and "+pathStatistics);
        return EventResult.continueResult(Optional.empty());
    }
}
