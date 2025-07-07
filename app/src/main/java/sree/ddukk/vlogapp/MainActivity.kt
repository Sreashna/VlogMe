package sree.ddukk.vlogapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import sree.ddukk.VlogDetailsScreen
import sree.ddukk.vlogapp.ui.theme.VlogAppTheme
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VlogMeApp()
        }
    }
}

@Composable
fun VlogMeApp() {
    val navController = rememberNavController()
    val viewModel: VlogViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onRecordClick = { navController.navigate("record") },
                onVlogClick = { vlog ->
                    val encodedPath = Uri.encode(vlog.videoPath)
                    val encodedTitle = Uri.encode(vlog.title)
                    val encodedMood = Uri.encode(vlog.mood)
                    val encodedDate = Uri.encode(vlog.date)

                    navController.navigate(
                        "details/$encodedPath/$encodedTitle/$encodedMood/$encodedDate"
                    )
                }
            )
        }

        composable("record") {
            RecordScreen(
                onStart = { navController.navigate("camera") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("camera") {
            CameraRecordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            "addDetails/{videoPath}",
            arguments = listOf(navArgument("videoPath") { type = NavType.StringType })
        ) { backStack ->
            val path = Uri.decode(backStack.arguments?.getString("videoPath") ?: "")
            AddDetailsScreen(
                videoPath = path,
                viewModel = viewModel,
                onSaved = { navController.navigate("home") }
            )
        }

        composable(
            "details/{path}/{title}/{mood}/{date}",
            arguments = listOf(
                navArgument("path") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("mood") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStack ->
            val path = Uri.decode(backStack.arguments?.getString("path") ?: "")
            val title = Uri.decode(backStack.arguments?.getString("title") ?: "")
            val mood = Uri.decode(backStack.arguments?.getString("mood") ?: "")
            val date = Uri.decode(backStack.arguments?.getString("date") ?: "")

            VlogDetailsScreen(
                videoPath = path,
                title = title,
                mood = mood,
                date = date,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


