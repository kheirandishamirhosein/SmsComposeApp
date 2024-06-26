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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
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
import com.example.smscomposeapp.util.FormatTimestamp


class SmsUserChatScreenFragment(
    private val navController: NavController,
    private val phoneNumber: String?
) : Fragment() {

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
            SmsUserChatScreen(
                navController = navController,
                viewModel = viewModel,
                phoneNumber = phoneNumber!!
            )
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
fun SmsUserChatScreen(navController: NavController, viewModel: SmsViewModel, phoneNumber: String?) {
    var phoneNumberState by remember { mutableStateOf(phoneNumber ?: "") }
    var message by remember { mutableStateOf("") }
    val smsModel by viewModel.smsModels.collectAsState()
    val context = LocalContext.current
    val filterMessages = if (phoneNumberState.isNotEmpty()) {
        smsModel.filter { it.phoneNumber == phoneNumberState }
    } else {
        emptyList()
    }
    val currentTimeMillis = System.currentTimeMillis()
    //Registration and cancellation of registration BroadcastReceiver
    DisposableEffect(Unit) {
        val smsBrdReceiver = SmsBrdReceiver(viewModel)
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        context.registerReceiver(smsBrdReceiver, intentFilter)
        onDispose {
            context.unregisterReceiver(smsBrdReceiver)
        }
    }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                OutlinedTextField(
                    value = phoneNumberState,
                    onValueChange = { phoneNumberState = it },
                    readOnly = phoneNumber != null,
                    label = { Text(text = "phone number") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                LazyColumn {
                    items(filterMessages) { sms ->
                        ChatMessageItem(chat = sms)
                    }
                    Log.e("khkhkh", "smsModel: $filterMessages")
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
                        if (phoneNumberState.isNotEmpty()) {
                            viewModel.sendSms(
                                SmsModel(
                                    phoneNumber = phoneNumberState,
                                    message = message,
                                    timestamp = currentTimeMillis,
                                    messageType = MessageType.SENT
                                )
                            )
                            message = ""
                        } else {
                            viewModel.sendSms(
                                SmsModel(
                                    phoneNumber = phoneNumberState,
                                    message = message,
                                    timestamp = currentTimeMillis,
                                    messageType = MessageType.SENT
                                )
                            )
                            message = ""
                        }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = if (chat.messageType == MessageType.SENT) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .padding(2.dp)
                .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            color = Color.Unspecified,
        ) {
            Text(
                text = chat.message,
                style = TextStyle(fontSize = 16.sp),
                textAlign = textAlign,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(1.dp))
    Text(
        text = FormatTimestamp.formatTimestamp(chat.timestamp),
        style = TextStyle(fontSize = 10.sp, color = Color.Gray),
        textAlign = textAlign,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 18.dp, start = 18.dp, bottom = 16.dp)
    )

}


