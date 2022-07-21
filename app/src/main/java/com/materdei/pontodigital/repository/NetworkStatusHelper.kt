package com.materdei.pontodigital.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import com.materdei.pontodigital.dto.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

/* 003.02: Classe que monitora o status da rede. */
class NetworkStatusHelper (private val context: Context) : LiveData<NetworkStatus>() {

    /* tenta estabelecer um conexão qualquer para verificar se a rede está online ou não */
    private object InernetAvailablity {
        fun check() : Boolean {
            return try {
                val socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8",53))
                socket.close()
                true
            } catch ( e: Exception){
                e.printStackTrace()
                false
            }
        }

    }

    /* O gerenciador de conectividade dever ser sempre buscado para evitar referências quebradas */
    val connMgr: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /* armazena as interfaces de rede disponíveis. Por alguma razão, o livedata dispara várias
    * chamadas simultaneamente, resultanto em interface repetidas dentro do conjunto causando
    * problemas. Por tratar isso, a coleção Set é foi a solução escolhida, pois esta estrura de
    * dados não permite elementos repetidos no conjunto. */
    /* A razão para múltiplos está no fato dos fragments saírem do foreground antes de receber
    * suas respostas às chamadas que fez ao livedata. Estas chamadas são passadas para o componente
    * que está em foreground no momento. Um outra solução seria aplicar chamadas com coroutines. */
    val valideNetworkConnections: MutableSet<Network> = mutableSetOf()

    /* atualiza o status de rede */
    private fun announceStatus(){
        Log.i("NETWORK",valideNetworkConnections.toString())
        if(valideNetworkConnections.isNotEmpty()) {
            postValue(NetworkStatus.Available)
        }
        else {
            postValue(NetworkStatus.Unavailable)
        }
    }

    /* anuncia se há interface com internet disponível */
    private fun determineInternetAccess(network: Network) {
        CoroutineScope(Dispatchers.IO).launch{
            if (InernetAvailablity.check()){
                withContext(Dispatchers.Main){
                    valideNetworkConnections.add(network)
                    announceStatus()
                }
            }
        }
    }

    /* callback que lida com a disponibilidade das interfaces de redes */
    private val connectionManagerCallback = object : ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            /* identifica as interfaces de rede e checa se há capacidade de internet */
            val networkCapabilities = connMgr.getNetworkCapabilities(network)
            val hasNetworkConnection = networkCapabilities?.let {
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } ?: false

            if (hasNetworkConnection){
                determineInternetAccess(network)
            }
        }

        /* remove as interfaces de redes que foram desligadas ou perderam sinal */
        override fun onLost(network: Network) {
            super.onLost(network)
            valideNetworkConnections.remove(network)
            announceStatus()
        }

        /* filtra as interfaces de redes que possuem internet e conexão válida. */
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
                determineInternetAccess(network)
            }
            else {
                valideNetworkConnections.remove(network)
            }

            announceStatus()
        }
    }

    /* registra o monitoramento de rede */
    override fun onActive() {
        super.onActive()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        connMgr.registerNetworkCallback(networkRequest,connectionManagerCallback)
    }

    /* remove o monitoramento de rede */
    override fun onInactive() {
        super.onInactive()
        connMgr.unregisterNetworkCallback(connectionManagerCallback)
    }

}