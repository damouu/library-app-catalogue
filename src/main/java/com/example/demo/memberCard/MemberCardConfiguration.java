package com.example.demo.memberCard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MemberCardConfiguration {

//    @Bean
    CommandLineRunner runner(MemberCardRepository serializable) {
        return args -> {
            for (int i = 1; i < 20; i++) {
                MemberCard student_idCard = new MemberCard(UUID.randomUUID());
                serializable.save(student_idCard);
            }
        };
    }
}
