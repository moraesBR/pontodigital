package com.materdei.pontodigital.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.materdei.pontodigital.dto.NetworkStatus
import com.materdei.pontodigital.repository.AuthenticationRepository
import com.materdei.pontodigital.repository.NetworkStatusHelper

/* TODO 003.03: criar a ViewModel para tratar a comunicação da atividade com o status de rede. */
class NetworkStatusViewModel(app: Application) : AndroidViewModel(app) {

    private val networkStatus = NetworkStatusHelper(app.baseContext)
    fun getNetworkStatus() = networkStatus

}