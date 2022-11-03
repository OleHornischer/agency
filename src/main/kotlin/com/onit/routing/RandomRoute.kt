package com.onit.routing

import com.onit.configuration.Configuration
import com.onit.routing.annotation.Route
import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall

@Route(name="Random")
class RandomRoute : com.onit.routing.Route {

    private var serviceCall: ServiceCall? = null

    override fun isDone(): Boolean {
        return when (serviceCall) {
            Done() -> true
            else -> false
        }
    }

    override fun next(): ServiceCall {
        if (null == serviceCall) {
            try {
                println("Initializing first random service call with " + Configuration.getInitialRandomServiceCall())
                serviceCall = ServiceCall.instanceForId(Configuration.getInitialRandomServiceCall())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val possibleNextCalls = serviceCall?.nextPossibleCalls() ?: listOf(Done.ID)

            val nextCall =ServiceCall.instanceForId(
                possibleNextCalls.get(Configuration.random.nextInt(possibleNextCalls.size)))
                    ?: Done()

            serviceCall = nextCall
        }
        return serviceCall ?: Done()
    }
}