package com.materdei.pontodigital.utils

import java.util.*

class Constants {
    companion object{
        val BIOMETRIC_KEY = UUID.randomUUID().toString()
        val BIOMETRIC_TITLE = "Biometric login for my app"
        val BIOMETRIC_SUBTITLE = "Log in using your biometric credential"
        val BIOMETRIC_NEGATIVE_BUTTON = "Use account password"
        val BIOMETRIC_FAILED = "Biometria incorreta"
        val LOCATION_MAX_DISTANCE = 10.0F
        val LOCATION_PERMISSION_REQUEST_CODE = 2022
        val LOCATION_REFERENCE_LAT = -1.3566939  // My Home
        val LOCATION_REFERENCE_LON = -48.3934582  // My Home
        //val LOCATION_REFERENCE_LAT = -1.431333  // Mater Dei
        //val LOCATION_REFERENCE_LON = -48.475374 // Mater Dei
        val LOCATION_REQUIREMENT = "Turn on your Internet or/and GPS, please"
        val LOCATION_TIME: Long = 60000
        val LOCATION_TOO_DISTANCE = "Too distance from Mater Dei"
        val LOCATION_NOT_FOUND = "Sua localização não foi encontrada"
        val LOCATION_PERMISSION_PROBLEM = "Sem permissões para usar geolocalização"
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
        val PUNCHADAPTER_USERNAME_EMPTY = "Sem nome"
    }
}