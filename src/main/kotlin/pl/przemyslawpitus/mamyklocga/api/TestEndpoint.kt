package pl.przemyslawpitus.mamyklocga.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("")
class TestEndpoint {

    @GetMapping("/test")
    fun hello(
    ): ResponseEntity<*> {
        return ResponseEntity.ok("Siemano")
    }
}