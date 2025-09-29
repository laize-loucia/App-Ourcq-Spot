package com.ourcqspot.client.composables

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.ourcqspot.client.R
import org.maplibre.android.annotations.Icon
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.maps.MapView

@Composable
fun RequestLocationPermission(onGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionState = rememberLauncherForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        } else {
            Log.d("PERM", "Permission refusée")
        }
    }

    LaunchedEffect (Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionState.launch(permission)
        } else {
            onGranted()
        }
    }
}

fun buildIconUserPosition(mapView: MapView, context: Context): Icon? {
    val userPosIconDrawable = ResourcesCompat.getDrawable(
        mapView.resources,
        //org.maplibre.android.R.drawable.maplibre_info_icon_default,
        R.drawable.icon_user_position,
        null
    )!!
    val userPosBitmapBlue = userPosIconDrawable.toBitmap()
    val userPosBitmapRed = userPosIconDrawable
        .mutate()
        .apply { setTint(android.graphics.Color.RED) }
        .toBitmap()

    return IconFactory.getInstance(context)
        .fromBitmap(if (false) userPosBitmapRed else userPosBitmapBlue)
}
fun buildIconPoiEvent(mapView: MapView, context: Context): Icon? {
    return buildIconFromResource(mapView, context, R.drawable.icon_pin_event)
}
fun buildIconPoiCulture(mapView: MapView, context: Context): Icon? {
    return buildIconFromResource(mapView, context, R.drawable.icon_pin_culture)
}

fun buildIconFromResource(mapView: MapView, context: Context, resourceDrawable: Int): Icon? {
    val iconDrawable = ResourcesCompat.getDrawable(
        mapView.resources,
        resourceDrawable,
        null
    )!!
    val iconBitmap = iconDrawable.toBitmap()

    return IconFactory.getInstance(context)
        .fromBitmap(iconBitmap)
}

@Composable
fun DefaultIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.icon_dots),
        contentDescription = "See more",
    )
}

@Composable
fun MapFilterItem(
    text: String? = null,
    icon: @Composable (Modifier) -> Unit = { DefaultIcon(it) },
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            Modifier
                .padding(horizontal = 4.5.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color(0xFFF4F4F4))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            icon(Modifier)
        }
        Spacer(Modifier.height(3.33.dp))
        if (text != null) Text(text, fontSize = 12.sp,)
    }
}

@Composable
fun MapFilterSpacer(modifier: Modifier = Modifier) {
    Spacer(Modifier.height(12.5.dp))
}

/**
 * Builds the composable of the side menu of map/POIs filters.
 * @param[modifier] Modifier of the menu, useful for passing instructions depending on the scope such as .
 */
@Composable
fun MapFiltersMenu(modifier: Modifier = Modifier) {
    Box (
        modifier
    ) {
        Column (
            Modifier
                .clip(RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp))
                .background(Color.White)
                //.fillMaxHeight()
                .width(66.dp)
                .padding(vertical = 25.dp, horizontal = 2.5.dp)
        ) {
            MapFilterItem(
                "Plein air",
                icon = { Icon(
                    painter = painterResource(R.drawable.icon_outdoor),
                    contentDescription = "Plein air",
                ) },
            )
            MapFilterSpacer()
            MapFilterItem(
                "Restaurants",
                icon = { Icon(
                    painter = painterResource(R.drawable.icon_restaurant),
                    contentDescription = "Restaurants",
                ) },
            )
            MapFilterSpacer()
            MapFilterItem()
        }
    }
}