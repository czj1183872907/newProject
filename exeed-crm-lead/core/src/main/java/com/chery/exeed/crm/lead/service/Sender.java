//package com.chery.exeed.crm.lead.service;
//
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * Author:xiaowei.zhu
// * 2019/1/25 9:48
// */
//@Component
//public class Sender {
//    @Autowired
//    private AmqpTemplate rabbitTemplate;
//    public void send(String topic,String context) {
//        this.rabbitTemplate.convertAndSend("hello", context);
//    }
//}
