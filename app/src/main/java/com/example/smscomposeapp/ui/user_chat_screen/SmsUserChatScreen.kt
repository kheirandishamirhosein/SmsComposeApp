package com.example.smscomposeapp.ui.user_chat_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smscomposeapp.data.models.SmsModel

@Composable
fun SmsUserChatScreen() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // شماره تلفن
            Text(text = "شماره تلفن",
                modifier = Modifier.padding(bottom = 8.dp),
                style = TextStyle(fontSize = 18.sp)
            )

            // لیست چت
            ChatList(
                chats = listOf(
                    //ChatMessage(sender = "من", message = "سلام!"),
                    //ChatMessage(sender = "شما", message = "سلام! خوبین؟")
                )
            )

            // ورودی متن پیام
            OutlinedTextField(
                value = "", // مقدار متنی که کاربر وارد می‌کند
                onValueChange = {}, // تابعی که هنگام تغییر مقدار فراخوانی می‌شود
                label = { Text(text = "پیام خود را وارد کنید...") }, // برچسب ورودی
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    fun ChatList(chats: List<SmsModel>) {
        LazyColumn {
            items(chats) { chat ->
                ChatMessageItem(chat = chat)
            }
        }
    }

    @Composable
    fun ChatMessageItem(chat: SmsModel) {
        Text(
            text = "${chat.phoneNumber}: ${chat.message}",
            style = TextStyle(fontSize = 16.sp)
        )
    }

