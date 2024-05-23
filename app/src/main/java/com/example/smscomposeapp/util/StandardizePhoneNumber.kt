package com.example.smscomposeapp.util

object StandardizePhoneNumber {

    fun standardizePhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith("+98")) {
            phoneNumber.replace("+98", "0")
        } else if (phoneNumber.startsWith("0098")) {
            phoneNumber.replace("0098", "0")
        } else {
            phoneNumber
        }
    }

}