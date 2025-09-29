package com.ourcqspot.client.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.ourcqspot.client.R
import com.ourcqspot.client.composables.MapFiltersMenu
import com.ourcqspot.client.composables.buildIconPoiCulture
import com.ourcqspot.client.composables.buildIconPoiEvent
import com.ourcqspot.client.composables.buildIconUserPosition
import com.ourcqspot.client.data.POI
import com.ourcqspot.client.data.POI_Categories
import com.ourcqspot.client.ui.theme.Blue1
import com.ourcqspot.client.ui.theme.Orange1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.Icon
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import java.net.HttpURLConnection
import java.net.URL

/*@SuppressLint("NewApi")
@Composable
fun MapScreenContent() {
    Box (
        Modifier
            .clipToBounds() // forbids overflow
            .fillMaxSize(),
        Alignment.Center
    ) {
        val context = LocalContext.current
        val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

        var userLatLng by remember {
            mutableStateOf<LatLng?>(null)
        }

        val locationRequest = remember {
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L).build()
        }
        val locationCallback = remember {
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    Log.d("LOCATION", "Location result received")
                    result.lastLocation?.let { location ->
                        userLatLng = LatLng(location.latitude, location.longitude)
                        Log.d("LOCATION", "Updated location: $userLatLng")
                    }
                }
            }
        }

        // Lance les updates une seule fois
        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("LOCATION", "Permission accordée")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }

        // Affiche la carte quand la position est disponible (sinon spinner ou fallback)
        userLatLng?.let {
            MapLibreView(userLatLng = it)
            MapFiltersMenu(
                Modifier
                    .align(Alignment.TopEnd)   // Important de le faire ici pour être dans BoxScope
                    .padding(top = 25.dp, bottom = 200.dp),
            )
            /*ModalBottomSheet(onDismissRequest = { /* Executed when the sheet is dismissed */ }) {
                // Sheet content
                Text("Ciné 104")
                Spacer(Modifier.height(10.dp))
                Text("Votre cinéma du quartier pour le cinéma actuel et les films cultes pour tous les publics.")
                Spacer(Modifier.height(10.dp))
                Text("Horaires :")
            }*/
        } ?: Text("Chargement de la position...")
    }
}*/














