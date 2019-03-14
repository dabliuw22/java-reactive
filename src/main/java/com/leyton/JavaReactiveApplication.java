
package com.leyton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.leyton.service.inter.ReactiveService;

@EnableScheduling
@SpringBootApplication
public class JavaReactiveApplication implements CommandLineRunner {

    @Autowired
    private ReactiveService couldReactiveService;

    @Autowired
    @Qualifier(
            value = "hotReactiveService")
    private ReactiveService hotReactiveService;

    public static void main(String[] args) {
        SpringApplication.run(JavaReactiveApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        hotReactiveService.process(Collections.emptyList());
        for (int i = 0; i <= 5; i++) {
            int size = new Random().nextInt(10) + 1;
            List<String> stream = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                stream.add("S" + i + "D" + j);
            }
            couldReactiveService.process(stream);
        }

    }
}
