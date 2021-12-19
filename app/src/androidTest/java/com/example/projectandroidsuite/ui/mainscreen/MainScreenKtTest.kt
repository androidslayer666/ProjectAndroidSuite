package com.example.projectandroidsuite.ui.mainscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.domain.di.DatabaseProvider
import com.example.projectandroidsuite.MainActivity
import com.example.projectandroidsuite.newui.ProjectTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseProvider::class)
class MainScreenKtTest{

    @get:Rule(order =0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order =1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            ProjectTheme{
                MainScreen(navController = navController)
            }
        }
    }

    @Test
    fun projectsPageTest_checkProjectFilterIsVisible() {
        composeRule.onNodeWithContentDescription("Filter Toggler").performClick()
        composeRule.onNodeWithContentDescription("FilterProjectsComponent").assertExists()
    }

    @Test
    fun projectsPageTest_checkTaskFilterIsVisible() {
        composeRule.onNodeWithTag("ProjectPageTab1").performClick()
        composeRule.onNodeWithContentDescription("Filter Toggler").performClick()
        composeRule.onNodeWithContentDescription("TaskProjectsComponent").assertExists()
    }

    @Test
    fun projectsPageTest_checkListIsVisibleWhenStateIsRelevant() {
        composeRule.onNodeWithContentDescription("ProjectListOnProjectPage").assertExists()
        composeRule.onNodeWithTag("ProjectPageTab1").performClick()
        composeRule.onNodeWithContentDescription("TaskListOnProjectPage").assertIsDisplayed()
    }


}