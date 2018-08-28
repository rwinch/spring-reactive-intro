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

package sample.message;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sample.user.User;

import java.util.List;

/**
 * @author Rob Winch
 */
@Component
public class WebClientMessageService implements MessageService {

	private final WebClient rest;

	public WebClientMessageService(WebClient rest) {
		this.rest = rest;
	}

	@Override
	public Mono<List<Message>> findAll() {
		ParameterizedTypeReference<List<Message>> type = new ParameterizedTypeReference<List<Message>>(){};
		return this.rest.get()
				.uri(u -> u.path("{messages}").build())
				.retrieve()
				.bodyToMono(type);
	}

	public Mono<List<Message>> findMessageByToUserEmail(String email) {
		return findUserByEmail(email)
			.map(User::getId)
			.flatMap(this::findMessagesByUserId);
	}

	private Mono<List<Message>> findMessagesByUserId(String id) {
		ParameterizedTypeReference<List<Message>> type = new ParameterizedTypeReference<List<Message>>(){};
		return this.rest.get()
				.uri(u -> u.path("{messages}").queryParam("to_user_id", id).build())
				.retrieve()
				.bodyToMono(type);
	}

	private Mono<User> findUserByEmail(String email) {
		return this.rest.get()
				.uri(u -> u.path("{users}").queryParam("email", email).build())
				.retrieve()
				.bodyToMono(User.class);
	}
}
