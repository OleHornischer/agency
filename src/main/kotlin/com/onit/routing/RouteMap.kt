package com.onit.routing

import com.onit.configuration.Configuration
import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object RouteMap {

    val routeMap = LinkedHashMap<String, List<String>>()

    fun init() {
        val serviceCalls = Reflections(Configuration.getServiceCallPackages()).getSubTypesOf(ServiceCall::class.java)
        serviceCalls
            .forEach {
                val instance = it.getConstructor()?.newInstance()
                if (null != instance)
                routeMap.put(instance.getId(), instance.nextPossibleCalls())
            }
    }
}