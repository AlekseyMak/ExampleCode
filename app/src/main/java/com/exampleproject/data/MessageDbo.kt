package com.exampleproject.data

import NetworkLayer.MessageStatus
import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.internal.LinkedTreeMap

@Entity(tableName = "messages")
data class MessageDbo(
    @PrimaryKey
    var id: Long,
    @TypeConverters(MessageTypeConverter::class)
    val type: MessageType,
    val text: String,
    val createdAt: Long,
    val contactId: Long,
    val inbox: Boolean,
    @TypeConverters(MessageStatusConverter::class)
    var status: MessageStatus,
    val image: String?
) {

    fun getSystemMessage(context: Context, isColored: Boolean = false): CharSequence {
        if (type != MessageType.system) {
            throw IllegalArgumentException("Not a system message")
        }

        return when (text) {
            //"SYS_MSG_1" -> "You added this person from video chat"
            "SYS_MSG_2" -> {
                if (isColored) {
                    context.getColoredString(R.string.messages_last_message_removed,
                        R.color.CTRed
                    )} else {
                    context.getString(R.string.messages_last_message_removed)
                }
            }
            //"SYS_MSG_2" -> You've been blocked, you cannot message this user
            "SYS_MSG_5" -> context.getString(R.string.messages_last_message_no_messages)
            "SYS_MSG_6" -> {
                if (isColored) {
                    context.getColoredString(
                        R.string.messages_last_message_request_got,
                        R.color.CTGreen
                    )
                } else {
                    context.getString(
                        R.string.messages_last_message_request_got
                    )
                }
            }
            "SYS_MSG_7" -> {
                if (isColored) {
                    context.getColoredString(
                        R.string.messages_last_message_friends_now,
                        R.color.CTGreen
                    )
                } else {
                    context.getString(
                        R.string.messages_last_message_friends_now
                    )
                }
            }
            "SYS_MSG_8" -> context.getString(R.string.messages_last_message_request_declined)
            "SYS_MSG_14" -> context.getString(R.string.messages_last_message_support)
            else -> context.getString(R.string.messages_last_message_hidden_message)
        }
    }

    companion object {

        fun fromNetwork(contactId: Long, messageId: Long, message: Message): MessageDbo {
            val imagePath = if (message.type == MessageType.image) {
                val suffix = (message.messageData as LinkedTreeMap<String, String>)["m"]
                ImageSizeType.medium.url(suffix)?.toString()
            } else {
                null
            }
//            val text = if (message.type == MessageType.text_system ||
//                message.type == MessageType.text ||
//                message.type == MessageType.system ) {
//                message.messageData.toString()
//            } else {
//                ""
//            }
            val text = message.messageData.toString()
            val type = if (text.startsWith(GIFT_MESSAGE_PREFIX)) {
                MessageType.gift
            } else if (text == "LOGGER_CALL"){
                MessageType.missed_call
            } else {
                message.type
            }
            return MessageDbo(
                messageId,
                type,
                text,
                message.createdAt,
                contactId,
                message.isIncomingMessage == 1,
                if (message.read == 1) MessageStatus.read else MessageStatus.delivered,
                imagePath
            )
        }
    }
}