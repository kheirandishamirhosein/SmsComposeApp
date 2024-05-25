package com.example.smscomposeapp.infrastructure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smscomposeapp.data.dao.SmsUserDao
import com.example.smscomposeapp.data.models.SmsModel
import com.example.smscomposeapp.doman.SmsSender
import com.example.smscomposeapp.util.StandardizePhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SmsViewModel(
    private val smsUserDao: SmsUserDao,
    private val smsSender: SmsSender
) : ViewModel() {

    private val _smsModels = MutableStateFlow<List<SmsModel>>(emptyList())
    val smsModels: StateFlow<List<SmsModel>> = _smsModels

    private val _lastSmsUser = MutableStateFlow<List<SmsModel>>(emptyList())
    val lastSmsUser: StateFlow<List<SmsModel>> = _lastSmsUser

    private val _selectedUserSmsModels = MutableStateFlow<List<SmsModel>>(emptyList())
    val selectedUserSmsModels: StateFlow<List<SmsModel>> = _selectedUserSmsModels

    init {
        standardizeAllPhoneNumbers(smsUserDao)
        fetchSmsFromDatabase()
        fetchGetLastSmsUser()
        //TODO: Test Delete
        //fetchDeleteAmirPhoneNumber()
    }

    private fun standardizeAllPhoneNumbers(smsUserDao: SmsUserDao) {
        viewModelScope.launch {
            val allSms = smsUserDao.getAllSmsMessages().first()
            allSms.forEach { smsModel ->
                val standardizedPhoneNumber =
                    StandardizePhoneNumber.standardizePhoneNumber(phoneNumber = smsModel.phoneNumber)
                if (standardizedPhoneNumber != smsModel.phoneNumber) {
                    val updateSmsModel = smsModel.copy(phoneNumber = standardizedPhoneNumber)
                    smsUserDao.upsertSmsUserModel(updateSmsModel)
                }
            }
        }
    }

    private fun fetchGetLastSmsUser() {
        viewModelScope.launch {
            smsUserDao.getLastSmsMessage().collect { smsList ->
                _lastSmsUser.value = smsList
            }
        }
    }

    // For save messages chat screen in database
    private fun fetchSmsFromDatabase() {
        viewModelScope.launch {
            smsUserDao.getAllSmsMessages().collect { smsList ->
                _smsModels.value = smsList
            }
        }
    }

    // For send messages
    fun sendSms(smsModel: SmsModel) {
        viewModelScope.launch {
            val standardizedSmsModel = smsModel.copy(
                phoneNumber = StandardizePhoneNumber.standardizePhoneNumber(smsModel.phoneNumber)
            )
            smsUserDao.upsertSmsUserModel(standardizedSmsModel)
            _smsModels.value += standardizedSmsModel
            smsSender.sendSms(standardizedSmsModel)
        }
    }

    // For receive messages
    fun receiveSms(smsModel: SmsModel) {
        viewModelScope.launch {
            val standardizedSmsModel = smsModel.copy(
                phoneNumber = StandardizePhoneNumber.standardizePhoneNumber(smsModel.phoneNumber)
            )
            smsUserDao.upsertSmsUserModel(standardizedSmsModel)
            _smsModels.value += standardizedSmsModel
        }
    }

    // For delete messages
    fun deleteSmsChat(smsModel: SmsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            smsUserDao.deleteSmsById(smsModel.id)
        }
    }

    //TODO: DELETE Phone number for Test
    private fun fetchDeleteAmirPhoneNumber() {
        viewModelScope.launch {
            smsUserDao.deleteSmsByAmirPhoneNumber("09365911325")
        }
    }

}