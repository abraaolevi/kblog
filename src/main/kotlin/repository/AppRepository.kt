package repository

import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.experimental.CoroutineContext

open class AppRepository {
    private val dispatcher: CoroutineContext

    init {
        dispatcher = newFixedThreadPoolContext(5, "core-pool")
    }

    suspend fun <T> query(block: () -> T): T {
        return withContext(dispatcher) {
            transaction { block() }
        }
    }
}