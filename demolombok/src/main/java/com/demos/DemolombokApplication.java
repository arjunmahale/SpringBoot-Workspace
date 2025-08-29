package com.demos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sdfsdf.DemoLombok;

@SpringBootApplication
public class DemolombokApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemolombokApplication.class, args);
		DemoLombok dme=new DemoLombok();
		dme.setId(2);
		dme.setName(null);
	}

}
