package com.example.smscomposeapp.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smscomposeapp.di.SmsContainer
import com.example.smscomposeapp.infrastructure.SmsViewModel
import com.example.smscomposeapp.ui.theme.SmsComposeAppTheme
import com.example.smscomposeapp.ui.user_chat_screen.SmsUserChatScreen
import com.example.smscomposeapp.ui.user_list_screen.UserListScreen
import com.example.smscomposeapp.util.AppSharePerf
import com.example.smscomposeapp.util.SmsPermission
import com.example.smscomposeapp.util.SmsPermissionCode

class MainActivity : AppCompatActivity() {

    private lateinit var permission: SmsPermission
    private lateinit var viewModel: SmsViewModel
    private lateinit var sharePref: AppSharePerf

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission = SmsPermission(this)
        viewModel = SmsContainer.getSmsViewModel()
        sharePref = AppSharePerf(this)

        setContent {
            SmsComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    sendMessageIfPermissionsGranted()
                    NavControllerManagementApp()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SmsPermissionCode.SEND_SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO: send sms
                    Log.e("khkhkh", "send sms: ${permission.isSendSmsPermissionGranted()}")
                } else {
                    permission.requestSendSmsPermission(SmsPermissionCode.SEND_SMS_PERMISSION_CODE)
                }
            }

            SmsPermissionCode.RECEIVE_SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO: receive SMS
                    Log.e("khkhkh", "receive sms: ${permission.isReceiveSmsPermissionGranted()}")
                } else {
                    permission.requestReceiveSmsPermission(SmsPermissionCode.RECEIVE_SMS_PERMISSION_CODE)
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendMessageIfPermissionsGranted() {
        if (!permission.isReceiveSmsPermissionGranted()) {
            permission.requestReceiveSmsPermission(SmsPermissionCode.RECEIVE_SMS_PERMISSION_CODE)
        } else {
            if (!permission.isSendSmsPermissionGranted()) {
                permission.requestSendSmsPermission(SmsPermissionCode.SEND_SMS_PERMISSION_CODE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun NavControllerManagementApp() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "userListScreen"
        ) {
            composable("userListScreen") {
                UserListScreen(navController = navController, viewModel = viewModel)
            }
            composable("smsUserChatScreen") {
                SmsUserChatScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}