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

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import sample.user.User

/**
 * @author Rob Winch
 */
@Component
class WebClientMessageService(private val rest: WebClient) : MessageService {

    override fun findAll(): Mono<List<Message>> {
        return this.rest.get()
                .uri {u -> u.path("{messages}").build()}
                .retrieve()
                .bodyToMono()
    }

    override fun findMessageByToUserEmail(email: String): Mono<List<Message>> {
        return findUserByEmail(email)
                .map(User::id)
                .flatMap(this::findMessagesByUserId)
    }

    private fun findMessagesByUserId(id: String): Mono<List<Message>> {
        return this.rest.get()
                .uri { u -> u.path("{messages}").queryParam("to_user_id", id).build() }
                .retrieve()
                .bodyToMono()
    }

    private fun findUserByEmail(email: String): Mono<User> {
        return this.rest.get()
                .uri { u -> u.path("{users}").queryParam("email", email).build() }
                .retrieve()
                .bodyToMono()
    }
}
