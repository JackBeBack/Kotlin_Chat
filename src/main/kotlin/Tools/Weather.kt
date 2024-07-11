package Tools

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool

@Tool("Returns the weather forecast for Aachen")
fun getWeather(@P("The city for which the weather forecast should be returned")city: String): String {
    println("I got called")
    return "It is 42 degrees here in $city"
}

