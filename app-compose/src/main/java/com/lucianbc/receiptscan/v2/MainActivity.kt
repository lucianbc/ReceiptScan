package com.lucianbc.receiptscan.v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lucianbc.receiptscan.v2.ui.theme.ReceiptScanTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ReceiptScanTheme {
        val navController = rememberNavController()

        // A surface container using the 'background' color from the theme
        Navigation(navController)
      }
    }
  }
}


typealias Component = @Composable () -> Unit

object NavTree {
  /*
   * - expenses
   *    - detail
   * - drafts
   *    - detail
   * - export
   * - settings
   *    - setting1
   *    - setting2
   *    - setting3
   * - camera
   */
  sealed class Destination(val route: String, val component: Component)

  sealed interface BottomNavDestinations {
    val resourceId: Int
      @StringRes get
    val route: String
  }

  object Expenses : Destination("expenses", { ExpensesScreen() }), BottomNavDestinations {
    override val resourceId = R.string.expenses
  }

  object ExpenseDetail : Destination("expense/detail", { ExpenseDetailScreen() })

  object Drafts : Destination("drafts", { DraftsScreen() }), BottomNavDestinations {
    override val resourceId = R.string.drafts
  }

  object DraftDetail : Destination("drafts/detail", { DraftDetailScreen() })

  object Export : Destination("export", { ExportsScreen() }), BottomNavDestinations {
    override val resourceId = R.string.export
  }

  object Settings : Destination("settings", { SettingsScreen() }), BottomNavDestinations {
    override val resourceId = R.string.settings
  }

  object DefaultCurrency : Destination("settings/defaultCurrency", { DefaultCurrencySettingScreen() })

  object Camera : Destination("camera", { CameraScreen() })

  val bottomNavDestinations = listOf<BottomNavDestinations>(
    Expenses,
    Drafts,
    Export,
    Settings,
  )

  val allRoutes = listOf(
    Expenses,
    ExpenseDetail,
    Drafts,
    DraftDetail,
    Export,
    Settings,
    DefaultCurrency,
    Camera,
  )
}

@Composable
fun Screen(name: String) {
  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Greeting(name)
  }
}

@Composable
fun CameraScreen() {
  Screen(name = "Camera")
}

@Composable
fun ExpensesScreen() {
  Screen(name = "Expenses")
}

@Composable
fun DraftsScreen() {
  Screen(name = "Drafts")
}

@Composable
fun ExportsScreen() {
  Screen(name = "Exports")
}

@Composable
fun SettingsScreen() {
  Screen(name = "Settings")
}

@Composable
fun ExpenseDetailScreen() {
  Screen(name = "ExpenseDetail")
}

@Composable
fun DraftDetailScreen() {
  Screen(name = "DraftDetail")
}

@Composable
fun DefaultCurrencySettingScreen() {
  Screen(name = "Default Currency Setting")
}

private fun NavHostController.topLevelNavigate(route: String) {
  navigate(route) {
    // Pop up to the start destination of the graph to
    // avoid building up a large stack of destinations
    // on the back stack as users select items
    popUpTo(graph.startDestinationId) {
      saveState = true
    }
    // Avoid multiple copies of the same destination when
    // re-selecting the same item
    launchSingleTop = true
    // Restore state when re-selecting a previously selected item
    restoreState = true
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(navController: NavHostController) {
  val navBackStackEntry = navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry.value

  val showNavigation = currentDestination?.destination?.route != NavTree.Camera.route

  Scaffold(
    bottomBar = {
      AnimatedVisibility(
        visible = showNavigation,
      ) {
        NavigationBar {
          NavTree.bottomNavDestinations.forEach { screen ->
            NavigationBarItem(
              selected = currentDestination?.destination?.route?.startsWith(screen.route) ?: false,
              icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
              label = { Text(text = stringResource(id = screen.resourceId))},
              onClick = {
                navController.topLevelNavigate(screen.route)
              },
            )
          }
        }
      }
    },
    floatingActionButton = {
      AnimatedVisibility(
        visible = showNavigation,
      ) {
        FloatingActionButton(
          onClick = { navController.navigate(NavTree.Camera.route) },
        ) {
          Icon(Icons.Default.Create, contentDescription = null)
        }
      }
    },
    floatingActionButtonPosition = FabPosition.End
  ) {
    NavHost(
      navController = navController,
      startDestination = NavTree.Expenses.route,
      modifier = Modifier.padding(it)
    ) {
      NavTree.allRoutes.forEach { screen ->
        composable(screen.route) {
          screen.component()
        }
      }
    }
  }
}


@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  ReceiptScanTheme {
    Greeting("Android")
  }
}