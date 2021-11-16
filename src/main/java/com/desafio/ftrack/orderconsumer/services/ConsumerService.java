package com.desafio.ftrack.orderconsumer.services;

import java.util.Optional;

import com.desafio.ftrack.orderconsumer.repositories.OrderRepository;
import com.desafio.ftrack.orderconsumer.types.Order;
import com.desafio.ftrack.orderconsumer.types.Order.ProcessingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    OrderRepository orderRepo;
    
    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.groupid}", containerFactory = "orderListener")
    public void doListen(Order order){
        log.info("KAFKA - CONSUME MSG: {}",this.toJson(order));
        if(order == null){
            log.error("INVALID MESSAGE");
            return;
        }
        Optional<Order> opt = orderRepo.findById(order.getId());
        if(opt.isEmpty()){
            log.error("ORDER NOT FOUND");
            return;
        }
        order = opt.get();
        order.setStatus(ProcessingStatus.PROCESSED);
        orderRepo.save(order);
        log.info("ORDER ID {} PROCESSED", order.getId());
    }
    
    private String toJson(Order order) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(order);
		} catch (Exception e) {
			log.error("EXCEPTION CONVERTING TO JSON", e);
		}
		return null;
	}

    /*private Order fromJson(String s) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(s,Order.class);
		} catch (Exception e) {
            log.error("EXCEPTION CONVERTING FROM JSON", e);
		}
		return null;
	}*/

}
