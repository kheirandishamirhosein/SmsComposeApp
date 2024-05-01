package com.example.smscomposeapp.infrastructure

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smscomposeapp.data.imp.ImpSmsSendRepository
import com.example.smscomposeapp.data.models.SmsModel

class MainViewModel : ViewModel() {
    private val smsSender = ImpSmsSendRepository()

    private val _receivedMessage = MutableLiveData<Pair<String?, String>>()
    val receivedMessage: LiveData<Pair<String?, String>> = _receivedMessage



    fun sendSms(smsModel: SmsModel) {
        smsSender.sendSms(smsModel)
    }

    fun receiveSms(smsModel: SmsModel) {
        _receivedMessage.value = Pair(smsModel.phoneNumber, smsModel.message)
    }
}