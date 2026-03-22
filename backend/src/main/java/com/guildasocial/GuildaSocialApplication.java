package com.guildasocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GuildaSocialApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuildaSocialApplication.class, args);
    }
}
