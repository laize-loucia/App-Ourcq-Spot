package com.ourcqspot.client.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Event(
    val id: Int,
    val label: String,
    val description: String,
    val start_date: String,
    val end_date: String,
    val spot: String,
    val price: Double
)

object EventRepository {
    var selectedEvents = mutableStateOf<List<Event>>(emptyList())
}

fun setSelectedEventsData(json: String) {
    Log.d("JSON", "> 1 " + json)
    val newEvents = Json.decodeFromString<List<Event>>(json)
    Log.d("JSON", "> 2 " + newEvents.toString())
    EventRepository.selectedEvents.value = newEvents
    Log.d("JSON", "> 3 " + EventRepository.selectedEvents.value.toString())
}