package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}

/**
 * 요구 사항 정의
 * - 선착순 100명에게만 할인쿠폰 지급
 * - 101개 이상이 지급되면 안된다.
 * - 순간적으로 몰리는 트래픽을 버틸 수 있어야 한다.
 */