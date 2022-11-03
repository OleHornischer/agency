package com.onit.routing

import org.reflections.Reflections
import java.io.File
import java.nio.file.Files
import kotlin.reflect.KClass


class Schedule private constructor(private val assignments: List<AgentInstructions>) {

    private var next = 0

    companion object {
        fun init() = Schedule(listOf(AgentInstructions(RandomRoute::class, emptyMap())))

        fun init(file: String): Schedule {
            @Suppress("UNCHECKED_CAST")
            val routesByName = Reflections("com.onit").getTypesAnnotatedWith(com.onit.routing.annotation.Route::class.java)
                    .associateBy({ it.getAnnotation(com.onit.routing.annotation.Route::class.java).name }, { it::class as KClass<out Route> })

            val scheduleConfiguration = Files.readAllLines(File(file).toPath()).iterator()
            val instructions = mutableListOf<AgentInstructions>()

            var seedParameters = mutableMapOf<String, String>()
            var route: KClass<out Route> = RandomRoute::class
            while (scheduleConfiguration.hasNext()) {
                val line = scheduleConfiguration.next()
                if (line.startsWith(' ') || line.startsWith('\t')) {
                    seedParameters.put(
                        line.substring(line.indexOf('=')).trim(),
                        line.substring(line.indexOf('=') + 1, line.length)
                    )
                } else {
                    instructions.add(AgentInstructions(route, seedParameters))
                    seedParameters= mutableMapOf()
                    route = routesByName.get(line.trim())?:RandomRoute::class
                }

            }
            return Schedule(instructions)
        }

    }

    fun nextInstructions() = assignments[next % assignments.size]
}