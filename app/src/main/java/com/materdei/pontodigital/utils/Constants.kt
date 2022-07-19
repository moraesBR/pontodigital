package com.materdei.pontodigital.utils

class Constants {
    companion object{
        val LOGIN_EMPTY_DATA = "Email e/ou Senha vazias. Por favor, informe os seus dados."
        val LOGIN_INVALID_DATA = "Por favor, informe email e/ou senha válidos."
        val LOGIN_ERRO = "Autenticação falhou."
        val LOGIN_CREATE_USER_SUCCESS = "Usuário criado com sucesso"
        val LOGIN_CREATE_USER_FAILED  = "Não foi possível criar o usuário"
        val LOGIN_DELETE_OK = "Conta deletada!"
        val LOGIN_DELETE_FAILED = "Falha ao deletar: usuário não está logado"
        val LOGIN_UPDATE_OK = "Dados do usuário foram atualizados."
        val LOGIN_REAUTHENTICATE_OK = "Usuário logado"
        val LOGIN_NO_INTERNET = "Não há internet no momento. Por favor, conecte-se a internet."
        val SHAREDPREFERENCES = "sharedpreferences mater dei"
        val SHAREDPREFERENCES_LOGIN = "LOGIN_DATA"
        val SHAREDPREFERENCES_LOGIN_EMAIL = "email"
        val SHAREDPREFERENCES_LOGIN_PASSWORD = "password"
        val SHAREDPREFERENCES_LOGIN_SAVE = "save login"
    }
}