package com.nowcoder.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommunityApplication {

	@PostConstruct
	public void init(){
		// 解决netty启动冲突问题（因为redis和elasticsearch都会使用netty，而netty默认只能启动一次服务）
		// in class "Netty4Utils ----->  setAvailableProcessors()"
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	// main
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
