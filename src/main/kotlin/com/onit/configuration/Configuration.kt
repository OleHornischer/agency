package com.onit.configuration

import com.natpryce.konfig.*
import com.onit.service_calls.ServiceCall
import java.io.File
import java.nio.file.Path
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object Configuration {

    val config = ConfigurationProperties.fromResource("config.properties")
    val random = Random(0)

    fun getBaseUrl(): String {
        return get(Key("rest.baseUrl", stringType))
    }

    fun getErrorThreshold(): Int {
        return getOrDefault(Key("result.error.threshold", intType), 0)
    }

    fun getCallDelayInSec(): Int {
        return getOrDefault(Key("call.delay.sec", intType), 1)
    }

    fun getSimulationStepDelayInSec(): Int {
        return getOrDefault(Key("simulation.step.delay.sec", intType), 1)
    }

    fun getOutputFile(): Path {
        return File(getOrDefault(Key("output.fileName", stringType), "/tmp/agency/plot.html")).toPath()
    }

    fun getServiceCallPackages(): List<String> {
        return get(Key("service.call.packages", stringType)).split(";")
    }

    fun getInitialRandomServiceCall() = get(Key("initial.random.service.call", stringType))

    private fun <T> get(configurationKey: Key<T>): T {
        return config.get(configurationKey)
    }

    private fun <T> getOrDefault(configurationKey: Key<T>, defaultValue: T): T {
        return config.getOrElse(configurationKey, defaultValue)
    }
}