package com.example.serverjvm

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils

object ComponentItemsTable : Table("component_items") {
    val id = integer("id").autoIncrement()
    val data = text("data")
    override val primaryKey = PrimaryKey(id)
}

fun initDatabase() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(ComponentItemsTable)
    }
}

fun insertItem(json: String) = transaction {
    ComponentItemsTable.insert {
        it[data] = json
    }
}

fun fetchAllItems(): List<String> = transaction {
    ComponentItemsTable.selectAll().map { it[ComponentItemsTable.data] }
}