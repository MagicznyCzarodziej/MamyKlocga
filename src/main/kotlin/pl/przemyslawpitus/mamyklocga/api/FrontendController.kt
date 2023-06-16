package pl.przemyslawpitus.mamyklocga.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("")
class FrontendController {
    @GetMapping("/**/{path:[^.]*}")
    fun redirectAllPathsToIndex(): String {
        return "forward:/index.html"
    }

//    @RequestMapping(value = ["/", "/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/*/{y:[\\w\\-]+}", "/error"])
//    fun getIndex(): String? {
//        return "forward:/index.html"
//    }
}