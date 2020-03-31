package com.exampleproject.data

import androidx.room.*
import com.exampleproject.AvatarConverter
import com.exampleproject.AvatarData
import com.exampleproject.network.FriendshipStatus
import com.exampleproject.network.FriendshipStatusConverter

@Entity(tableName = "contacts")
data class ContactDbo(
    @PrimaryKey
    val contactId: Long,
    @TypeConverters(AvatarConverter::class)
    val avatar: AvatarData?,
    val username: String,
    val online: Boolean,
    var newMessage: Int,
//    @TypeConverters(MessageConverter::class)
    @Embedded(prefix = "last_message_")
    var lastMessage: MessageDbo?,
    @TypeConverters(FriendshipStatusConverter::class)
    val friendshipStatus: FriendshipStatus
) {

    @Ignore
    var isTyping: Boolean = false

    companion object {

        fun fromChatModel(id: Long, contact: Contact): ContactDbo {
            val lastMessage = if (contact.lastMessage != null) {
                MessageDbo.fromNetwork(id, contact.lastMessage!!.id, contact.lastMessage!!)
            } else {
                null
            }
            return ContactDbo(
                id,
                contact.avatar,
                contact.username,
                contact.online == 1,
                contact.newMessage,
                lastMessage,
                contact.status ?: FriendshipStatus.unknown
            )
        }
    }
}