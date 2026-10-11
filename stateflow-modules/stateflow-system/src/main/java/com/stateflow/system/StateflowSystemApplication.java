package com.stateflow.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.stateflow.common.security.annotation.EnableCustomConfig;
import com.stateflow.common.security.annotation.EnableStateflowFeignClients;

@EnableCustomConfig
@EnableStateflowFeignClients
@EnableScheduling
@SpringBootApplication
public class StateflowSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StateflowSystemApplication.class, args);
    }
}
