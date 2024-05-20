package com.example.smscomposeapp.infrastructure

import androidx.lifecycle.ViewModel
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.imp.ImpSmsSendRepository
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.doman.SmsSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SmsViewModel(private val smsUserDao: SmsUserDao , private val smsSender: SmsSender) : ViewModel() {
    //private val smsSender = ImpSmsSendRepository()
    //private val smsList = mutableListOf<SmsModel>()

    //private val _receivedMessages = MutableStateFlow<List<SmsModel>>(emptyList())
    //val receivedMessages: StateFlow<List<SmsModel>> = _receivedMessages

    //val smsModels: List<SmsModel>
    //    get() = smsList.toList()
    private val _smsModels = MutableStateFlow<List<SmsModel>>(emptyList())
    val smsModels: StateFlow<List<SmsModel>> = _smsModels

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
        //smsList.add(smsModel)
        //smsSender.sendSms(smsModel)
        _smsModels.value += smsModel
        smsSender.sendSms(smsModel)
    }


    fun receiveSms(smsModel: SmsModel) {
        //_receivedMessages.value += smsModel
        _smsModels.value += smsModel
    }
}