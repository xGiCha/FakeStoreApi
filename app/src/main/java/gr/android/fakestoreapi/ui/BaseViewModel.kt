package gr.android.fakestoreapi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface BaseViewModel<UiModel, Event> {
    val uiModels: Flow<UiModel>
    val events: Flow<Event>
}

class EventFlow<Event> : AbstractFlow<Event>() {
    // this channel queue events if no active subscribers
    private val eventsQueue = Channel<Event>(capacity = Channel.UNLIMITED)

    // this flow drops events if no active subscribers
    private val eventsFlow = MutableSharedFlow<Event>()

    private val resultFlow = merge(eventsQueue.receiveAsFlow(), eventsFlow)

    @ExperimentalCoroutinesApi
    override suspend fun collectSafely(collector: FlowCollector<Event>) {
        resultFlow.collect(collector)
    }

    suspend fun emit(event: Event) = eventsFlow.emit(event)
}

context(ViewModel)
fun <Event> EventFlow<Event>.emitAsync(event: Event) {
    viewModelScope.launch {
        this@emitAsync.emit(event)
    }
}

abstract class BaseViewModelImpl<UiModel, Event> : ViewModel(), BaseViewModel<UiModel, Event> {
    override val uiModels: Flow<UiModel>
        get() = uiState.mapNotNull { it }
    override val events = EventFlow<Event>()
    abstract val uiState: StateFlow<UiModel?>

    protected fun <T> MutableSharedFlow<T>.emitAsync(item: T) {
        viewModelScope.launch {
            this@emitAsync.emit(item)
        }
    }
}
