package com.example.smscomposeapp.ui.user_chat_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.infrastructure.SmsViewModel

@Composable
fun SmsUserChatScreen(viwModel: SmsViewModel) {

    var message by remember { mutableStateOf("") }


    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text(text = "phone number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                ChatList(
                    chats = listOf(
                        SmsModel(phoneNumber = "من", message = "سلام!"),
                        SmsModel(phoneNumber = "شما", message = "سلام! خوبین؟")
                    ).reversed()
                )
            }
        },
        bottomBar = {
            BottomAppBar() {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text(text = "message") },
                    textStyle = if (message.isNotEmpty()) {
                        TextStyle(textAlign = if (message.isRTL()) TextAlign.End else TextAlign.Start)
                    } else {
                        TextStyle(textAlign = TextAlign.Start)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
                IconButton(onClick = { /* ارسال پیام */ }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "ارسال"
                    )
                }
            }
        }
    )

}

private fun String.isRTL(): Boolean {
    return Character.getDirectionality(this[0]) == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
            Character.getDirectionality(this[0]) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
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
    val textAlign = if (chat.phoneNumber == "من") TextAlign.End else TextAlign.Start
    Text(
        text = "${chat.phoneNumber}: ${chat.message}",
        style = TextStyle(fontSize = 16.sp),
        textAlign = textAlign,
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
}

