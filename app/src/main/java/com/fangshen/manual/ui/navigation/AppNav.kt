package com.fangshen.manual.ui.navigation

import android.app.Application
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.drug.DrugDetailScreen
import com.fangshen.manual.ui.drug.DrugDetailViewModel
import com.fangshen.manual.ui.drug.DrugEditScreen
import com.fangshen.manual.ui.drug.DrugEditViewModel
import com.fangshen.manual.ui.drug.DrugListScreen
import com.fangshen.manual.ui.drug.DrugListViewModel
import com.fangshen.manual.ui.entry.DoseRecordScreen
import com.fangshen.manual.ui.entry.DoseRecordViewModel
import com.fangshen.manual.ui.entry.EntryDetailScreen
import com.fangshen.manual.ui.entry.EntryDetailViewModel
import com.fangshen.manual.ui.entry.EntryEditScreen
import com.fangshen.manual.ui.entry.EntryEditViewModel
import com.fangshen.manual.ui.home.HomeScreen
import com.fangshen.manual.ui.home.HomeViewModel
import com.fangshen.manual.ui.prescription.PrescriptionEditScreen
import com.fangshen.manual.ui.prescription.PrescriptionEditViewModel
import com.fangshen.manual.ui.prescription.PrescriptionListScreen
import com.fangshen.manual.ui.prescription.PrescriptionListViewModel
import com.fangshen.manual.ui.report.ReportEditScreen
import com.fangshen.manual.ui.report.ReportEditViewModel
import com.fangshen.manual.ui.report.ReportListScreen
import com.fangshen.manual.ui.report.ReportListViewModel
import com.fangshen.manual.ui.report.ReportReaderScreen
import com.fangshen.manual.ui.report.ReportReaderViewModel
import com.fangshen.manual.ui.settings.AboutScreen
import com.fangshen.manual.ui.settings.DataManagementScreen
import com.fangshen.manual.ui.settings.DataManagementViewModel
import com.fangshen.manual.ui.settings.DisclaimerScreen
import com.fangshen.manual.ui.alert.AlertScreen
import com.fangshen.manual.ui.alert.AlertViewModel
import com.fangshen.manual.ui.profile.ProfileViewModel
import com.fangshen.manual.ui.settings.ProfileScreen
import com.fangshen.manual.ui.settings.ScheduleListScreen
import com.fangshen.manual.ui.entry.ScheduleListViewModel
import com.fangshen.manual.ui.handbook.HandbookScreen

