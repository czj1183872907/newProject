package com.chery.exeed.crm.lead.entry;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan({"com.chery.exeed.crm.lead.service","com.chery.exeed.crm.common.service"
        ,"com.chery.exeed.crm.lead.mapper"
        ,"com.chery.exeed.crm.lead.configuration"
        ,"com.chery.exeed.crm.common.configuration"})
@EnableAutoConfiguration
@SpringBootApplication
@EnableServiceComb
@EnableCaching
@EnableSwagger2
public class StartLeadApplication {
    public static void main(String[] args){
        SpringApplication.run(StartLeadApplication.class, args);
    }
}
