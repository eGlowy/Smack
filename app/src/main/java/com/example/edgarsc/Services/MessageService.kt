package com.example.edgarsc.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.edgarsc.Controller.App
import com.example.edgarsc.Model.Channel
import com.example.edgarsc.Model.Message
import com.example.edgarsc.Utilities.URL_GET_CHANNELS
import com.example.edgarsc.Utilities.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {
    val messages = ArrayList<Message>()
    val channels = ArrayList<Channel>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelsRequest =
            object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->

                try {

                    for (x in 0 until response.length()) {
                        val channel = response.getJSONObject(x)
                        val name = channel.getString("name")
                        val chanDesc = channel.getString("description")
                        val chanId = channel.getString("_id")

                        val newChannel = Channel(name, chanDesc, chanId)
                        this.channels.add(newChannel)
                    }
                    complete(true)

                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }

            }, Response.ErrorListener {
                Log.d("ERROR", "Could not retrieve channels")
                complete(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                    return headers
                }
            }
        App.prefs.requestQueue.add(channelsRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val url = "$URL_GET_MESSAGES$channelId"
        val messageRequest =
                object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
                    clearMessages()
                    try {

                        for (x in 0 until response.length()) {
                            val message = response.getJSONObject(x)

                            val id = message.getString("_id")
                            val msgBody = message.getString("messageBody")
                            val channelId = message.getString("channelId")
                            val userName = message.getString("userName")
                            val userAvatar = message.getString("userAvatar")
                            val userAvatarColor = message.getString("userAvatarColor")
                            val timeStamp = message.getString("timeStamp")

                            val newMessage = Message(msgBody, userName, channelId, userAvatar,userAvatarColor,id,timeStamp)
                            this.messages.add(newMessage)
                        }
                        complete(true)

                    } catch (e: JSONException) {
                        Log.d("JSON", "EXC:" + e.localizedMessage)
                    }
                }, Response.ErrorListener {
                    Log.d("ERROR", "Could not retrieve messages")
                    complete(false)
                }) {
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                        return headers
                    }
                }
        App.prefs.requestQueue.add(messageRequest)
    }

    fun clearMessages(){
        messages.clear()
    }

    fun clearChannels(){
        channels.clear()
    }
}