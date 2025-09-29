package com.ourcqspot.client.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ourcqspot.client.R
import com.ourcqspot.client.ui.theme.NUNITO_FONT





//La fonction GetCompleteListImgRessources donne une liste d'images.
var listImgRessources = getCompleteListImgRessources()

fun getCompleteListImgRessources(): List<Int> {
    return listOf(
        R.drawable.bassin_de_la_villette_1,
        R.drawable.parc_de_la_villette_1,
        R.drawable.parcours_1,
        R.drawable.parc_de_la_villette_1,
        R.drawable.parcours_1,
        R.drawable.parcours_2,
        R.drawable.bassin_de_la_villette_1,
    )
}


//La fonction NewsscreenContent permet de structurer avec des titres, des onglets, des carrousels
//et des sections informatives
@Composable
fun NewsScreenContent() {
    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5)) // Couleur de fond grise claire
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
        // textDecoration = TextDecoration.Underline

    ) {
        Spacer(modifier = Modifier.height(8.dp))
        // Title
        Text(
            text = "Explorez le canal de l'Ourcq autrement",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
        )

        // Ajoute un petit espace entre le titre et les onglets
        Spacer(modifier = Modifier.height(8.dp))  // Espace de 8.dp

        //La fonction TabSection crée une section de 2 onglets
        //pour naviguer entre les lieux les plus appréciés et les lieux à proximité
        TabSection()

        Spacer(modifier = Modifier.height(8.dp))

        // First carousel
        Carrousel(
            items = listOf("Bassin de la Villette", "Parc de la Villette", "Philarmonie de Paris", "Ciné 104"),
            backgroundColor = Color(0xFFDFF8FF)

        )

        // Second section : Votre prochaine exploration
        Text(
            text = "Votre prochaine exploration commence ici !",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        //Le carrousel est contenu dans une fonction composable qui prend en paramètres items, type, backgroundColor
        //listOf : fonction qui crée une liste immuable à partir des éléments fournis

        Carrousel(
            items = listOf("Visitez les Buttes-Chaumont", "Explorez les bords du canal", "Découvrez les parcs"),
            backgroundColor = Color.White, // Couleur blanche
            type = "2"
        )

        // Last section : Gagnez des points
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Gagnez des points et débloquez des récompenses !",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "En explorant les lieux, collectez des points et échangez-les contre des récompenses exclusives !",
                fontSize = 14.sp,
                fontFamily = NUNITO_FONT,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
            Button(
                onClick = {  },
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(text = "En savoir plus")
            }
        }
    }
}


//La fonction TabSection crée une section d'onglets avec TabRow pour contenir les onglets et Tab pour chaque onglet individuel

//Lorsqu'un onglet est sélectionné, son texte change de couleur et l'index de l'onglet sélectionné
//est mis à jour  pour naviguer associés à chaque onglet

//TabRow est une fonction composable qui crée une rangee d'onglets et place en paramètres l'index
//de l'onglet sélectionné, la couleur de fon et du contenu de ces derniers


@Composable
fun TabSection() {
    var selectedTab by remember { mutableStateOf(0) }

    TabRow(
        selectedTabIndex = selectedTab,
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        listOf("Lieux les plus appréciés", "Lieux à proximité").forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index }
            ) {
                Text(
                    text = title,
                    fontFamily = NUNITO_FONT,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTab == index) Color.Blue else Color.Gray
                )
            }
        }
    }
}

@Composable
fun Carrousel(items: List<String>, backgroundColor: Color, type: String = "1") {
    var cardWidth = 100.dp
    var cardHeight = 100.dp
    var cardContentAlignment = Alignment.Center
    when (type) {
        "1" -> {
            cardWidth = 225.dp
            cardHeight = 300.dp
            cardContentAlignment = Alignment.TopStart
        }
        "2" -> {
            cardWidth = 300.dp
            cardHeight = 200.dp
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .width(cardWidth)
                    .height(cardHeight)
                    .padding(end = 16.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = backgroundColor,
                elevation = 4.dp
            ) {
                Box(
                    Modifier
                        .fillMaxSize(),
                    cardContentAlignment
                ) {
                    Image(
                        painterResource(
                            listImgRessources.first()
                        ),
                        "Section progression",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    listImgRessources = listImgRessources.slice(1 until listImgRessources.size)
                    if (listImgRessources.size<1) {
                        listImgRessources = getCompleteListImgRessources()
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .background(
                                Color(0x6C000000),
                                RoundedCornerShape(7.5.dp)
                            )
                            .padding(vertical = 5.dp),
                        Alignment.Center
                    ) {
                        Text(
                            text = item,
                            fontSize = 16.sp,
                            fontFamily = NUNITO_FONT,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}










/*
@Composable
fun NewsScreenContent() {
    // Fonctionnement identique à MapScreenContent.
    // Ce @Composable est intégré dans HomeNavGraph (dans NavHost {composable() { ... } })

    Column ( // Tu peux changer Column en Row, en Box, en LazyColumn, ...etc
        Modifier
            .fillMaxSize()
            .background(Color.Cyan) // À retirer
    ) {
        // -------------------
        // À remplacer par le contenu
        Box (
            Modifier
                .padding(15.dp)
                .fillMaxSize()
                .background(Color(0x8844CCAA))
        ) {

        }
        // -------------------
    }
}
*/