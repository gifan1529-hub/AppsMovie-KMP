package com.example.moviekmp.UI.Navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviekmp.DI.appModule
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Greeting
import com.example.moviekmp.UI.Component.BottomBar
import com.example.moviekmp.UI.Screen.BookingTicket
import com.example.moviekmp.UI.Screen.BuffetItemScreen
import com.example.moviekmp.UI.Screen.DetailFilmScreen
import com.example.moviekmp.UI.Screen.DetailTicketScreen
import com.example.moviekmp.UI.Screen.EditUserScreen
import com.example.moviekmp.UI.Screen.FavoriteScreen
import com.example.moviekmp.UI.Screen.HomeScreen
import com.example.moviekmp.UI.Screen.LoginScreen
import com.example.moviekmp.UI.Screen.ResultPaymentScreen
import com.example.moviekmp.UI.Screen.SearchScreen
import com.example.moviekmp.UI.Screen.SeatSelectionScreen
import com.example.moviekmp.UI.Screen.SignInScreen
import com.example.moviekmp.UI.Screen.SignUpScreen
import com.example.moviekmp.UI.Screen.TicketScreen
import com.example.moviekmp.UI.Screen.TicketSelectionScreen
import com.example.moviekmp.UI.Screen.UserScreen
import com.example.moviekmp.ViewModel.AuthViewModel
import com.example.moviekmp.ViewModel.BookingTicketVM
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

import moviekmp.composeapp.generated.resources.Res
import moviekmp.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import kotlin.collections.listOf

@Suppress("SuspiciousIndentation")
@Composable
fun App(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = koinViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

        MaterialTheme {
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = modifier.fillMaxSize()
            ) {
                composable(route = "login") {
                    LaunchedEffect(Unit) {
                        if (authViewModel.isLoggedIn()) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    LoginScreen(navController = navController)
                }
                composable("signin") {
                    SignInScreen(navController)
                }
                composable("signup") {
                    SignUpScreen(navController)
                }
                composable(route = "home") {
                    MainPagerScreen(navController = navController)
                }
                composable(
                    route = "home?page={page}",
                    arguments = listOf(navArgument("page") { type = NavType.IntType; defaultValue = 1 })
                ) { backStackEntry ->
                    val page = backStackEntry.arguments?.getInt("page") ?: 1
                    MainPagerScreen(navController = navController, startPage = page)
                }
                composable(route = "ticketselection") {
                    val prefsManager : PrefsManager = koinInject ()
                    val bookingVM: BookingTicketVM = koinViewModel()
                    BookingTicket(navController = navController, viewModel = bookingVM, prefsManager = prefsManager)
                }
                composable(route = "buffet") {
                    val bookingVM: BookingTicketVM = koinViewModel()
                    BuffetItemScreen(navController = navController, viewModel = bookingVM)
                }
                composable(route = "resultpayment") {
                    ResultPaymentScreen(navController = navController)
                }
                composable(
                    route = "detailticket/{bookingId}",
                    arguments = listOf(navArgument("bookingId") { type = NavType.IntType }),
//                    deepLinks = listOf(
//                        navDeepLink { uriPattern = "app://movie/detailticket/{bookingId}" }
//                    )
                ) { backStackEntry ->
                    val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
                    DetailTicketScreen(navController = navController, bookingId = bookingId)
                }
                composable(
                    route = "user",
                    enterTransition = {
                        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    }
                ) {
                    UserScreen(navController = navController)
                }

                composable(route = "edituser/{userEmail}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("userEmail") ?: ""
                    EditUserScreen(
                        navController = navController,
                        userEmail = email,
                        onEditSucces = {
                            navController.previousBackStackEntry?.savedStateHandle?.set("refresh_trigger", true)
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = "search",
                    enterTransition = {
                        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    }
                ) {
                    SearchScreen(
                        navController = navController,
                        onMovieClick = { movieId -> navController.navigate("detailfilm/$movieId") }
                    )
                }

                composable(route = "detailfilm/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                    val bookingVM: BookingTicketVM = koinViewModel()

                    DetailFilmScreen(navController = navController, movieId = movieId, bookingVM = bookingVM)
                }

                composable(route = "seatselection") {
                    SeatSelectionScreen(navController = navController)
                }
            }
        }
//    }
}

@Composable
fun MainPagerScreen (navController: NavController, startPage: Int = 1) {
    val screens = listOf("favorite", "home", "ticket")
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { screens.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(startPage) {
        pagerState.scrollToPage(startPage)
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            BottomBar(
                selectedIndex = pagerState.currentPage,
                onHomeClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                onFavoriteClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                onTicketClick = { scope.launch { pagerState.animateScrollToPage(2) } }
            )
        }
    ) { _ ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> FavoriteScreen(navController = navController)
                1 -> HomeScreen(navController = navController)
                2 -> TicketScreen(navController = navController)
            }
        }
    }
}