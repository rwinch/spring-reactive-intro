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

package sample.message

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 * @author Rob Winch
 */
class MessageController(private val messages: MessageService) {

    fun findMessageByToUserEmail(request: ServerRequest): Mono<ServerResponse> {
        val email = request.pathVariable("email")
        val result = this.messages.findMessageByToUserEmail(email)
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, object : ParameterizedTypeReference<List<Message>>() {})
    }

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        val result = this.messages.findAll()
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, object : ParameterizedTypeReference<List<Message>>() {})
    }
}
