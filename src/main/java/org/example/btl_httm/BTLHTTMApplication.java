package org.example.btl_httm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BTLHTTMApplication {
    public static void main(String[] args) {
        SpringApplication.run(BTLHTTMApplication.class, args);
    }
}
