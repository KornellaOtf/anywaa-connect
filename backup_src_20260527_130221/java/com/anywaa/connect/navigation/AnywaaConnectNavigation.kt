import com.anywaa.connect.util.FeatureFlags
package com.anywaa.connect.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anywaa.connect.AnywaaConnectApplication
import com.anywaa.connect.screens.*
import com.anywaa.connect.viewmodels.ChatViewModelFactory
import com.anywaa.connect.viewmodels.ThemeViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object Home : Screen("home")
    object Projects : Screen("projects")
    object Activity : Screen("activity")
    object Chat : Screen("chat/{chatId}?creatorId={creatorId}") {
        fun createRoute(chatId: String = "new", creatorId: String? = null): String {
            return if (creatorId != null) "chat/$chatId?creatorId=$creatorId" else "chat/$chatId"
        }
    }
    object ChatHistory : Screen("chat_history")
    object WritingAid : Screen("writing_aid")
    object Translator : Screen("translator")
    object Transcriber : Screen("transcriber")
    object ScamDetector : Screen("scam_detector")
    object ImageGenerator : Screen("image_generator")
    object VibeCoder : Screen("vibe_coder")
    object VibeVoice : Screen("vibevoice")
    object CodeCanvas : Screen("code_canvas") {
        fun createRoute(codeContent: String, codeType: String = "html") =
            "code_canvas?code=${android.net.Uri.encode(codeContent)}&type=$codeType"
    }
    object Settings : Screen("settings")
    object Models : Screen("models")
    object About : Screen("about")
    object Terms : Screen("terms")
    object CreatorGeneration : Screen("creator_generation")
    object Premium : Screen("premium")
}

