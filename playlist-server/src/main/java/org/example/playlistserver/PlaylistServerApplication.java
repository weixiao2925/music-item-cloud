package org.example.playlistserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PlaylistServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaylistServerApplication.class, args);
    }

}
