package com.example.login


object SessionManager {
    var token: String? = null
    var identity: String? = null

    fun isLoggedIn(): Boolean {
        return !token.isNullOrEmpty()
    }

    fun clear() {
        token = null
        identity = null
    }
    fun getFirstLetter(name: String?): String {
        return name
            ?.trim()
            ?.firstOrNull()
            ?.uppercase()
            ?: "?"
    }


}

