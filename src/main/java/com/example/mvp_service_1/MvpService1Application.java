package com.example.mvp_service_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MvpService1Application {

	public static void main(String[] args) {
		SpringApplication.run(MvpService1Application.class, args);
	}

}
