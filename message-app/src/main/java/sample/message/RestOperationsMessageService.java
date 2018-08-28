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
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sample.user.User;

import java.net.URI;
import java.util.List;

/**
 * @author Rob Winch
 */
@Service
public class RestOperationsMessageService implements MessageService {
	private String usersBaseUri = "http://localhost:8081/";

	private String messagesBaseUri = "http://localhost:8082/";

	private final RestOperations rest = new RestTemplate();

	@Override
	public List<Message> findAll() {
		ParameterizedTypeReference<List<Message>> type = new ParameterizedTypeReference<List<Message>>(){};
		URI uri = UriComponentsBuilder.fromHttpUrl(this.messagesBaseUri)
				.path("/messages")
				.build()
				.toUri();
		ResponseEntity<List<Message>> result = this.rest.exchange(RequestEntity.get(uri).build(), type);
		return result.getBody();
	}

	@Override
	public List<Message> findMessageByToUserEmail(String email) {
		User user = findUserByEmail(email);
		return findMessagesByUserId(user.getId());
	}

	private List<Message> findMessagesByUserId(String id) {
		ParameterizedTypeReference<List<Message>> type = new ParameterizedTypeReference<List<Message>>(){};
		URI uri = UriComponentsBuilder.fromHttpUrl(this.messagesBaseUri)
				.path("/messages")
				.queryParam("to_user_id", id)
				.build()
				.toUri();
		ResponseEntity<List<Message>> result = this.rest.exchange(RequestEntity.get(uri).build(), type);
		return result.getBody();
	}

	private User findUserByEmail(String email) {
		String uri = UriComponentsBuilder.fromHttpUrl(this.usersBaseUri)
				.path("/users/")
				.queryParam("email", email)
				.build()
				.toUriString();
		ResponseEntity<User> user = this.rest.getForEntity(uri, User.class);
		return user.getBody();
	}
}
