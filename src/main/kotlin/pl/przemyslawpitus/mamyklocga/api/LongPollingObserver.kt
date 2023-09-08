package pl.przemyslawpitus.mamyklocga.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.async.DeferredResult
import pl.przemyslawpitus.mamyklocga.domain.Observer

abstract class LongPollingObserver<T,R>(
    protected val deferredResult: DeferredResult<R>,
): Observer<T> {
    init {
        deferredResult.onTimeout {
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build<Unit>())
        }
    }
}