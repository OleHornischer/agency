package com.onit.statistic

import com.onit.agent.Agent
import com.onit.configuration.Configuration
import com.onit.service_calls.Done
//import kscience.plotly.*
//import kscience.plotly.models.ScatterMode
import java.time.format.DateTimeFormatter
//
object Graphing {
//
//    @OptIn(UnstablePlotlyAPI::class)
    fun plotGraph(agents: List<Agent>) {
//        val summaryData = calculatSummaryData(agents)
//        val individualData = calculateIndividualEndpointData(agents)
//
//        val plot = Plotly.grid {
//
//            plot(row = 1, width = 12) {
//                scatter {
//                    x.set(summaryData.first)
//                    y.set(summaryData.second)
//                    name = "All Endpoints Response Times"
//                    mode = ScatterMode.markers
//                }
//
//                layout {
//                    title = "All Endpoints Response Times"
//                    xaxis {
//                        title = "Progress"
//                    }
//                    yaxis {
//                        title = "Response Times"
//                    }
//                }
//            }
//
//            individualData.entries.sortedBy { it.key }
//                    .forEach {
//                        plot(row = 1, width = 12) {
//                            scatter {
//                                x.set(it.value.first)
//                                y.set(it.value.second)
//                                name = it.key + " Response Times"
//                                mode = ScatterMode.markers
//                            }
//
//                            layout {
//                                title = it.key + " Response Times"
//                                xaxis {
//                                    title = "Progress"
//                                }
//                                yaxis {
//                                    title = "Response Times"
//                                }
//                            }
//                        }
//                    }
//
//        }
//
//        val fileName = Configuration.getOutputFile()
//        plot.makeFile(fileName)
//        println("Plot created at $fileName")
    }
//
//    private fun calculatSummaryData(agents: List<Agent>): Pair<List<String>, List<Double>> {
//        val x =
//            agents.map { it.requestDurationStatistic }
//                    .flatMap { it.toList() }
//                    .sortedBy { it.time }
//                    .map { it.time }
//                    .map {
//                        it.format(
//                            DateTimeFormatter.ISO_DATE_TIME
//                        )
//                    }
//        val y = agents.map { it.requestDurationStatistic }
//                .flatMap { it.toList() }
//                .sortedBy { it.time }
//                .map { it.value }
//        return Pair(x, y)
//    }
//
//    private fun calculateIndividualEndpointData(agents: List<Agent>): Map<String, Pair<List<String>, List<Double>>> {
//        val result = LinkedHashMap<String, Pair<MutableList<String>, MutableList<Double>>>()
//        agents.map { it.requestDurationStatistic }
//                .flatMap { it.toList() }
//                // We don't want a graph for the final stop, no queries are sent here
//                .filter { !it.description.equals(Done.description) }
//                .sortedBy { it.time }
//                .map { Pair(it.description, it) }
//                .forEach {
//                    val data = result.getOrPut(it.first) { Pair(mutableListOf(), mutableListOf()) }
//                    data.first.add(it.second.time.format(DateTimeFormatter.ISO_DATE_TIME))
//                    data.second.add(it.second.value)
//                }
//
//        return result
//    }
}