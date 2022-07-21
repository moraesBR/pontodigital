package com.materdei.pontodigital.model

/* 001.3: Classe de dados usada para vincular os dados ao layout xml */
data class User(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var phonenumber: String = ""
)
