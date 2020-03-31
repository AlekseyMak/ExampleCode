package com.exampleproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: ContactDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contacts: List<ContactDbo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFromVideoEvent(contacts: List<ContactDbo>)

    @Query("DELETE FROM contacts")
    fun clear(): Int

    @Query("SELECT * FROM contacts")
    fun getAll(): Flow<List<ContactDbo>>

    @Query("SELECT * FROM contacts ORDER BY last_message_createdAt DESC")
    fun getAllOrdered(): Flow<List<ContactDbo>>

    @Query("SELECT * FROM contacts WHERE username LIKE :query")
    fun search(query: String): Flow<List<ContactDbo>>

    @Query("SELECT * FROM contacts WHERE contactId == :contactId")
    fun get(contactId: Long): Flow<ContactDbo>

    @Query("UPDATE contacts SET online = :isOnline WHERE contactId == :contactId")
    fun updateOnline(contactId: Long, isOnline: Boolean)

    @Query("UPDATE contacts SET newMessage = :unread WHERE contactId == :contactId")
    fun updateUnread(contactId: Long, unread: Int)

    @Query("SELECT COALESCE(SUM(newMessage), 0) FROM contacts")
    fun fetchUnreadCount(): Flow<Int>
}