package pl.przemyslawpitus.mamyklocga

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

open class WithLogger {
    val logger: Logger = LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name)
}

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass?.kotlin?.companionObject?.java == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}