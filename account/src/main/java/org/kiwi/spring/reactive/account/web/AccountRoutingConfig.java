package org.kiwi.spring.reactive.account.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AccountRoutingConfig {

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Bean
	public RouterFunction<ServerResponse> accountRoutes(AccountHandler accountHandler) {
		return route().path(contextPath, builder -> builder
			.GET("/accounts", accept(MediaType.APPLICATION_JSON), accountHandler::findAll)
			.GET("/accounts/{accountNo}", accept(MediaType.APPLICATION_JSON), accountHandler::findOne)
			.POST("/accounts", accept(MediaType.APPLICATION_JSON), accountHandler::save)
		).build();
	}
}