@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MapScreenContent() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("PERMISSION", "Permission accordée")
                // On pourrait déclencher ici une logique si besoin
            } else {
                Log.d("PERMISSION", "Permission refusée")
            }
        }
    )
    val mapController = remember { mutableStateOf<MapLibreMap?>(null) }

    val key = "L59S7L80jAg6X9qIJYUM"
    val mapId = remember { mutableStateOf("streets-v2") }
    val styleUrl = remember { derivedStateOf { "https://api.maptiler.com/maps/${mapId.value}/style.json?key=$key" } }

    val cameraFollowsLocation = remember { mutableStateOf(false) }

    val showBottomSheet = remember { mutableStateOf(false) }
    val selectedPoi = remember { mutableStateOf<POI?>(null) }

    Box(
        Modifier
            .clipToBounds()
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(Unit) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                Log.d("PERMISSION", "Permission déjà accordée")
            }
        }

        GeolocatedMapLibreView(
            styleUrl = styleUrl.value,
            cameraFollowsLocation = cameraFollowsLocation.value,
            onMapReady = { mapController.value = it },
            showBottomSheet = showBottomSheet,
            selectedPoi = selectedPoi,
        )

        MapFiltersMenu(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 25.dp, bottom = 200.dp),
        )

        if (showBottomSheet.value) {
            Log.d("MAP", "Showing bottom sheet")
            ModalBottomSheet (
                onDismissRequest = { showBottomSheet.value = false }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Column {
                            Text(
                                text = selectedPoi.value?.name ?: "Point d’intérêt",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(Modifier.width(7.5.dp))
                            Text(
                                text = "Catégorie : ${selectedPoi.value?.category}"
                                    ?: "Aucune catégorie disponible",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${selectedPoi.value?.latitude}, ${selectedPoi.value?.longitude}"
                                    ?: "Aucune position disponible",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (selectedPoi.value?.illustrationPainterResource != null) {
                            Spacer(Modifier.width(15.dp))
                            Box(
                                Modifier
                                    .defaultMinSize(minWidth = 200.dp)
                                    .height(200.dp)) {
                                Image(
                                    painterResource(selectedPoi.value?.illustrationPainterResource!!),
                                    contentDescription = "Illustration de [${selectedPoi.value?.name}]",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = selectedPoi.value?.description
                            ?: "Aucune description disponible",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(175.dp))
                }
            }
        }
        Column (
            Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        Log.d("MAP", "Changing style")
                        mapId.value = if (mapId.value == "streets-v2") "winter-v2" else "streets-v2"
                    },
                    shape = RoundedCornerShape(percent = 100),
                    modifier = Modifier.size(75.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = (if (mapId.value == "streets-v2") Orange1 else Blue1)
                    )
                ) {
                    AnimatedContent (
                        targetState = mapId.value,
                        transitionSpec = {
                            slideInVertically (
                                animationSpec = tween(durationMillis = 300)
                            ) { height -> height } with
                                    slideOutVertically (
                                        animationSpec = tween(durationMillis = 300)
                                    ) { height -> -height }
                        }
                    ) { currentMapId ->
                        if (currentMapId == "streets-v2") {
                            Icon(
                                painter = painterResource(R.drawable.icon_rocketship),
                                contentDescription = "Style Streets",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.icon_map),
                                contentDescription = "Style Winter",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                /*Button(onClick = {
                    Log.d("MAP", "Changing style")
                    mapId.value = if (mapId.value == "streets-v2") "winter-v2" else "streets-v2"
                },
                    shape = RoundedCornerShape(percent = 100),
                    modifier = Modifier
                        .size(75.dp)
                ) {
                    //Text("Changer de style")
                    if (mapId.value == "streets-v2") {
                        Icon(
                            painter = painterResource(R.drawable.icon_rocketship),
                            contentDescription = "Btn Guide/Game",
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icon_map),
                            contentDescription = "Btn Guide/Game",
                            tint = Color.White
                        )
                    }
                }*/
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED) {
                    Button(onClick = {
                        cameraFollowsLocation.value = !cameraFollowsLocation.value
                    },
                        shape = RoundedCornerShape(percent = 100),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFDFEFE)
                        ),
                        modifier = Modifier
                            .size(75.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_target),
                            contentDescription = "Btn Guide/Game",
                            tint = (if (cameraFollowsLocation.value) Orange1 else Blue1),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(100.dp))
        }
    }
}


@Composable
fun GeolocatedMapLibreView(
    styleUrl: String,
    cameraFollowsLocation: Boolean,
    onMapReady: (MapLibreMap) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    selectedPoi: MutableState<POI?>
) {
    Log.d("MAP", "GeolocatedMapLibreView() > Style asked: ${styleUrl}")
    Log.d("MAP", "GeolocatedMapLibreView() > Camera follows location: ${cameraFollowsLocation}")
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val userLocationMarker = remember { mutableStateOf<Marker?>(null) }
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }
    val mapRef = remember { mutableStateOf<MapLibreMap?>(null) }

    val userPosIcon = remember { mutableStateOf<Icon?>(null) }
    val poiEventIcon = remember { mutableStateOf<Icon?>(null) }
    val poiCultureIcon = remember { mutableStateOf<Icon?>(null) }

    LaunchedEffect(Unit) {
        Log.d("MAP", "GeolocatedMapLibreView() > Loading icons")
        val view = mapViewRef.value ?: return@LaunchedEffect
        Log.d("MAP", "GeolocatedMapLibreView() > MapView loaded")
//        val map = mapRef.value ?: return@LaunchedEffect
//        Log.d("MAP", "GeolocatedMapLibreView() > Map loaded")

        userPosIcon.value = buildIconUserPosition(view, context)
        poiEventIcon.value = buildIconPoiEvent(view, context)
        poiCultureIcon.value = buildIconPoiCulture(view, context)

        Log.d("MAP", "GeolocatedMapLibreView() > Icons loaded")
    }

    AndroidView(
        factory = { ctx ->
            MapLibre.getInstance(ctx)
            MapView(ctx).apply {
                mapViewRef.value = this
                getMapAsync { map ->
                    mapRef.value = map
                    onMapReady(map)
                    setMapStyleWithMarkers(
                        map = map,
                        styleUrl = styleUrl,
                        fusedLocationClient = fusedLocationClient,
                        context = context,
                        showBottomSheet = showBottomSheet,
                        selectedPoi = selectedPoi,
                        cameraFollowsLocation = cameraFollowsLocation,
                        userLocationMarker = userLocationMarker,
                        userPosIcon = userPosIcon,
                        poiEventIcon = poiEventIcon,
                        poiCultureIcon = poiCultureIcon
                    )
                }
            }
        },
        update = {
            mapRef.value?.let { map ->
                setMapStyleWithMarkers(
                    map = map,
                    styleUrl = styleUrl,
                    fusedLocationClient = fusedLocationClient,
                    context = context,
                    showBottomSheet = showBottomSheet,
                    selectedPoi = selectedPoi,
                    cameraFollowsLocation = cameraFollowsLocation,
                    userLocationMarker = userLocationMarker,
                    userPosIcon = userPosIcon,
                    poiEventIcon = poiEventIcon,
                    poiCultureIcon = poiCultureIcon
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


fun setMapStyleWithMarkers(
    map: MapLibreMap,
    styleUrl: String,
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    showBottomSheet: MutableState<Boolean>,
    selectedPoi: MutableState<POI?>,
    cameraFollowsLocation: Boolean,
    userLocationMarker: MutableState<Marker?>,
    userPosIcon: MutableState<Icon?>,
    poiEventIcon: MutableState<Icon?>,
    poiCultureIcon: MutableState<Icon?>
) {
    val markerDataMap = mutableMapOf<LatLng, POI>()

    map.setStyle(styleUrl) {
        map.setOnMarkerClickListener { marker ->
            val poi = markerDataMap[marker.position]
            if (poi != null) {
                Log.d("MAP", "Marker clicked: ${poi.name}")
                selectedPoi.value = poi
                showBottomSheet.value = true
            }

            true // indique que l’événement est consommé
        }

        poiEventIcon.value?.let {
            val poi = POI(
                id = 1,
                name = "Ciné 104",
                category = POI_Categories.CATEGORY_CULTURE_FR,
                description = "Cinéma et centre culturel de Pantin, à proximité de la gare de Pantin.",
                latitude = 48.922960,
                longitude = 2.398452,
                illustrationPainterResource = R.drawable.parcours_1
            )
            val poiLatLng = LatLng(poi.latitude, poi.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(poiLatLng)
                    .title(poi.name)
                    .icon(it)
            )
            Log.d("MAP", "Marker added : $poiLatLng")
            markerDataMap[poiLatLng] = poi
        }

        poiCultureIcon.value?.let {
            val poi = POI(
                id = 1,
                name = "Parc de la Villette",
                category = POI_Categories.CATEGORY_OUTSIDE_FR,
                description = "Parc étendu sur une grande superficie à la frontière Nord-Est de Paris, des deux côtés du canal de l'Ourcq, avoisinnant la Cité des Sciences.",
                latitude = 48.9629,
                longitude = 2.4865,
                illustrationPainterResource = R.drawable.parc_de_la_villette_1
            )
            val poiLatLng = LatLng(poi.latitude, poi.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(poiLatLng)
                    .title("Culture")
                    .icon(it)
            )
            Log.d("MAP", "Marker added : $poiLatLng")
            markerDataMap[poiLatLng] = poi
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                val position = LatLng(location.latitude, location.longitude)

                userLocationMarker.value?.remove()
                userLocationMarker.value = userPosIcon.value?.let {
                    Log.d("LOCATION", "Position: $position")
                    map.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title("Vous êtes ici")
                            .icon(it)
                    )
                }

                if (cameraFollowsLocation) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0))
                }
            }
        }

        Log.d("LOCATION", "Requesting location updates")
        val permissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d("MapLibre", "Permission granted? $permissionGranted")
        if (permissionGranted) {
            Log.d("LOCATION", "Permission accordée")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }
}












//@Composable
//fun MapScreenContent() {
//    Box (
//        Modifier
//            .clipToBounds() // forbids overflow
//            .fillMaxSize(),
//        Alignment.Center
//    ) {
//        val mapController = remember { mutableStateOf<MapLibreMap?>(null) }
//        // val key = "9zzN7nvsweC0cL7QY6k9" // Clé MapTiler API
//        val key = "L59S7L80jAg6X9qIJYUM"
//        val mapId = remember { mutableStateOf("winter-v2")}
//        val mapStyleUrl = remember { mutableStateOf("https://api.maptiler.com/maps/${mapId.value}/style.json?key=$key")}
//        Log.d("MAP", "> mapStyleUrl was updated: ${mapStyleUrl.value}")
//        GeolocatedMapLibreView(styleUrl=mapStyleUrl.value, { map ->
//            mapController.value = map
//        })
//        MapFiltersMenu(
//            Modifier
//                .align(Alignment.TopEnd)   // Important de le faire ici pour être dans BoxScope
//                .padding(top = 25.dp, bottom = 200.dp),
//        )
////        Button(onClick = {
////            val newStyleId = if (mapId.value == "streets-v2") "winter-v2" else "streets-v2"
////            mapId.value = newStyleId
////            mapStyleUrl.value = "https://api.maptiler.com/maps/${newStyleId}/style.json?key=$key"
////
////            mapController.value?.setStyle(mapStyleUrl.value) {
////                Log.d("MAP", ">> Nouveau style chargé")
////                // Tu peux réinitialiser des couches ou en recréer ici si besoin
////            }
////        }) {
////            Text("Changer de style")
////        }
//        Button(onClick = {
//            val newStyleId = if (mapId.value == "streets-v2") "winter-v2" else "streets-v2"
//            mapId.value = newStyleId
//            mapStyleUrl.value = "https://api.maptiler.com/maps/${newStyleId}/style.json?key=$key"
//
//            mapController.value?.setStyle(mapStyleUrl.value) {
//                Log.d("MAP", ">> Nouveau style chargé")
//                // Appelle ici la même fonction, avec les bons paramètres.
//                setupMapAfterStyleLoad(
//                    context = LocalContext.current,
//                    map = mapController.value!!,
//                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current),
//                    locationWasInitialized = remember { mutableStateOf(false) }, // ⚠️ Peut devoir être passé en paramètre global
//                    cameraFollowsLocation = remember { mutableStateOf(true) },
//                    zoomLevel = 15.0
//                )
//            }
//        }) {
//            Text("Changer de style")
//        }
//        // TestHttpRequest()
////        Button(onClick = {
////            if(mapId.value == "streets-v2") {
////                mapId.value = "winter"
////            } else {
////                mapId.value = "streets-v2"
////            }
////            Log.d("MAP", "> mapStyleUrl changed: ${mapStyleUrl.value}")
////        }) {
////            Text("Changer de carte")
////        }
//        /*ModalBottomSheet(onDismissRequest = { /* Executed when the sheet is dismissed */ }) {
//            // Sheet content
//            Text("Ciné 104")
//            Spacer(Modifier.height(10.dp))
//            Text("Votre cinéma du quartier pour le cinéma actuel et les films cultes pour tous les publics.")
//            Spacer(Modifier.height(10.dp))
//            Text("Horaires :")
//        }*/
//    }
//}
//
//@Composable
//fun GeolocatedMapLibreView(
//    styleUrl: String = "https://api.maptiler.com/maps/streets-v2/style.json?key=9zzN7nvsweC0cL7QY6k9",
//    onMapReady: (MapLibreMap) -> Unit = {}
//) {
//    Log.d("MAP", "> Style asked: ${styleUrl}")
//    val uniqueKey = remember(styleUrl) { UUID.randomUUID().toString() }
//    val context = LocalContext.current
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    val locationWasInitialized = remember { mutableStateOf(false) }
//    val cameraFollowsLocation = remember { mutableStateOf(false) }
//    val zoomLevel = remember { mutableDoubleStateOf(15.0) } // Niveau de zoom par défaut
//
//    AndroidView(
//        factory = { context ->
//            MapLibre.getInstance(context)
//            MapView(context).apply {
//                // Initialisation de la carte
//                getMapAsync { map ->
//
//                    val userPosIcon = buildIconUserPosition(this, context)
//                    val poiEventIcon = buildIconPoiEvent(this, context)
//                    val poiCultureIcon = buildIconPoiCulture(this, context)
//
//                    map.setStyle(styleUrl) {
//                        onMapReady(map)
//                        setupMapAfterStyleLoad(
//                            context = context,
//                            map = map,
//                            fusedLocationClient = fusedLocationClient,
//                            userPosIcon = userPosIcon,
//                            poiEventIcon = poiEventIcon,
//                            poiCultureIcon = poiCultureIcon,
//                            locationWasInitialized = locationWasInitialized,
//                            cameraFollowsLocation = cameraFollowsLocation,
//                            zoomLevel = zoomLevel.doubleValue
//                        )
//                    }
//                }
//            }
//        }
//    )
//}
//
//fun setupMapAfterStyleLoad(
//    context: Context,
//    map: MapLibreMap,
//    fusedLocationClient: FusedLocationProviderClient,
//    userPosIcon: Icon?,
//    poiEventIcon: Icon?,
//    poiCultureIcon: Icon?,
//    locationWasInitialized: MutableState<Boolean>,
//    cameraFollowsLocation: MutableState<Boolean>,
//    zoomLevel: Double
//) {
//    map.addMarker(
//        MarkerOptions()
//            .position(LatLng(48.98290839445176, 2.4065472144680085))
//            .title("POI")
//            .snippet("Description du POI")
//            .icon(poiEventIcon)
//    )
//    map.addMarker(
//        MarkerOptions()
//            .position(LatLng(48.96290839445176, 2.4865472144680085))
//            .title("POI")
//            .snippet("Description du POI")
//            .icon(poiCultureIcon)
//    )
//
//    var userLocationMarker: Marker? = null
//
//    val locationRequest = LocationRequest.create().apply {
//        interval = 5000
//        fastestInterval = 2000
//        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }
//
//    val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(result: LocationResult) {
//            val location = result.lastLocation
//            location?.let {
//                val newLatLng = LatLng(it.latitude, it.longitude)
//                userLocationMarker?.remove()
//                userLocationMarker = map.addMarker(
//                    MarkerOptions()
//                        .position(newLatLng)
//                        .icon(userPosIcon)
//                        .title("Vous êtes ici")
//                )
//                if (cameraFollowsLocation.value || !locationWasInitialized.value) {
//                    map.animateCamera(
//                        CameraUpdateFactory.newLatLngZoom(newLatLng, zoomLevel)
//                    )
//                    if (!locationWasInitialized.value) locationWasInitialized.value = true
//                }
//            }
//        }
//    }
//
//    if (ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    ) {
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            location?.let {
//                val userLatLng = LatLng(it.latitude, it.longitude)
//                if (cameraFollowsLocation.value || !locationWasInitialized.value) {
//                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, zoomLevel))
//                    if (!locationWasInitialized.value) locationWasInitialized.value = true
//                }
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//    }
//}










//@Composable
//fun GeolocatedMapLibreView(
//    styleUrl: String = "https://api.maptiler.com/maps/streets-v2/style.json?key=9zzN7nvsweC0cL7QY6k9",
//    onMapReady: (MapLibreMap) -> Unit = {}
//) {
//    Log.d("MAP", "> Style asked: ${styleUrl}")
//    val uniqueKey = remember(styleUrl) { UUID.randomUUID().toString() }
//    val context = LocalContext.current
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    val locationWasInitialized = remember { mutableStateOf(false) }
//    val cameraFollowsLocation = remember { mutableStateOf(false) }
//    val zoomLevel = remember { mutableDoubleStateOf(15.0) } // Niveau de zoom par défaut
//
//    AndroidView(
//        factory = { context ->
//            MapLibre.getInstance(context)
//            MapView(context).apply {
//                // Initialisation de la carte
//                getMapAsync { map ->
//
//                    val userPosIcon = buildIconUserPosition(this, context)
//                    val poiEventIcon = buildIconPoiEvent(this, context)
//                    val poiCultureIcon = buildIconPoiCulture(this, context)
//
//                    map.setStyle(styleUrl) { url ->
//                        onMapReady(map)
//                        Log.d("MAP", "> Style loaded: $url")
//                        // Ajouter des marqueurs fixes (optionnel)
//
//                        map.addMarker(
//                            MarkerOptions()
//                                .position(LatLng(48.98290839445176, 2.4065472144680085))
//                                .title("POI")
//                                .snippet("Description du POI")
//                                .icon(poiEventIcon)
//                        )
//                        map.addMarker(
//                            MarkerOptions()
//                                .position(LatLng(48.96290839445176, 2.4865472144680085))
//                                .title("POI")
//                                .snippet("Description du POI")
//                                .icon(poiCultureIcon)
//                        )
//
//                        var userLocationMarker: Marker? = null
//
//                        // Mise à jour continue de la position de l'utilisateur (optionnel)
//                        val locationRequest = LocationRequest.create().apply {
//                            interval = 5000 // Mise à jour toutes les 10 secondes
//                            fastestInterval = 2000 // Intervalle le plus rapide
//                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                        }
//
//                        Log.d("MAP", ">> Avant locationCallback")
//                        val locationCallback = object : LocationCallback() {
//                            override fun onLocationResult(p0: LocationResult) {
//                                Log.d("MAP", ">> onLocationResult")
//                                p0.let {
//                                    Log.d("MAP", ">> onLocationResult p0.let")
//                                    val location = it.lastLocation
//                                    location?.let { loc ->
//                                        val newLatLng = LatLng(loc.latitude, loc.longitude)
//                                        userLocationMarker?.remove()
//                                        // Ajouter ou mettre à jour le marqueur utilisateur
//                                        userLocationMarker = map.addMarker(
//                                            MarkerOptions()
//                                                .position(newLatLng)
//                                                .icon(userPosIcon)
//                                                .title("Vous êtes ici")
//                                        )
//                                        Log.d("MAP", ">> Avant déplacement de la caméra")
//                                        if (cameraFollowsLocation.value || !locationWasInitialized.value) {
//                                            Log.d("MAP", ">> Déplacement de la caméra")
//                                            map.animateCamera(
//                                                CameraUpdateFactory.newLatLngZoom(
//                                                    newLatLng,
//                                                    zoomLevel.doubleValue
//                                                )
//                                            )
//                                            if (!locationWasInitialized.value) locationWasInitialized.value = true
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        val permission = ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        )
//                        Log.d("MAP", ">> Avant PERMISSION_GRANTED")
//                        if (permission == PackageManager.PERMISSION_GRANTED) {
//                            Log.d("MAP", ">> PERMISSION_GRANTED")
//                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                                location?.let {
//                                    Log.d("MAP", ">> Récupération de la position")
//                                    val userLocation = LatLng(it.latitude, it.longitude)
//                                    var cameraUpdate: CameraUpdate? = null
//                                    if (cameraFollowsLocation.value) {
//                                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(
//                                            userLocation,
//                                            zoomLevel.doubleValue
//                                        )
//                                    }
//                                    if(cameraUpdate!=null) {
//                                        map.animateCamera(cameraUpdate)
//                                    }
//                                    /*val userMarker =
//                                        MarkerOptions().position(userLocation).title("You are here")
//                                    map.addMarker(userMarker)*/
//                                }
//                            }
//                        }
//
//                        // Demander la mise à jour de la position de l'utilisateur
//                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//                    }
//                }
//            }
//        },
//        update = { _ ->
//            // Important : ici, on ne fait rien de spécial à chaque recomposition
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}







