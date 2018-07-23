package core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import model.table.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class Connection {

    fun getDataSource(): HikariDataSource {
        val config = HikariConfig()

        // H2
        // config.driverClassName = "org.h2.Driver"
        // config.jdbcUrl = "jdbc:h2:mem:test"
        // config.maximumPoolSize = 3
        // config.isAutoCommit = false
        // config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        // SQLite
        // config.driverClassName = "org.sqlite.JDBC"
        // config.jdbcUrl = "jdbc:sqlite:core.db"
        // config.transactionIsolation = "TRANSACTION_SERIALIZABLE"

        // MySql
        config.driverClassName = "com.mysql.jdbc.Driver"
        config.jdbcUrl = "jdbc:mysql://192.168.1.7:3306/kblog"
        config.username = "root"
        config.password = ""
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        config.validate()
        return HikariDataSource(config)
    }

    fun connect() {
        // SQLite
        // Database.connect("jdbc:sqlite:core.db", "org.sqlite.JDBC")
        // TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        // H2
        // Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // MySql
        // Database.connect("jdbc:mysql://192.168.1.7:3306/kblog",driver = "com.mysql.jdbc.Driver", user = "root", password = "")

        // HikariCP
        Database.connect(getDataSource())
    }

    fun prepareTables() {
        transaction {
            SchemaUtils.create(Users)
        }
    }
}