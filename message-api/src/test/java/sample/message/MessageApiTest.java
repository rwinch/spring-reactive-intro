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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rob Winch
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class MessageApiTest {
	@Autowired
	MockMvc mockMvc;

	@Test
	public void findOne() throws Exception {
		this.mockMvc.perform(get("/messages/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":1,\"to_user_id\":\"1\",\"text\":\"St Louis Says Hello!\"}"));
	}

	@Test
	public void findAll() throws Exception {
		this.mockMvc.perform(get("/messages/"))
				.andExpect(status().isOk())
				.andExpect(content().json(" [{\"id\":1,\"to_user_id\":\"1\",\"text\":\"St Louis Says Hello!\"},{\"id\":2,\"to_user_id\":\"1\",\"text\":\"Greetings from Spring Boot!\"},{\"id\":3,\"to_user_id\":\"1\",\"text\":\"Hi Rob!\"},{\"id\":4,\"to_user_id\":\"2\",\"text\":\"Hi Joe!\"},{\"id\":5,\"to_user_id\":\"3\",\"text\":\"Hi Josh Cummings!\"}]"));
	}

	@Test
	public void findByToUserId() throws Exception {
		this.mockMvc.perform(get("/messages?to_user_id=1"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"id\":1,\"to_user_id\":\"1\",\"text\":\"St Louis Says Hello!\"},{\"id\":2,\"to_user_id\":\"1\",\"text\":\"Greetings from Spring Boot!\"},{\"id\":3,\"to_user_id\":\"1\",\"text\":\"Hi Rob!\"}]"));
	}

	@Test
	public void findByToUserIdWhenNotFound() throws Exception {
		this.mockMvc.perform(get("/messages?to_user_id=not-found"))
				.andExpect(status().isNotFound());
	}
}
