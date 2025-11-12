package team.mediagroup.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.*

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/evms",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "yourpassword"
        )

        transaction {
            SchemaUtils.create(Departments, Users, Employees, HolidayHistories)
        }
    }
}
