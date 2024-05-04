package com.example.smscomposeapp.infrastructure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.imp.ImpSmsSendRepository
import com.example.smscomposeapp.data.models.SmsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SmsViewModel(private val smsUserDao: SmsUserDao) : ViewModel() {
    private val smsSender = ImpSmsSendRepository()

    private val _receivedMessage = MutableLiveData<Pair<String?, String>>()
    val receivedMessage: LiveData<Pair<String?, String>> = _receivedMessage


    //Room DB
    /*
    fun upsertSmsUserData(phoneNumber: String, message: String) {
        val smsModel = SmsModel(phoneNumber = phoneNumber, message = message)
        viewModelScope.launch {

        }
    }

    fun getAllSmsList(): Flow<List<SmsModel>> {
        return smsUserDao.getAllSmsRecord()
    }
    */

    fun sendSms(smsModel: SmsModel) {
        smsSender.sendSms(smsModel)
    }

    fun receiveSms(smsModel: SmsModel) {
        _receivedMessage.value = Pair(smsModel.phoneNumber, smsModel.message)
    }
}