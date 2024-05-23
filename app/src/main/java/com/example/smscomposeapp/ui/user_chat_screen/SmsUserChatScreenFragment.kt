package com.example.smscomposeapp.ui.user_chat_screen

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.smscomposeapp.data.models.MessageType
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.di.SmsContainer
import com.example.smscomposeapp.infrastructure.SmsBrdReceiver
import com.example.smscomposeapp.infrastructure.SmsViewModel


class SmsUserChatScreenFragment(private val navController: NavController) : Fragment() {

    private lateinit var viewModel: SmsViewModel
    private lateinit var smsBrdReceiver: SmsBrdReceiver

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ComposeView(requireContext())
        viewModel = SmsContainer.getSmsViewModel()
        smsBrdReceiver = SmsBrdReceiver(viewModel)
        view.setContent {
            SmsUserChatScreen(navController = navController, viewModel = viewModel)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        context?.registerReceiver(smsBrdReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(smsBrdReceiver)
    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SmsUserChatScreen(navController: NavController, viewModel: SmsViewModel) {
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val smsModel by viewModel.smsModels.collectAsState()

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { value ->
                        if (value.length <= 11) {
                            phoneNumber = value
                        }
                    },
                    label = { Text(text = "phone number") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                LazyColumn {
                    items(viewModel.smsModels.value) { sms ->
                        ChatMessageItem(chat = sms)
                    }
                    Log.e("khkhkh", "smsModel: $smsModel")
                }
            }
        },
        bottomBar = {
            BottomAppBar {
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
                IconButton(
                    onClick = {
                        viewModel.sendSms(
                            SmsModel(
                                phoneNumber = phoneNumber,
                                message = message,
                                messageType = MessageType.SENT
                            )
                        )
                        message = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "ارسال"
                    )
                }
                Log.d("khkhkh send sms", "phoneNumber: $phoneNumber , message: $message")
            }
        }
    )

}

private fun String.isRTL(): Boolean {
    return Character.getDirectionality(this[0]) == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
            Character.getDirectionality(this[0]) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
}

@Composable
fun ChatMessageItem(chat: SmsModel) {
    val textAlign = if (chat.messageType == MessageType.SENT) TextAlign.End else TextAlign.Start
    Text(
        text = chat.message,
        style = TextStyle(fontSize = 16.sp),
        textAlign = textAlign,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

