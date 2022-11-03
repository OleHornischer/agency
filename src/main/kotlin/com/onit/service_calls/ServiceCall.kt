package com.onit.service_calls

import com.onit.agent.Agent
import com.onit.configuration.Configuration
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class ServiceCall {
    companion object {

        val registeredServiceCalls = HashMap<String, Class<out ServiceCall>>()

        fun init() {
            val serviceCalls =
                Reflections(Configuration.getServiceCallPackages()).getSubTypesOf(ServiceCall::class.java)
            serviceCalls
                .forEach {
                    val instance = it.getConstructor().newInstance()
                    if (null != instance)
                        registeredServiceCalls.put(instance.getId(), it)
                }
            registeredServiceCalls.put(Done.ID, Done().javaClass)
        }

        fun getForId(id: String): Class<out ServiceCall>? {
            if (registeredServiceCalls.isEmpty()) init()
            return registeredServiceCalls.get(id)
        }

        fun instanceForId(id: String) : ServiceCall? {
            if (registeredServiceCalls.isEmpty()) init()
            return registeredServiceCalls.get(id).let { it?.getConstructor()?.newInstance() }
        }
    }

    fun basePath(): String {
        return Configuration.getBaseUrl()
    }

    abstract fun execute(agent: Agent)
    open fun verify(agent: Agent): Boolean {
        return true
    }

    abstract fun requiredSessionValues(): List<String>

    abstract fun getId(): String

    abstract fun nextPossibleCalls(): List<String>

    open fun description() = this::class.simpleName!!
}