package com.cryptocurrency.rates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RatesApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(RatesApplication.class, args);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