@Composable
fun TestHttpRequest(url: String = "https://www.google.com") {
    var result by remember { mutableStateOf("Chargement...") }

    LaunchedEffect(Unit) {
        result = withContext(Dispatchers.IO) {
            httpGetRequestUsingHttpURLConnection(url)
        }
    }

    Text(text = result)
}

fun httpGetRequestUsingHttpURLConnection(urlString: String): String {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    return try {
        val stream = if (connection.responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }
        stream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        "Erreur (${e.javaClass.simpleName}) : ${e.message}"
    } finally {
        connection.disconnect()
    }
}










//@Composable
//fun GeolocatedMapLibreView() {
//    val context = LocalContext.current
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    // Variable pour garder une référence au dernier niveau de zoom
//    val zoomLevel = remember { mutableDoubleStateOf(15.0) } // Niveau de zoom par défaut
//
//    AndroidView(
//        factory = { context ->
//            Log.d("MAP", "MapView created")
//            MapLibre.getInstance(context)
//            MapView(context).apply {
//                Log.d("MAP", "MapView applied")
//                // Initialisation de la carte
//                getMapAsync { map ->
//                    val key = "9zzN7nvsweC0cL7QY6k9" // Clé MapTiler API
//                    val mapId = "streets-v2"
//                    val styleUrl = "https://api.maptiler.com/maps/$mapId/style.json?key=$key"
//                    map.setStyle(styleUrl) {
//                        // Ajouter des marqueurs fixes (optionnel)
//                        val poiEventIcon = buildIconPoiEvent(this, context)
//                        val poiCultureIcon = buildIconPoiCulture(this, context)
//
//                        map.addMarker(
//                            MarkerOptions()
//                                .position(LatLng(48.98290839445176, 2.4065472144680085))
//                                .title("POI")
//                                .snippet("Description du POI")
//                                .icon(poiEventIcon)
//                        )
//                        map.addMarker(
//                            MarkerOptions()
//                                .position(LatLng(48.96290839445176, 2.4865472144680085))
//                                .title("POI")
//                                .snippet("Description du POI")
//                                .icon(poiCultureIcon)
//                        )
//
//                        val permission = ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        )
//                        if (permission == PackageManager.PERMISSION_GRANTED) {
//                            // Récupérer la position actuelle de l'utilisateur et centre la caméra
//                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                                location?.let {
//                                    val userLocation = LatLng(it.latitude, it.longitude)
//                                    // Déplacer la caméra sans toucher au zoom actuel
//                                    map.animateCamera(
//                                        CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel.doubleValue)
//                                    )
//                                    // Ajouter un marqueur pour la position de l'utilisateur
//                                    val userMarker =
//                                        MarkerOptions().position(userLocation).title("You are here")
//                                    map.addMarker(userMarker)
//                                }
//                            }
//                        }
//
//                        // Mise à jour continue de la position de l'utilisateur (optionnel)
//                        val locationRequest = LocationRequest.create().apply {
//                            interval = 5000 // Mise à jour toutes les 10 secondes
//                            fastestInterval = 2000 // Intervalle le plus rapide
//                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                        }
//
//                        val locationCallback = object : LocationCallback() {
//                            override fun onLocationResult(p0: LocationResult) {
//                                p0.let {
//                                    val location = it.lastLocation
//                                    location?.let { loc ->
//                                        // Vérifier si la position a changé de manière significative
//                                        val newLocation = LatLng(loc.latitude, loc.longitude)
//                                        map.animateCamera(
//                                            CameraUpdateFactory.newLatLng(newLocation)
//                                        )
//                                        // Ajouter ou mettre à jour le marqueur utilisateur
//                                        val userMarker = MarkerOptions().position(newLocation).title("You are here")
//                                        map.addMarker(userMarker)
//                                    }
//                                }
//                            }
//                        }
//
//                        // Demander la mise à jour de la position de l'utilisateur
//                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//                    }
//                }
//            }
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}



