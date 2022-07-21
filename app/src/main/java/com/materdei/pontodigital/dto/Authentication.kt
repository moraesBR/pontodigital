package com.materdei.pontodigital.dto

/* 001.3: Classe de dados usada para vincular os dados ao layout xml */
/* 003.07: Classe de dados de usuário de acordo com o Firebase Authentication */
data class Authentication(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var phonenumber: String = "",
    var photouri: String = "",
    var uid: String = ""
)
