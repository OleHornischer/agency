package com.onit.service_calls

import com.onit.agent.Agent
import com.onit.configuration.Configuration.getBaseUrl
import java.util.function.Function
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.client.SyncInvoker
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

abstract class RestServiceCall {

    private val headers = HashMap<String, String>()
    private val queryParams = HashMap<String, String>()
    private lateinit var url: String
    lateinit var entityAsString: String
    lateinit var response: Response
    var followRedirects = false

    fun url(url:String) : RestServiceCall {
        this.url=url
        return this
    }

    fun withHeader(key: String, value: String): RestServiceCall {
        headers.put(key, value)
        return this
    }

    fun withQueryParam(name: String, value: String): RestServiceCall {
        queryParams.put(name, value)
        return this
    }

    fun delete() = query(SyncInvoker::delete)

    fun get() = query(SyncInvoker::get)

    fun patch(entity: Any?): Int {
        val lambda: (Invocation.Builder) -> Response = { request: Invocation.Builder ->
            request.method("PATCH", Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE))
        }
        return query(lambda)
    }

    fun post(entity: Any?): Int {
        val lambda: (Invocation.Builder) -> Response = { request: Invocation.Builder ->
            request.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE))
        }
        return query(lambda)
    }

    fun put(entity: Any?): Int {
        val lambda: (Invocation.Builder) -> Response = { request: Invocation.Builder ->
            request.put(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE))
        }
        return query(lambda)
    }

    private fun query(method: Function<Invocation.Builder, Response>): Int {
        val client = ClientBuilder.newClient()
        var target = client.target(if (url.contains("://")) url else getBaseUrl() + url)
        for (queryParam in queryParams) {
            target = target.queryParam(queryParam.key, queryParam.value)
        }
        var request = target.request()
        request = applyHeaders(request)

        var response: Response? = null
        try {
            response = method.apply(request)
            while (followRedirects
                && Response.Status.TEMPORARY_REDIRECT.family == Response.Status.Family.familyOf(response!!.status)
            ) {
                val redirectClient = ClientBuilder.newClient()
                val redirectTarget = redirectClient.target(response.getHeaderString(HttpHeaders.LOCATION))
                val redirectRequest = applyHeaders(redirectTarget.request())
                response = method.apply(redirectRequest)
            }
            entityAsString = response!!.readEntity(object : GenericType<String>() {})
            this.response = response
            return response.status
        } finally {
            response?.close()
            client.close()
        }
    }

    private fun applyHeaders(request: Invocation.Builder): Invocation.Builder {
        var request1 = request
        for (header in headers) {
            request1 = request1.header(header.key, header.value)
        }
        return request1
    }

    abstract fun execute(agent: Agent)

    abstract fun requiredSessionValues(): List<String>

    abstract fun getId(): String

    abstract fun nextPossibleCalls(): List<String>

    open fun description() = this::class.simpleName!!
}