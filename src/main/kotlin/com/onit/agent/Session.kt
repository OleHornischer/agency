package com.onit.agent

import com.onit.exception.MissingInputException
import kotlin.collections.HashMap

class Session {
    private val values = HashMap<String, Any>()

    fun get(key: String): Any {
        return if (values.contains(key)) values.getOrDefault(
            key,
            ""
        ) else throw MissingInputException("Input for key " + key + " expected in session, but not found. Check your routing.")
    }

    fun put(key: String, value: Any) {
        values.put(key, value)
    }
}

