package com.materdei.pontodigital.dto

/* TODO 003.05: Criar (ou ajustar o existente) o dado de usuário de acordo com
               Firebase Authentication */
/* TODO 001.3: Criar a classe de dados para vincular nos arquivos de layout */
data class Authentication(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var phonenumber: String = "",
    var photouri: String = "",
    var uid: String = ""
)
