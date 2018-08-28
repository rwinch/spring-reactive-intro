/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import sample.message.MessageController;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class MessageAppApplication {
	private String usersBaseUri = "http://localhost:8081/users";

	private String messagesBaseUri = "http://localhost:8082/messages";

	@Bean
	WebClient webClient() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("users", this.usersBaseUri);
		variables.put("messages", this.messagesBaseUri);
		DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
		uriBuilderFactory.setDefaultUriVariables(variables);
		uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
		return WebClient.builder()
				.uriBuilderFactory(uriBuilderFactory)
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> monoRouterFunction(MessageController messageController) {
		return route(GET("/messages"), messageController::findAll)
				.andRoute(GET("/messages/{email}"), messageController::findMessageByToUserEmail);
	}

	public static void main(String[] args) {
		SpringApplication.run(MessageAppApplication.class, args);
	}
}
