package com.materdei.pontodigital.model

/* TODO 001.3: Criar a classe de dados para vincular nos arquivos de layout */
data class User(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var phonenumber: String = ""
)
