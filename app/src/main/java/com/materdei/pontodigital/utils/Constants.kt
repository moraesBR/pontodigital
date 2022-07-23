package com.materdei.pontodigital.utils

class Constants {
    companion object{
        val LOGIN_CREATE_FAILED  = "Não foi possível criar o usuário"
        val LOGIN_CREATE_SUCCESS = "Usuário criado com sucesso"
        val LOGIN_DATA_EMPTY = "Email e/ou Senha vazias. Por favor, informe os seus dados."
        val LOGIN_DATA_INVALID = "Por favor, informe email e/ou senha válidos."
        val LOGIN_DELETE_FAILED = "Falha ao deletar: usuário não está logado"
        val LOGIN_DELETE_SUCCESS = "Conta deletada!"
        val LOGIN_ERRO = "Autenticação falhou."
        val LOGIN_REAUTHENTICATE_OK = "Usuário logado"
        val LOGIN_UPDATE_OK = "Dados do usuário foram atualizados."
        val LOGIN_NO_INTERNET = "Não há internet no momento. Por favor, conecte-se a internet."
        val REGISTERVIEWMODEL_REGISTERS_COLLECTION = "registers"
        val REGISTERVIEWMODEL_WORKDAY_COLLECTION = "workday"
        val REGISTERVIEWMODEL_ROOT_COLLECTION = "timeclock"
        val SHAREDPREFERENCES = "sharedpreferences mater dei"
        val SHAREDPREFERENCES_LOGIN = "LOGIN_DATA"
        val SHAREDPREFERENCES_LOGIN_EMAIL = "email"
        val SHAREDPREFERENCES_LOGIN_PASSWORD = "password"
        val SHAREDPREFERENCES_LOGIN_SAVE = "save login"
    }
}