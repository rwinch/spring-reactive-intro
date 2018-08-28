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

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * @author Rob Winch
 */
@Component
public class MessageDataIntializer implements SmartInitializingSingleton {
	private final MessageRepository messages;

	public MessageDataIntializer(MessageRepository messages) {
		this.messages = messages;
	}

	@Override
	public void afterSingletonsInstantiated() {
		this.messages.save(new Message(10L, "1", "St Louis Says Hello!"));
		this.messages.save(new Message(12L, "1", "Greetings from Spring Boot!"));
		this.messages.save(new Message(13L, "1", "Hi Rob!"));

		this.messages.save(new Message(20L, "2", "Hi Joe!"));

		this.messages.save(new Message(30L, "3", "Hi Josh Cummings!"));
	}
}
