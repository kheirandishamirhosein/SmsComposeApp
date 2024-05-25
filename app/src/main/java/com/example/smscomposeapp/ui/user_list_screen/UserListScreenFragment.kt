package com.example.smscomposeapp.ui.user_list_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.di.SmsContainer
import com.example.smscomposeapp.infrastructure.SmsViewModel

class UserListScreenFragment(private val navController: NavHostController) : Fragment() {

    private lateinit var viewModel: SmsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ComposeView(requireContext())
        viewModel = SmsContainer.getSmsViewModel()
        view.setContent {
            UserListScreen(navController = navController, viewModel)
        }
        return view
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: SmsViewModel) {
    val lastSmsUser by viewModel.lastSmsUser.collectAsState()

    Scaffold(
        //Action Button
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("newChatScreen")
                },
                content = {
                    Icon(Icons.Default.Email, contentDescription = "Send SMS")
                },
                modifier = Modifier.padding(bottom = 46.dp, end = 16.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(lastSmsUser) { sms ->
                UserItem(navController = navController, smsModel = sms, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun UserItem(navController: NavController, smsModel: SmsModel, viewModel: SmsViewModel) {
    val showDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDialog.value = true
                    },
                    onTap = {
                        navController.navigate("smsUserChatScreen/${smsModel.phoneNumber}")
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Gray)
        )
         */
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = smsModel.phoneNumber,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = smsModel.message,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Delete Chat") },
            text = { Text("Are you sure you want to delete this chat?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSmsChat(smsModel)
                        showDialog.value = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


}