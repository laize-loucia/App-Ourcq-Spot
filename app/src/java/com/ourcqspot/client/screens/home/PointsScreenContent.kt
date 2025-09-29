package com.ourcqspot.client.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ourcqspot.client.R

@Composable
fun PointsScreenContent() {
    Column (
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(vertical = 25.dp, horizontal = 15.dp)
            .padding(bottom = 100.dp),
        Arrangement.spacedBy(40.dp),
    ) {
        Image(
            painterResource(R.drawable.tmp_section_progression),
            "Section progression",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painterResource(R.drawable.tmp_section_succes),
            "Section succes",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painterResource(R.drawable.tmp_section_badges),
            "Section badges",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}