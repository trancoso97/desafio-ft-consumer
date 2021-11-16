package com.desafio.ftrack.orderconsumer.repositories;

import com.desafio.ftrack.orderconsumer.types.Order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {

    
}