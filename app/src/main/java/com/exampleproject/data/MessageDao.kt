package com.exampleproject.data

import NetworkLayer.MessageStatus
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: MessageDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: List<MessageDbo>)

    @Query("DELETE FROM messages WHERE id == :id")
    fun remove(id: Long)

    @Query("DELETE FROM messages")
    fun clear(): Int

    @Query("SELECT * FROM messages")
    fun getAll(): Flow<List<MessageDbo>>

    @Query("SELECT * FROM messages WHERE id == :id")
    fun get(id: Long): MessageDbo

    @Query("SELECT * FROM messages WHERE contactId == :contactId ORDER BY createdAt DESC")
    fun getByUserId(contactId: String): Flow<List<MessageDbo>>

    @Query("UPDATE messages SET id = :newId WHERE id == :oldId")
    fun updateMessageId(oldId: Long, newId: Long)

    @Query("UPDATE messages SET id = :newId, status = :newStatus WHERE id == :oldId")
    fun updateMessageStatusAndId(oldId: Long, newId: Long, newStatus: MessageStatus)

    @Transaction
    fun updateMessage(oldMessageId: Long, newMessage: MessageDbo) {
        remove(oldMessageId)
        insert(newMessage)
    }

    @Query("UPDATE messages SET status = :newStatus WHERE contactId == :contactId")
    fun updateStatusForContact(newStatus: MessageStatus, contactId: String)
}