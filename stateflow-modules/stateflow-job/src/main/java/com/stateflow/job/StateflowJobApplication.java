package com.stateflow.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.stateflow.common.security.annotation.EnableCustomConfig;
import com.stateflow.common.security.annotation.EnableStateflowFeignClients;

/**
 * 定时任务
 * 
 * @author stateflow
 */
@EnableCustomConfig
@EnableStateflowFeignClients   
@SpringBootApplication
public class StateflowJobApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StateflowJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  定时任务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
