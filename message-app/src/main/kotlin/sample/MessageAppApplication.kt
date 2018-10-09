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

import org.springframework.boot.kofu.application
import org.springframework.boot.kofu.web.server
import org.springframework.context.support.beans
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.router
import org.springframework.web.util.DefaultUriBuilderFactory
import reactor.netty.http.client.HttpClient
import sample.message.MessageController
import sample.message.WebClientMessageService

const val usersBaseUri = "http://localhost:8081/users"
const val messagesBaseUri = "http://localhost:8082/messages"

fun routes(messageController: MessageController) = router {
	(accept(APPLICATION_JSON) and "/messages").nest {
		GET("/{email}", messageController::findMessageByToUserEmail)
		GET("/", messageController::findAll)
	}
}

val beans = beans {
	bean<MessageController>()
	bean<WebClientMessageService>()
	bean {
		val uriBuilderFactory = DefaultUriBuilderFactory()
		uriBuilderFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT
		uriBuilderFactory.setDefaultUriVariables(mapOf("users" to usersBaseUri, "messages" to messagesBaseUri))
		val client = HttpClient.create().tcpConfiguration { it.noSSL() }
		WebClient.builder()
				.clientConnector(ReactorClientHttpConnector(client))
				.uriBuilderFactory(uriBuilderFactory)
				.build()
	}
}

val app = application {
	import(beans)
	server {
		import(::routes)
	}

}

fun main(args: Array<String>) {
	app.run(args)
}
