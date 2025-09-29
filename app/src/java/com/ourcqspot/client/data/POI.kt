package com.ourcqspot.client.data

data class POI(
    val id: Int,
    val name: String = "unnamed",
    val category: String? = null,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val illustrationPainterResource: Int? = null
)

object POI_Categories {
    const val CATEGORY_EVENT_FR = "Événement"
    const val CATEGORY_CULTURE_FR = "Culure"
    const val CATEGORY_OUTSIDE_FR = "Plein air"
}