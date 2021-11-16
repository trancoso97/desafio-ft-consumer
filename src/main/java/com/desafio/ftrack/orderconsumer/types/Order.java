package com.desafio.ftrack.orderconsumer.types;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private double total;

    @Enumerated(EnumType.STRING)
    @Setter(value = AccessLevel.NONE)
    private ProcessingStatus status = ProcessingStatus.NOT_PROCESSED;

    public enum ProcessingStatus{
        NOT_PROCESSED,
        PROCESSED
    }

    public void setStatus(ProcessingStatus status){
        if(status == ProcessingStatus.NOT_PROCESSED) return;
        if(this.status == ProcessingStatus.PROCESSED) return;
        this.status = ProcessingStatus.PROCESSED;
    }

    public Long getId() {
        return null;
    }

}