package com.ourcqspot.client.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomAppBar
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ContentAlpha
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ourcqspot.client.MainScreensData
import com.ourcqspot.client.R
import com.ourcqspot.client.graphs.HomeNavGraph
import com.ourcqspot.client.ui.theme.Blue1
import com.ourcqspot.client.ui.theme.NUNITO_FONT
import com.ourcqspot.client.ui.theme.Orange1
import com.ourcqspot.client.ui.theme.SelectedBottomItemColor
import com.ourcqspot.client.ui.theme.UnselectedBottomItemColor

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    var currentRoute by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentRoute = destination?.route
        }
    }
    val topBarShowFiltersBtn = currentRoute == MainScreensData.Map.route
//    Log.e("Nav", ">> currentRoute = $currentRoute")
//    Log.e("Nav", "> MainScreensData.Map.route = ${MainScreensData.Map.route}")
//    Log.e("Nav", ">> topBarShowFiltersBtn = $topBarShowFiltersBtn")

    val focusManager = LocalFocusManager.current
    Scaffold (
        modifier = Modifier.clickable { focusManager.clearFocus() },
        topBar = { TopBar(topBarShowFiltersBtn) },
        bottomBar = { BottomBar(navController = navController) },
        //contentWindowInsets =
    ) { innerPadding ->
        Box (
            modifier = Modifier//.padding(innerPadding).background(color = Color.Yellow)
                .navigationBarsPadding()
                .imePadding()
                .padding(PaddingValues(top = innerPadding.calculateTopPadding()))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HomeNavGraph(navController = navController)
        }
    }
}

@Composable
fun TopBar(showFiltersBtn: Boolean = false) {
    TopAppBar (
        modifier = Modifier
            .statusBarsPadding()
            .defaultMinSize(minHeight = 120.dp)
            .fillMaxWidth(),
        backgroundColor = Color(0xffffffff)
        //contentColor = Color(0xff0E2176)
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 12.5.dp)
        ) {
            Column(
                modifier = Modifier
                    //.background(color = Color(0xff0088ff))
                    .padding(horizontal = 5.dp)
                    .defaultMinSize(minHeight = 30.dp, minWidth = 10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.5.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_ourcqspot),
                        contentDescription = stringResource(id = R.string.logo_ourcqspot_content_description),
                        modifier = Modifier.height(height = 20.dp)
                    )
                }
                Row (
                    modifier = Modifier
                        .padding(vertical = 7.5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val searchPlaceholder = "Recherche"
                    var searchValue by remember { mutableStateOf("") }
                    var placeholderAlpha by remember { mutableStateOf(1F) }
                    val focusManager = LocalFocusManager.current
                    Row (
                        modifier = Modifier
                            .clickable { focusManager.moveFocus(FocusDirection.Next) }
                            .weight(1f)
                            .alpha(.5F)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xff000000)
                                ),
                                shape = RoundedCornerShape(17.5.dp)
                            )
                            .padding(horizontal = 20.dp)
                            .height(height = 50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon (
                            painter = painterResource(R.drawable.icon_search),
                            contentDescription = "Search : ",
                            modifier = Modifier.size(size = 16.6.dp)
                        )
                        Spacer ( modifier = Modifier.width(width = 10.dp) )
                        Box (
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text (
                                searchPlaceholder,
                                fontFamily = NUNITO_FONT,
                                fontSize = 17.5.sp,
                                modifier = Modifier.alpha(placeholderAlpha)
                            )
                            BasicTextField(
                                value = searchValue,
                                onValueChange = { searchValue = it },
                                textStyle = TextStyle(
                                    fontFamily = NUNITO_FONT,
                                    fontSize = 17.5.sp
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        focusManager.clearFocus()
                                    },
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    },
                                    //onDone = {}
                                ),

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged {
                                        placeholderAlpha =
                                            if ((!it.isFocused) and (searchValue == "")) {
                                                1F
                                            } else {
                                                0F
                                            }
                                    }
                            )
                        }
                    }
                    // if (showFiltersBtn) {
                    AnimatedVisibility(showFiltersBtn) {
                        Row (
                            Modifier
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(Modifier.width(6.66.dp))
                            Image(
                                painter = painterResource(R.drawable.icon_filters),
                                contentDescription = "Open/close filters",
                                //tint = Color(0xff0E2176),
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                    //}
                }
            }
        }
    }
}

// TO DO: split code and composable elements like this
/*@Composable
fun SearchIcon() {
Icon(
    painter = painterResource(R.drawable.icon_search),
    contentDescription = ""
)
}*/

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun BottomBar(navController: NavHostController) {
    val screens = MainScreensData.getScreensList()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column (
        Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(vertical = 30.dp, horizontal = 27.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mainScreens = MainScreensData.getScreensList()
        val flagOffset by animateIntOffsetAsState(
            targetValue = when (currentDestination?.route) {
                mainScreens[0].route -> IntOffset(-140, 0)
                mainScreens[1].route -> IntOffset(-70, 0)
                mainScreens[3].route -> IntOffset(70, 0)
                mainScreens[4].route -> IntOffset(140, 0)
                else -> IntOffset.Zero
            }, label = "BottomBar navigation flag [x-offset animation]"
        )
        Spacer(
            Modifier
                .offset(flagOffset.x.dp, flagOffset.y.dp) //.offset { flagOffset }
                .size(width = 40.dp, height = 3.33.dp)
                .background(
                    Orange1,
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
        )
        BottomAppBar (
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .defaultMinSize(minHeight = 75.dp),
            //contentColor = Color(0xff0E2176)
            backgroundColor = Blue1
        ) {
            BottomNavigation(
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            ) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController,
                    )
                }
            }
        }
        }
}

@Composable
fun RowScope.AddItem(
    screen: MainScreensData,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    var itemColor = UnselectedBottomItemColor
    if (currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true) {
        itemColor = SelectedBottomItemColor
    }
    BottomNavigationItem(
        label = {
            BoxWithConstraints {
                Column (
                    Modifier
                        .requiredHeight(maxHeight + 10.dp)
                        .requiredWidth(maxWidth + 24.dp), // Draws outside of the imposed item padding)
                    Arrangement.Bottom,
                    Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(unbounded = true),
                        softWrap = false,
                        text = screen.frTitle,
                        color = itemColor,
                        fontFamily = NUNITO_FONT,
                        fontSize = 13.3.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        icon = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(22.5.dp),
                    painter = painterResource(screen.iconPainterId),
                    contentDescription = screen.iconContentDescription,
                    tint = itemColor
                )
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )
            }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            MainScreensData.lastScreen = screen
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}