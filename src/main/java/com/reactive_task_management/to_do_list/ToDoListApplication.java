package com.reactive_task_management.to_do_list;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableReactiveMongoAuditing
@OpenAPIDefinition(info = @Info(title="To Do List Application"))
public class ToDoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoListApplication.class, args);
	}

}
