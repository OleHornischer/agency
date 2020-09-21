package com.onit.agent

import com.onit.configuration.Configuration
import com.onit.routing.Route
import com.onit.service_calls.ServiceCall
import com.onit.statistic.DataPoint
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Agent
    (val route: Route) : Runnable {
    val session = Session()
    var finished = false
    var successes = 0
    var errors = 0

    val requestDurationStatistic = ArrayList<DataPoint>()

    override fun run() {
        if (!route.isDone()) callService(route.next())
    }

    private fun callService(serviceCall: ServiceCall) {
        println("Calling " + serviceCall.description())
        val start = LocalDateTime.now()
        try {
            serviceCall.execute(this)
            successes++
        } catch (e: Exception) {
            errors++
        }
        requestDurationStatistic.add(
            DataPoint(
                start,
                serviceCall.description(),
                start.until(LocalDateTime.now(), ChronoUnit.MILLIS).toDouble()
            )
        )

        if (!route.isDone()) {
            Thread.sleep(Configuration.getCallDelayInSec() * 1000L)
            callService(route.next())
        } else {
            finished = true
        }
    }
}