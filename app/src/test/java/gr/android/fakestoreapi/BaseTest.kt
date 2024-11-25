package gr.android.fakestoreapi

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.TurbineContext
import app.cash.turbine.turbineScope
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.AfterClass
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
* ReceiveTurbine is a testing utility from the Turbine library designed to help collect and assert values emitted by a Kotlin Flow.
* It provides methods to await items, check for completion, handle errors, and manage events in asynchronous flow tests.
* This makes it easier to validate flow emissions step-by-step in a structured manner.
* */
// BaseTest is an abstract class that provides utility functions and setup for testing Flow emissions with Turbine.
abstract class BaseTest {
    // A list to hold all instances of ReceiveTurbineWrapper for cleanup after tests.
    private val wrappers = mutableListOf<ReceiveTurbineWrapper<*>>()

    // A helper function to subscribe to a flow within a TurbineContext and wrap it with ReceiveTurbineWrapper.
    // The wrapper is added to the wrappers list for later cleanup.
    context(TurbineContext)
    protected fun <T> Flow<T>.testSubscribe(): ReceiveTurbineWrapper<T> =
        ReceiveTurbineWrapper(testIn(this@TurbineContext)).also { wrappers.add(it) }

    // A helper function to run tests with coroutine and Turbine context for testing suspending functions.
    // It initializes a TestScope and TurbineContext and executes the provided testBody in this environment.
    protected fun runTest(
        context: CoroutineContext = EmptyCoroutineContext,
        testBody: suspend context(TurbineContext, TestScope) () -> Unit,
    ) = kotlinx.coroutines.test.runTest(context = context) {
        val testScope = this
        turbineScope { testBody(this, testScope) }
    }

    // After each test, checks that all wrappers are properly cancelled.
    // Throws an error if any wrapper is not cancelled, ensuring clean tests.
    @After
    fun baseCleanup() {
        wrappers.forEach {
            if (!it.cancelled) throw AssertionError("Wrapper $it has not been cancelled")
        }
    }

    companion object {
        // A static method that runs after all tests in the class are complete.
        // It calls unmockkAll() to clear all mockk mocks, ensuring no mock state carries over between tests.
        @AfterClass
        @JvmStatic
        fun baseTestCleanup() {
            unmockkAll()
        }
    }
}

// A wrapper class around ReceiveTurbine to provide utility functions for verifying Flow emissions.
class ReceiveTurbineWrapper<T>(private val turbine: ReceiveTurbine<T>) {
    // A list to cache emitted items from the flow for easier assertions.
    private val cache: MutableList<T> = mutableListOf()
    // A flag to track if the wrapper has been disposed of (i.e., cancelled).
    var cancelled = false

    // Awaits the item at the specified index and applies an assertion to it.
    // If necessary, waits for more items to fulfill the index.
    suspend fun assertAt(idx: Int, assertion: (T) -> Unit): ReceiveTurbineWrapper<T> {
        if (cache.size <= idx) {
            repeat(idx - cache.size + 1) {
                cache.add(turbine.awaitItem())
            }
        }
        assertion(cache[idx])
        return this
    }

    // Awaits the item at the specified index and asserts that it matches the given condition.
    suspend fun assertValueAt(idx: Int, check: (T) -> Boolean): ReceiveTurbineWrapper<T> {
        return assertAt(idx) { assertTrue(check(it)) }
    }

    // Awaits the item at the specified index and asserts that it equals the expected item.
    suspend fun assertValueAt(idx: Int, item: T): ReceiveTurbineWrapper<T> {
        return assertAt(idx) { assertEquals(item, it) }
    }

    // Ensures that no cached item matches the specified condition.
    fun assertNever(check: (T) -> Boolean): ReceiveTurbineWrapper<T> {
        expectAllItems()
        assertFalse(cache.any(check))
        return this
    }

    // Awaits the next emitted item and asserts that it matches the given condition.
    suspend fun assertValue(check: (T) -> Boolean): ReceiveTurbineWrapper<T> {
        cache.add(turbine.awaitItem())
        assertTrue(check(cache.last()))
        return this
    }

    // Awaits the next emitted item and asserts that it equals the specified item.
    suspend fun assertValue(item: T): ReceiveTurbineWrapper<T> {
        cache.add(turbine.awaitItem())
        assertEquals(item, cache.last())
        return this
    }

    // Asserts that the specified sequence of items is emitted in the correct order.
    suspend fun assertValues(vararg items: T): ReceiveTurbineWrapper<T> {
        items.forEachIndexed { index, t -> assertValueAt(index, t) }
        return this
    }

    // Waits until the specified count of items has been emitted, asserting no additional events.
    suspend fun awaitCount(count: Int) = assertValueCount(count)

    // Asserts that the total emitted item count equals the specified count.
    // If more items are needed, it waits for them; otherwise, checks for no extra events.
    suspend fun assertValueCount(count: Int): ReceiveTurbineWrapper<T> {
        val remaining = count - cache.size
        if (remaining < 0) {
            throw AssertionError("Expected $count values but have ${cache.size}")
        } else if (remaining == 0) {
            turbine.expectNoEvents()
        } else {
            repeat(remaining) {
                cache.add(turbine.awaitItem())
            }
            turbine.expectNoEvents()
        }
        return this
    }

    // Asserts a condition on the last cached item.
    fun assertLast(assertion: (T) -> Unit): ReceiveTurbineWrapper<T> {
        expectAllItems()
        assertion(cache.last())
        return this
    }

    // Returns a list of all cached items, useful for custom assertions.
    fun values(): List<T> {
        expectAllItems()
        return cache
    }

    // Returns the number of cached items.
    fun valueCount(): Int = values().size

    // Asserts that no items have been emitted by the flow.
    suspend fun assertEmpty(): ReceiveTurbineWrapper<T> = assertValueCount(0)

    // Alias for assertEmpty, asserting that no values were emitted by the flow.
    suspend fun assertNoValues(): ReceiveTurbineWrapper<T> = assertValueCount(0)

    // Disposes of the turbine, cancelling further listening, and sets cancelled to true.
    suspend fun dispose(): ReceiveTurbineWrapper<T> {
        turbine.cancelAndIgnoreRemainingEvents()
        cancelled = true
        return this
    }

    // Attempts to receive all remaining items from the flow and adds them to cache.
    private fun expectAllItems() {
        try {
            val channel = turbine.asChannel()
            do {
                val current = channel.tryReceive().getOrNull()
                current?.let { cache.add(it) }
            } while (current != null)
        } catch (e: Throwable) {
            // Noop, ignore exceptions that may occur when receiving is complete
        }
    }
}