package pl.przemyslawpitus.mamyklocga.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerTypePredicate
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

private const val API_ENDPOINTS_PREFIX = "/api"

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        // Serve all endpoints with @RestController in api package from /api prefix.
        // Static files will still be served from root
        configurer.addPathPrefix(
            API_ENDPOINTS_PREFIX,
            HandlerTypePredicate
                .forAnnotation(RestController::class.java)
                .and(HandlerTypePredicate.forBasePackage("pl.przemyslawpitus.mamyklocga.api"))
        )
    }
}
