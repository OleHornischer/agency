package com.onit.service_calls

import com.onit.agent.Agent
import kotlin.reflect.KClass

class Done : ServiceCall() {


    companion object {
        const val description = "Done"
        const val ID = "DONE"
    }

    override fun execute(agent: Agent) = println("Agent is done.")

    override fun requiredSessionValues(): List<String> = ArrayList()

    override fun nextPossibleCalls(): List<String> = ArrayList()

    override fun getId() = ID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun description() = description
}