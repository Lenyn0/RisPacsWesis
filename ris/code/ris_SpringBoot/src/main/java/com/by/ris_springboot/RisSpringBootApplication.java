package com.by.ris_springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@EntityScan({"com.by.ris_springboot.settings.domain","com.by.ris_springboot.workbench.domain"})
@MapperScan({"com.by.ris_springboot.settings.dao","com.by.ris_springboot.workbench.dao"})
@ServletComponentScan
public class RisSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RisSpringBootApplication.class, args);
    }

}
