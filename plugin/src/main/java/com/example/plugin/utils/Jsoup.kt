package com.example.plugin.utils

import android.util.Log
import kotlinx.coroutines.delay
import org.jsoup.Connection
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.SocketException
import javax.net.ssl.SSLHandshakeException

suspend fun Connection.autoReconnectionGet(reconnectTime: Int = 3, reconnectDelay: Long = 250, isUesProxy: Boolean = false): Document? =
    autoReconnection(
        reconnectTime,
        reconnectDelay,
        isUesProxy
    ) {
        this.get()
    }

suspend fun Connection.autoReconnectionPost(reconnectTime: Int = 3, reconnectDelay: Long = 250, isUesProxy: Boolean = false): Document? =
    autoReconnection(
        reconnectTime,
        reconnectDelay,
        isUesProxy
    ) {
        this.post()
    }

private suspend fun autoReconnection(
    reconnectTime: Int = 3,
    reconnectDelay: Long = 250,
    isUesProxy: Boolean = false,
    block: suspend () -> Document?
): Document? {
    try {
        block.invoke()?.let {
            return it
        }
    } catch (e: HttpStatusException) {
        Log.e("Network", "failed to get data from ${e.url}, last reconnection times: $reconnectTime")
        e.printStackTrace()
        retry(reconnectTime, reconnectDelay) {
            autoReconnection(reconnectTime - 1, reconnectDelay, isUesProxy, block)
        }
    } catch (e: SocketException) {
        Log.e("Network", "failed to get data, last reconnection times: $reconnectTime")
        e.printStackTrace()
        retry(reconnectTime, reconnectDelay) {
            autoReconnection(reconnectTime - 1, reconnectDelay, isUesProxy, block)
        }
    } catch (e: SSLHandshakeException) {
        Log.e("Network", "failed to get data, last reconnection times: $reconnectTime")
        e.printStackTrace()
        retry(reconnectTime, reconnectDelay) {
            autoReconnection(reconnectTime - 1, reconnectDelay, isUesProxy, block)
        }
    } catch (e: IOException) {
        Log.e("Network", "failed to get data, last reconnection times:  $reconnectTime")
        e.printStackTrace()
        retry(reconnectTime, reconnectDelay) {
            autoReconnection(reconnectTime - 1, reconnectDelay, isUesProxy, block)
        }
    }
    return null
}

private suspend fun retry(reconnectTimes: Int, reconnectDelay: Long, block: suspend () -> Document?): Document? {
    if (reconnectTimes < 1) return null
    delay(reconnectDelay)
    return block.invoke()
}