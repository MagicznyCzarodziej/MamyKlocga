package pl.przemyslawpitus.mamyklocga

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

//@SpringBootTest
class MamyKlocgaApplicationTests {

//    @Test
//    fun contextLoads() {
//    }

    @Test
    fun xd() {
        val pointsForBuild = listOf(
            mapOf("jeden" to 1, "dwa" to 2),
            mapOf("jeden" to 2, "dwa" to 4)
        )
        val result = pointsForBuild
            .flatMap { it.entries }
            .groupBy({ it.key }, {it.value})
            .mapValues { it.value.sum() }

        println(result)

        assert(result == mapOf("jeden" to 3, "dwa" to 6))
    }
}

