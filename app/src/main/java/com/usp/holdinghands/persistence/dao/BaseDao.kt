package com.usp.holdinghands.persistence.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.usp.holdinghands.model.entities.BaseEntity

abstract class BaseDao<T : BaseEntity>(
    private val tableName: String
) {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entities: List<T>)

    @Update
    abstract fun update(entity: T)

    @Update
    abstract fun update(entities: List<T>)

    @Delete
    abstract fun delete(entity: T)

    @Delete
    abstract fun delete(entities: List<T>)

    @RawQuery
    protected abstract fun deleteAll(query: SupportSQLiteQuery): Boolean

    fun deleteAll() {
        val query = SimpleSQLiteQuery("DELETE FROM $tableName")
        deleteAll(query)
    }

    @RawQuery
    protected abstract fun getAll(query: SupportSQLiteQuery): List<T>

    fun getAll(): List<T> {
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName")
        return getAll(query)
    }

    @RawQuery
    protected abstract fun getEntitySync(query: SupportSQLiteQuery): List<T>?

    fun getEntitySync(id: Int): T? {
        return getEntitySync(listOf(id))?.firstOrNull()
    }

    fun getEntitySync(ids: List<Int>): List<T>? {
        val result = StringBuilder()
        for (index in ids.indices) {
            if (index != 0) {
                result.append(",")
            }
            result.append("'").append(ids[index]).append("'")
        }
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName WHERE id IN ($result);")
        return getEntitySync(query)
    }
}