@Composable
fun AppNav(application: Application) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalApplication provides application) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            enterTransition = { slideInHorizontally(animationSpec = tween(220)) { it / 6 } + fadeIn(tween(180)) },
            exitTransition = { fadeOut(tween(140)) },
            popEnterTransition = { fadeIn(tween(180)) },
            popExitTransition = { slideOutHorizontally(animationSpec = tween(220)) { it / 6 } + fadeOut(tween(140)) },
        ) {
            composable(Routes.HOME) {
                val vm: HomeViewModel = viewModel(
                    factory = HomeViewModel.provideFactory(application),
                )
                HomeScreen(
                    onNavigate = { route -> navController.navigate(route) },
                    viewModel = vm,
                )
            }
            composable(Routes.DRUG_LIST) {
                val vm: DrugListViewModel = viewModel(
                    factory = DrugListViewModel.provideFactory(application),
                )
                DrugListScreen(
                    onNavigate = { route -> navController.navigate(route) },
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.DRUG_DETAIL,
                arguments = listOf(
                    navArgument("drugId") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val drugId = backStackEntry.arguments?.getString("drugId").orEmpty()
                val vm: DrugDetailViewModel = viewModel(
                    factory = DrugDetailViewModel.provideFactory(application, drugId),
                )
                DrugDetailScreen(
                    onNavigate = { route -> navController.navigate(route) },
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.DRUG_EDIT,
                arguments = listOf(navArgument("drugId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val drugId = backStackEntry.arguments?.getString("drugId").orEmpty()
                val vm: DrugEditViewModel = viewModel(
                    factory = DrugEditViewModel.provideFactory(application, drugId),
                )
                DrugEditScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.ENTRY_NEW,
                arguments = listOf(
                    navArgument("type") { type = NavType.StringType },
                    navArgument("uri") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    },
                    navArgument("drugId") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    },
                ),
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type").orEmpty()
                val uri = backStackEntry.arguments?.getString("uri")
                val drugId = backStackEntry.arguments?.getString("drugId")
                when (type) {
                    "DOSE" -> {
                        val vm: DoseRecordViewModel = viewModel(
                            factory = DoseRecordViewModel.provideFactory(
                                application, drugId?.takeIf { it.isNotBlank() },
                            ),
                        )
                        DoseRecordScreen(
                            onBack = { navController.popBackStack() },
                            viewModel = vm,
                        )
                    }
                    else -> {
                        val vm: EntryEditViewModel = viewModel(
                            factory = EntryEditViewModel.provideFactory(
                                application, type, uri?.takeIf { it.isNotBlank() }, 0L,
                            ),
                        )
                        EntryEditScreen(
                            onBack = { navController.popBackStack() },
                            viewModel = vm,
                        )
                    }
                }
            }
            composable(
                route = Routes.ENTRY_DETAIL,
                arguments = listOf(navArgument("entryId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val entryId = backStackEntry.arguments?.getLong("entryId") ?: 0L
                val vm: EntryDetailViewModel = viewModel(
                    factory = EntryDetailViewModel.provideFactory(application, entryId),
                )
                EntryDetailScreen(
                    onNavigate = { route -> navController.navigate(route) },
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.ENTRY_EDIT,
                arguments = listOf(navArgument("entryId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val entryId = backStackEntry.arguments?.getLong("entryId") ?: 0L
                // 编辑已有条目时复用详情 VM 读取类型不可行，这里通过详情数据流读取
                val vm: EntryEditViewModel = viewModel(
                    factory = EntryEditViewModel.provideFactory(application, "", null, entryId),
                )
                EntryEditScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.SCHEDULE_LIST) {
                val vm: ScheduleListViewModel = viewModel(
                    factory = ScheduleListViewModel.provideFactory(application),
                )
                ScheduleListScreen(
                    onNavigate = { route -> navController.navigate(route) },
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.REPORTS) {
                val vm: ReportListViewModel = viewModel(
                    factory = ReportListViewModel.provideFactory(application),
                )
                ReportListScreen(
                    onOpenReport = { fileName -> navController.navigate(Routes.reportReader(fileName)) },
                    onEditReport = { fileName -> navController.navigate(Routes.reportEdit(fileName)) },
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.REPORT_READER,
                arguments = listOf(
                    navArgument("fileName") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val fileName = backStackEntry.arguments?.getString("fileName").orEmpty()
                val vm: ReportReaderViewModel = viewModel(
                    factory = ReportReaderViewModel.provideFactory(
                        application, fileName,
                    ),
                )
                ReportReaderScreen(
                    fileName = fileName,
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.PRESCRIPTIONS) {
                val vm: PrescriptionListViewModel = viewModel(
                    factory = PrescriptionListViewModel.provideFactory(application),
                )
                PrescriptionListScreen(
                    onBack = { navController.popBackStack() },
                    onEdit = { displayName ->
                        navController.navigate(Routes.prescriptionEdit(displayName))
                    },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.PRESCRIPTION_EDIT,
                arguments = listOf(
                    navArgument("displayName") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val displayName = backStackEntry.arguments?.getString("displayName").orEmpty()
                val vm: PrescriptionEditViewModel = viewModel(
                    factory = PrescriptionEditViewModel.provideFactory(
                        application, displayName,
                    ),
                )
                PrescriptionEditScreen(
                    displayName = displayName,
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(
                route = Routes.REPORT_EDIT,
                arguments = listOf(navArgument("fileName") { type = NavType.StringType }),
            ) { backStackEntry ->
                val fileName = backStackEntry.arguments?.getString("fileName").orEmpty()
                val vm: ReportEditViewModel = viewModel(
                    factory = ReportEditViewModel.provideFactory(
                        application, fileName,
                    ),
                )
                ReportEditScreen(
                    fileName = fileName,
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.ALERTS) {
                val vm: AlertViewModel = viewModel(
                    factory = AlertViewModel.provideFactory(application),
                )
                AlertScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.PROFILE) {
                val vm: ProfileViewModel = viewModel(
                    factory = ProfileViewModel.provideFactory(application),
                )
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.DATA_MANAGEMENT) {
                val vm: DataManagementViewModel = viewModel(
                    factory = DataManagementViewModel.provideFactory(application),
                )
                DataManagementScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = vm,
                )
            }
            composable(Routes.HANDBOOK) {
                HandbookScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.ABOUT) { AboutScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.DISCLAIMER) { DisclaimerScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