private data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun AnywaaConnectNavigation(
    navController: NavHostController,
    chatViewModelFactory: ChatViewModelFactory,
    themeViewModel: ThemeViewModel,
    startDestination: String = Screen.Chat.createRoute("new")
) {
    val drawerState = rememberSaveable(saver = DrawerState.Saver(confirmStateChange = { true })) {
        DrawerState(DrawerValue.Closed)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isOnChatRoute = currentRoute == Screen.Chat.route
    var wasOnChatRoute by remember { mutableStateOf(isOnChatRoute) }
    val context = LocalContext.current

    val FeatureFlags.PREMIUM_ENABLED = true

    LaunchedEffect(isOnChatRoute) {
        if (wasOnChatRoute && !isOnChatRoute) {
            (context.applicationContext as? AnywaaConnectApplication)?.inferenceService?.unloadModel()
            com.anywaa.connect.embedding.RagServiceManager.getInstance(context.applicationContext).cleanup()
        }
        wasOnChatRoute = isOnChatRoute
    }

    val bottomNavItems = listOf(
        BottomNavItem("Chat", "chat/new?creatorId={creatorId}", Icons.Default.Chat),
        BottomNavItem("Tools", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Projects", Screen.Projects.route, Icons.Default.Folder),
        BottomNavItem("Activity", Screen.Activity.route, Icons.Default.History),
        BottomNavItem("Settings", Screen.Settings.route, Icons.Default.Settings)
    )

    // Show bottom nav only on main routes
    val showBottomNav = currentRoute in listOf(
        Screen.Chat.route,
        Screen.Home.route,
        Screen.Projects.route,
        Screen.Activity.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route || (item.name == "Chat" && currentRoute == Screen.Chat.route)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    val route = if (item.name == "Chat") Screen.Chat.createRoute("new") else item.route
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.name) },
                            label = { Text(item.name) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Loading.route) {
                LoadingScreen(onNavigateNext = {
                    navController.navigate(Screen.Chat.createRoute("new")) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToFeature = { route ->
                        when (route) {
                            "chat" -> navController.navigate(Screen.Chat.createRoute("new"))
                            "writing_aid" -> navController.navigate(Screen.WritingAid.route)
                            "translator" -> navController.navigate(Screen.Translator.route)
                            "transcriber" -> navController.navigate(Screen.Transcriber.route)
                            "scam_detector" -> navController.navigate(Screen.ScamDetector.route)
                            "image_generator" -> navController.navigate(Screen.ImageGenerator.route)
                            "vibe_coder" -> navController.navigate(Screen.VibeCoder.route)
                            "creator_generation" -> navController.navigate(Screen.CreatorGeneration.route)
                            "vibevoice" -> navController.navigate(Screen.VibeVoice.route)
                        }
                    },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToPremium = {},
                    FeatureFlags.PREMIUM_ENABLED = FeatureFlags.PREMIUM_ENABLED
                )
            }

            composable(Screen.Projects.route) {
                ProjectsScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.Activity.route) {
                ActivityScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.ChatHistory.route) {
                ChatHistoryScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToChat = { chatId -> navController.navigate(Screen.Chat.createRoute(chatId)) },
                    onCreateNewChat = { navController.navigate(Screen.Chat.createRoute("new")) }
                )
            }

            composable(
                route = Screen.Chat.route,
                arguments = listOf(
                    androidx.navigation.navArgument("chatId") { defaultValue = "new" },
                    androidx.navigation.navArgument("creatorId") { nullable = true; defaultValue = null }
                )
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: "new"
                val creatorId = backStackEntry.arguments?.getString("creatorId")

                ChatScreen(
                    chatId = chatId,
                    creatorId = creatorId,
                    viewModelFactory = chatViewModelFactory,
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToModels = { navController.navigate(Screen.Models.route) },
                    onNavigateToChat = { newChatId ->
                        navController.navigate(Screen.Chat.createRoute(newChatId)) {
                            popUpTo(Screen.Chat.route) { inclusive = true }
                        }
                    },
                    onNavigateToCreatorChat = { newCreatorId ->
                        navController.navigate(Screen.Chat.createRoute("new", newCreatorId)) {
                            popUpTo(Screen.Chat.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                    drawerState = drawerState
                )
            }

            composable(Screen.WritingAid.route) { WritingAidScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }) }
            composable(Screen.Translator.route) { TranslatorScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }) }
            composable(Screen.Transcriber.route) { TranscriberScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }) }
            composable(Screen.ScamDetector.route) { ScamDetectorScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }) }
            composable(Screen.ImageGenerator.route) { ImageGeneratorScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }) }
            composable(Screen.VibeCoder.route) { VibeCoderScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }, onNavigateToCanvas = { code, type -> navController.navigate(Screen.CodeCanvas.createRoute(code, type)) }) }
            composable(Screen.VibeVoice.route) { VibeVoiceScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToModels = { navController.navigate(Screen.Models.route) }) }
            
            composable(
                route = "code_canvas?code={code}&type={type}",
                arguments = listOf(
                    androidx.navigation.navArgument("code") { type = androidx.navigation.NavType.StringType },
                    androidx.navigation.navArgument("type") { type = androidx.navigation.NavType.StringType }
                )
            ) { backStackEntry ->
                val codeContent = android.net.Uri.decode(backStackEntry.arguments?.getString("code") ?: "")
                val codeType = backStackEntry.arguments?.getString("type") ?: "html"
                CodeCanvasScreen(codeContent = codeContent, codeType = codeType, onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToModels = { navController.navigate(Screen.Models.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onNavigateToTerms = { navController.navigate(Screen.Terms.route) },
                    onNavigateToPremium = { navController.navigate(Screen.Premium.route) },
                    themeViewModel = themeViewModel
                )
            }

            composable(Screen.Models.route) { ModelDownloadScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToPremium = {}) }
            composable(Screen.About.route) { AboutScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(Screen.Terms.route) { TermsOfServiceScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(Screen.CreatorGeneration.route) { CreatorGenerationScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToChat = { creatorId -> navController.navigate(Screen.Chat.createRoute("new", creatorId)) { popUpTo(Screen.Home.route) } }, viewModelFactory = chatViewModelFactory) }
            composable(Screen.Premium.route) { PremiumScreen(onNavigateBack = { navController.popBackStack() }) }
        }
    }
}
