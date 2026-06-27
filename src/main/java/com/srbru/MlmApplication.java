package com.srbru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class MlmApplication
{

	public static void main(String[] args)
	{
		log.info("Starting MLM Application...");
		
		SpringApplication.run(MlmApplication.class, args);
	}

}
