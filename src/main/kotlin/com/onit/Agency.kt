package com.onit

import com.onit.agent.Agent
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import com.onit.configuration.Configuration
import com.onit.routing.RouteMap
import com.onit.routing.Schedule
import com.onit.statistic.Graphing
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool


class Agency : CliktCommand() {

    private val numberOfAgents: Int by option("-a", "--agents", help = "The number of parallel agents").int()
        .default(10)
    private val duration: Long by option(
        "-d",
        "--duration",
        help = "The (minimum) duration of the test in seconds"
    ).long().default(60)
    private val plotGraph: Boolean by option(
        "-p",
        "--plot",
        help = "Plot a graph of the results"
    ).flag()
    private val scheduleFile: String? by option(
        "-s",
        "--schedule",
        help = "Filename to a specification which routes to assign to the agents in which order"
    )

    val errorThreshold = Configuration.getErrorThreshold()

    override fun run() {
        val start = LocalDateTime.now()
        println("Starting execution at $start")
        val executor = ForkJoinPool(numberOfAgents)
        val agents = ArrayList<Agent>()

        RouteMap.init()
        val schedule = scheduleFile?.let { Schedule.init(it) } ?: Schedule.init()

        while (LocalDateTime.now().minus(duration, ChronoUnit.SECONDS).isBefore(start)
            || agents.any { !it.finished }
        ) {
            val activeAgents = agents.filter { !it.finished }
            if (LocalDateTime.now().minus(duration, ChronoUnit.SECONDS).isBefore(start)
                && activeAgents.count() < numberOfAgents
            ) {
                println("Currently " + activeAgents.count() + " agents are active. Spawning another one.")
                agents.add(spawnAgent(schedule, executor))
            } else {
                println("Currently " + activeAgents.count() + " agents are active. ")
            }
            Thread.sleep(1000L * Configuration.getSimulationStepDelayInSec())
        }
        println("Completed execution at " + LocalDateTime.now())
        if (plotGraph) Graphing.plotGraph(agents)
        throw ProgramResult(if (agents.sumOf { it.errors } > errorThreshold) 1 else 0)
    }

    private fun spawnAgent(schedule: Schedule, executorService: ExecutorService): Agent {
        val agent = Agent(schedule.nextInstructions())
        executorService.submit(agent)
        return agent
    }
}