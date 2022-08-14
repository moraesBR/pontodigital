package com.materdei.pontodigital.repository

import com.materdei.pontodigital.di.FirebaseConnection
import com.materdei.pontodigital.domain.model.DataModel.Punch
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.domain.model.Response.Error
import com.materdei.pontodigital.domain.model.Response.Loading
import com.materdei.pontodigital.domain.model.Response.Success
import com.materdei.pontodigital.domain.repository.DataRepository
import com.materdei.pontodigital.utils.Constants.Companion.REGISTERVIEWMODEL_REGISTERS_COLLECTION
import com.materdei.pontodigital.utils.Constants.Companion.REGISTERVIEWMODEL_ROOT_COLLECTION
import com.materdei.pontodigital.utils.PunchCard
import com.materdei.pontodigital.utils.getDate
import com.materdei.pontodigital.utils.getTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* 004.14: Repositório para obter os dados de punch que são encapsulados em livedata via Flow. */
class PunchRepository : DataRepository <Punch> {

    private val pathCollection by lazy {
            REGISTERVIEWMODEL_ROOT_COLLECTION +
            "/${FirebaseConnection.firebaseUser!!.uid}/" +
            REGISTERVIEWMODEL_REGISTERS_COLLECTION
    }

    override suspend fun get(): Flow<Response<List<Punch>>> =
        FirebaseConnection.getDocuments(pathCollection)

    override suspend fun add(data: Punch): Flow<Response<Void?>> =
        FirebaseConnection.addDocument(pathCollection,data)

    override suspend fun delete(key: String): Flow<Response<Void?>> =
        FirebaseConnection.deleteDocument(pathCollection,key)

    /* TODO 005.10: Gera a resposta correspondente para obtenção do último e próximo ponto */
    suspend fun lastAndNext(): Flow<Response<Pair<Punch,Punch>>> = get().map { result ->
        when(result){
            is Loading -> Loading
            is Success -> {
                val today = getDate()
                val now = getTime()
                val lastpunch = result.data.last()

                val punchCard = if(lastpunch.date == today){
                    when (lastpunch.punch) {
                        PunchCard.IN.value -> PunchCard.OUT.value
                        else -> PunchCard.IN.value
                    }
                }
                else {
                    PunchCard.IN.value
                }

                val newPunch = Punch(today, now, punchCard)

                if (newPunch == lastpunch)
                    Error("Ponto já registrado. Aguarde alguns instantes para registrar outro ponto.")
                else
                    Success(Pair(lastpunch,newPunch))
            }
            is Error -> Error(result.msg)
        }
    }
}