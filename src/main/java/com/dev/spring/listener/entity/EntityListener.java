package com.dev.spring.listener.entity;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class EntityListener {

    @EventListener(condition = "#p0.getAccessType() == 'READ'")
    @Order(10)
    public void acceptEntity(EntityEvent entityEvent){
        System.out.println("Entity: " + entityEvent);
    }
}
