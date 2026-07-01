package io.coco318213.stubox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class StuboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(StuboxApplication.class, args);
    }

}
