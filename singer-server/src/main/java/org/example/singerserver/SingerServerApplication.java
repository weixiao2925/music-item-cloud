package org.example.singerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SingerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingerServerApplication.class, args);
    }

}
