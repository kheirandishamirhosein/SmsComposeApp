package com.example.smscomposeapp.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.di.SmsContainer
import com.example.smscomposeapp.doman.SmsReceiver
import com.example.smscomposeapp.infrastructure.SmsViewModel
import com.example.smscomposeapp.infrastructure.Receiver
import com.example.smscomposeapp.ui.theme.SmsComposeAppTheme
import com.example.smscomposeapp.ui.user_chat_screen.SmsUserChatScreenFragment
import com.example.smscomposeapp.util.AppSharePerf
import com.example.smscomposeapp.util.SmsPermission
import com.example.smscomposeapp.util.SmsPermissionCode
import com.example.smscomposeapp.util.SmsPermissionCode.REQUEST_READ_PHONE_STATE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : FragmentActivity() {

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
                    sendMessageIfPermissionsGranted(this)
                    //SmsPermission()
                    //SmsScreen()
                    //SmsUserChatScreen(viewModel)
                    val fragment = SmsUserChatScreenFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .commit()

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
                } else {
                    permission.requestSendSmsPermission(SmsPermissionCode.SEND_SMS_PERMISSION_CODE)
                }
            }

            SmsPermissionCode.RECEIVE_SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Receiver.receiveMessage(this, this)
                } else {
                    permission.requestReceiveSmsPermission(SmsPermissionCode.RECEIVE_SMS_PERMISSION_CODE)
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendMessageIfPermissionsGranted(mainActivity: MainActivity) {
        if (!permission.isReceiveSmsPermissionGranted()) {
            permission.requestReceiveSmsPermission(SmsPermissionCode.RECEIVE_SMS_PERMISSION_CODE)
        } else {
            if (!permission.isSendSmsPermissionGranted()) {
                permission.requestSendSmsPermission(SmsPermissionCode.SEND_SMS_PERMISSION_CODE)
            }
        }
    }



    /*
    private fun sendSms(number: TextFieldValue, text: TextFieldValue) {
        val phoneNumber = number.text
        val message = text.text
        val smsModel = SmsModel(phoneNumber = phoneNumber, message = message)
        viewModel.sendSms(smsModel)
    }
     */


    /*
    private fun receiveSmsUi(number: TextFieldValue, text: TextFieldValue) {
        viewModel.receivedMessage.observe(this@MainActivity) { (_, _) ->
        val phoneNumber = number.text
        val message = text.text
        val smsModel = SmsModel(phoneNumber = phoneNumber, message = message)
        viewModel.receiveSms(smsModel)
        }
    }
    */


    /*
    @Composable
    fun OutlinedTextFieldComponent(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
        label: String,
        keyboardOptions: KeyboardOptions,
        modifier: Modifier = Modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            keyboardOptions = keyboardOptions,
            modifier = modifier.fillMaxWidth()
        )
    }
    */

    /*
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun SmsScreen() {
        val context = LocalContext.current
        var number by remember { mutableStateOf(TextFieldValue()) }
        var text by remember { mutableStateOf(TextFieldValue()) }

        sendMessageIfPermissionsGranted(context, this@MainActivity)
        receiveSmsUi(number, text)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextFieldComponent(
                value = number,
                onValueChange = { number = it },
                label = "Number",
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextFieldComponent(
                value = text,
                onValueChange = { text = it },
                label = "Message",
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),

                )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    sendSms(number, text)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Send")
            }
        }
    }

    */

}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmsComposeAppTheme {
        //SmsScreen()
    }
}

 */
