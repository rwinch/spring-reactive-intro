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

package sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.springframework.web.util.DefaultUriBuilderFactory
import sample.message.MessageController

@SpringBootApplication
class MessageAppApplication {
    private val usersBaseUri = "http://localhost:8081/users"

    private val messagesBaseUri = "http://localhost:8082/messages"

	@Bean
	fun webClient() : WebClient {
        val uriBuilderFactory = DefaultUriBuilderFactory()
        uriBuilderFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT
        uriBuilderFactory.setDefaultUriVariables(mapOf("users" to this.usersBaseUri,
                "messages" to messagesBaseUri))
		return WebClient.builder()
                .uriBuilderFactory(uriBuilderFactory)
				.build()
	}
    @Bean
    fun route(messageController: MessageController): RouterFunction<ServerResponse> {
        return router {
            (accept(APPLICATION_JSON) and "/messages").nest {
                GET("/{email}", messageController::findMessageByToUserEmail)
                GET("/", messageController::findAll)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MessageAppApplication>(*args)
}
