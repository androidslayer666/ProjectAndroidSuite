package com.example.projectandroidsuite.ui.mainscreen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.di.DatabaseProvider
import com.example.projectandroidsuite.MainActivity
import com.example.projectandroidsuite.newui.ProjectTheme
import com.example.projectandroidsuite.newui.utils.ComposeTestDescriptions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseProvider::class)
class FilterProjectsKtTest{

    @get:Rule(order =0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order =1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            ProjectTheme{
                FilterProjects(viewModel = hiltViewModel())
            }
        }
    }

    @Test
    fun filterProjectsKtTest_assertBodyExist(){
        composeRule.onNodeWithContentDescription(ComposeTestDescriptions.FILTER_PROJECTS).assertExists()
    }

    @Test
    fun filterProjectsKtTest_assertListUsersIsShown(){
        composeRule.onNodeWithContentDescription(ComposeTestDescriptions.BUTTON_SHOW_USERS).performClick()
        composeRule.onNodeWithContentDescription(ComposeTestDescriptions.TEAM_PICKER_DIALOG).assertExists()
    }

}