package com.materdei.pontodigital.domain.repository

import com.materdei.pontodigital.domain.model.DataModel
import com.materdei.pontodigital.domain.model.Response
import kotlinx.coroutines.flow.Flow

/* 004.13: Interface para padronizar os repositórios */
interface DataRepository <T: DataModel> {

    suspend fun get(): Flow<Response<List<T>>>

    suspend fun add(data: T): Flow<Response<Void?>>

    suspend fun delete(key: String): Flow<Response<Void?>>

}