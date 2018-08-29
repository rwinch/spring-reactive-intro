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

import org.springframework.context.support.beans
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.util.DefaultUriBuilderFactory
import sample.message.MessageController
import sample.message.WebClientMessageService

private val usersBaseUri = "http://localhost:8081/users"

private val messagesBaseUri = "http://localhost:8082/messages"

fun uriBuilderFactory() = DefaultUriBuilderFactory().apply {
    encodingMode = DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT
    setDefaultUriVariables(mapOf("users" to usersBaseUri,
            "messages" to messagesBaseUri))
}

fun webClient() = WebClient.builder()
        .uriBuilderFactory(uriBuilderFactory())
        .build()

fun beans() = beans {
    bean { webClient() }
    bean<WebClientMessageService>()
    bean<MessageController>()
    bean<Routes>()
    bean("webHandler") {
        RouterFunctions.toWebHandler(ref<Routes>().router(), HandlerStrategies.withDefaults());
    }
}