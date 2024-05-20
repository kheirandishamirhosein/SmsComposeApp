package com.example.smscomposeapp.infrastructure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.doman.SmsSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SmsViewModel(private val smsUserDao: SmsUserDao, private val smsSender: SmsSender) :
    ViewModel() {
    private val _smsModels = MutableStateFlow<List<SmsModel>>(emptyList())
    val smsModels: StateFlow<List<SmsModel>> = _smsModels

    init {
        fetchSmsFromDatabase()
    }

    private fun fetchSmsFromDatabase() {
        viewModelScope.launch {
            smsUserDao.getAllSmsMessages().collect { smsList ->
                _smsModels.value = smsList
            }
        }
    }

    fun sendSms(smsModel: SmsModel) {
        viewModelScope.launch {
            smsUserDao.upsertSmsUserModel(smsModel)
            _smsModels.value += smsModel
            smsSender.sendSms(smsModel)
        }

    }

    fun receiveSms(smsModel: SmsModel) {
        viewModelScope.launch {
            smsUserDao.upsertSmsUserModel(smsModel)
            _smsModels.value += smsModel
        }

    }

}