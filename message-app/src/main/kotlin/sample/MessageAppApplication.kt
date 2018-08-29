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

import org.springframework.beans.factory.getBean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.DisposableServer
import reactor.netty.http.server.HttpServer

class MessageAppApplication {

    private val httpHandler: HttpHandler

    private val server: HttpServer

    private var disposable: DisposableServer? = null

    constructor(port: Int = 8080) {
        val context = GenericApplicationContext().apply {
            beans().initialize(this)
            refresh()
        }

        server = HttpServer.create().port(port)
        httpHandler = WebHttpHandlerBuilder
                .applicationContext(context)
                .apply { if (context.containsBean("corsFilter")) filter(context.getBean<CorsWebFilter>()) }
                .build()
    }

    fun start(): HttpServer {
        return server.handle(ReactorHttpHandlerAdapter(httpHandler))
    }

    fun startAndAwait() {
        disposable = start().bind().block()
    }

    fun stop() {
        disposable?.disposeNow()
    }
}

fun main(args: Array<String>) {
    MessageAppApplication().startAndAwait()
}
