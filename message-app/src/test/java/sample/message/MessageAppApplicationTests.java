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

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class MessageAppApplicationTests {
	MockWebServer userApi = new MockWebServer();

	MockWebServer messageApi = new MockWebServer();

	@Autowired
	MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.userApi.start(8081);
		this.messageApi.start(8082);
	}

	@After
	public void cleanup() throws Exception {
		this.userApi.shutdown();
		this.messageApi.shutdown();
	}

	@Test
	@WithMockUser
	public void messageWhenFindAllThenOk() throws Exception {
		setupMessageFound();
		this.mockMvc.perform(get("/messages"))
				.andExpect(status().isOk());
		assertThat(this.messageApi.takeRequest().getPath()).isEqualTo("/messages");
	}

	@Test
	@WithMockUser
	public void messageWhenFindMessageByToUserEmailThenOk() throws Exception {
		setupMessageFound();
		this.mockMvc.perform(messagesToRob())
				.andExpect(status().isOk());
		assertThat(this.messageApi.takeRequest().getPath()).isEqualTo("/messages?to_user_id=1");
	}

	@Test
	@WithMockUser(roles = "ACTUATOR")
	public void actuatorWhenAuthorizedThenOk() throws Exception {
		this.mockMvc.perform(get("/actuator/env"))
				.andExpect(status().isOk());
	}

	@Test
	public void messageWhenNotAuthenticatedThenUnauthorized() throws Exception {
		this.mockMvc.perform(messagesToRob())
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void messageWhenHttpBasicValidThenOk() throws Exception {
		setupMessageFound();
		this.mockMvc.perform(messagesToRob().with(httpBasic("user", "password")))
				.andExpect(status().isOk());
	}

	private MockHttpServletRequestBuilder messagesToRob() {
		return get("/messages/{email}", "rwinch@example.com");
	}

	private void setupMessageFound() {
		this.userApi.enqueue(jsonBody("{\"id\":\"1\",\"email\":\"rwinch@example.com\",\"name\":\"Rob Winch\"}"));
		this.messageApi.enqueue(jsonBody("[{\"id\":1,\"to_user_id\":\"1\",\"text\":\"Hello St Louis!\"},{\"id\":2,\"to_user_id\":\"1\",\"text\":\"Greetings Spring Boot!\"}]"));
	}

	private MockResponse jsonBody(String body) {
		return new MockResponse()
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.setBody(body);
	}
}
