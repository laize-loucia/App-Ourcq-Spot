package com.ourcqspot.client.screens.home


//package com.example.myagenda_v0_0


import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
//import.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
//import com.ourcqspot.client.R
//import com.ourcqspot.client.ui.t androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import java.time.YearMonth
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.ourcqspot.client.data.Event
import com.ourcqspot.client.data.EventRepository
import com.ourcqspot.client.networking.ClientHandler


/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgendaScreenContent()
        }
    }
}*/

@Composable
fun AgendaScreenContent() {
    Column {
        MonthNavigator()
        val events by EventRepository.selectedEvents
        Log.d("OLD_PRINT", "EVENTS: $events")
        EventsList(events = events)
    }
}

@Composable
fun EventsList(events: List<Event>) {
    Column {
        events.forEach({ event ->
            EventItem(event)
        })
    }
}

@Composable
fun EventItem(event: Event) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val label = event.label
        val spot = event.spot
        val startDate = event.start_date
        val endDate = event.end_date
        Column {
            Text("Début : $label")
            Text("Début : $spot")
        }
        Column {
            Text("Début : $startDate")
            Text("Début : $endDate")
        }
    }
}

@Composable
fun MonthNavigator() {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var currentMonthIndex by remember { mutableStateOf(0) }

    //verticalArrangement = Arrangement.Top
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Navigation entre les mois
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { if (currentMonthIndex > 0) currentMonthIndex-- },
                enabled = currentMonthIndex > 0
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
            }

            Text(text = "${months[currentMonthIndex]} 2025", fontSize = 20.sp)

            IconButton(
                onClick = { if (currentMonthIndex < months.size - 1) currentMonthIndex++ },
                enabled = currentMonthIndex < months.size - 1
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                Text(text = day, fontSize = 16.sp)
            }
        }

        DaysGrid(currentMonthIndex)
    }
}

@SuppressLint("NewApi")
fun getDaysInMonth(monthIndex: Int): List<Int> {
    val yearMonth = YearMonth.of(2024, monthIndex + 1)
    return (1..yearMonth.lengthOfMonth()).toList()
}

@Composable
fun DaysGrid(monthIndex: Int) {
    val days = getDaysInMonth(monthIndex)
    val eventDays = listOf(9, 15, 24, 27) // Jours à souligner en rouge avec la bdd

    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // 7 colonnes pour les jours de la semaine
        modifier = Modifier.padding(16.dp)
    ) {
        items(days) { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = day.toString(),
                    fontSize = 18.sp,
                    color = if (eventDays.contains(day)) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Black,
                    modifier = Modifier.clickable {
                        val monthStr = (monthIndex + 1).toString().padStart(2, '0')
                        val dayStr = day.toString().padStart(2, '0')
                        val dateStr = "2025-$monthStr-$dayStr"
                        Log.d("CLICK", "Clicked on date $dateStr")
                        val requestApi_getAllEventsByDate = "3;$dateStr"
                        ClientHandler.getInstance().requestMessageToServer = requestApi_getAllEventsByDate
                    },
                )
                if (eventDays.contains(day)) {
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .width(16.dp)
                            .background(androidx.compose.ui.graphics.Color.Red)
                    )
                }
            }
        }
    }
}





