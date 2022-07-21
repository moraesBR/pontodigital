package com.materdei.pontodigital.dto

/* TODO 003.07: Criar (ou ajustar o existente) o dado de usuário de acordo com o Firebase
                Authentication */
/* 001.3: Classe de dados usada para vincular os dados ao layout xml */
data class Authentication(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var phonenumber: String = "",
    var photouri: String = "",
    var uid: String = ""
)
