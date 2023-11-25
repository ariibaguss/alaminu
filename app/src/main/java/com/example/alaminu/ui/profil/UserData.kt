package com.example.alaminu.ui.profil

import java.io.Serializable

class UserData(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val no_wa: String,
    val jenis_kelamin: String,
    val image: String
) : Serializable
