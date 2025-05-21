package com.test.accesa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AccesaApplication {

    public static void main(String[] args) {

        SpringApplication.run(AccesaApplication.class, args);

    }

}
