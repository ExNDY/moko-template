package org.example.app.features.config

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import org.example.app.R
import org.example.app.di.AppComponent
import org.example.app.features.news.NewsScreen
import org.example.app.navigation.Screen
import org.example.app.navigation.defaultScreenName
import org.example.app.ui.ScreenContent
import org.example.app.utils.observeAsMutableState
import org.example.library.feature.config.presentation.ConfigViewModel.Action.RouteToNews

object ConfigScreen : Screen {

    override val screenName = defaultScreenName()

    @Composable
    override fun Content(navController: NavController, args: Bundle?) {
        val viewModel = viewModel {
            AppComponent.factory.configFactory.createConfigViewModel()
        }

        viewModel.actions.observeAsActions { action ->
            when (action) {
                RouteToNews -> navController.navigate(route = NewsScreen.screenName)
            }
        }

        val (token, tokenSetter) = viewModel.apiTokenField.data.observeAsMutableState()
        val (lang, langSetter) = viewModel.languageField.data.observeAsMutableState()
        val tokenError = viewModel.apiTokenField.error.observeAsState()
        val langError = viewModel.languageField.error.observeAsState()

        ScreenContent(
            toolbar = {
                TopAppBar(
                    title = {
                        Text(text = "Config")
                    },
                    elevation = 8.dp
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = token,
                    onValueChange = tokenSetter,
                    label = {
                        Text(text = stringResource(R.string.token_hint))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.token_hint))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = tokenError.value?.toString(LocalContext.current) ?: "",
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lang,
                    onValueChange = langSetter,
                    label = {
                        Text(text = stringResource(R.string.language_hint))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.language_hint))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = langError.value?.toString(LocalContext.current) ?: "",
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = viewModel::onSubmitPressed,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.submit_btn),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}
