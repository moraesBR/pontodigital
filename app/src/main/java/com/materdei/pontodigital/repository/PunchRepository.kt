package com.materdei.pontodigital.repository

import com.materdei.pontodigital.di.FirebaseConnection
import com.materdei.pontodigital.domain.model.DataModel.Punch
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.domain.repository.DataRepository
import com.materdei.pontodigital.utils.Constants.Companion.REGISTERVIEWMODEL_REGISTERS_COLLECTION
import com.materdei.pontodigital.utils.Constants.Companion.REGISTERVIEWMODEL_ROOT_COLLECTION
import kotlinx.coroutines.flow.Flow

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

}