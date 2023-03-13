package org.example.app.features.news

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import org.example.app.di.AppComponent
import org.example.app.navigation.Screen
import org.example.app.navigation.defaultScreenName
import org.example.app.ui.EmptyDataConfig
import org.example.app.ui.ResourceContent
import org.example.app.ui.ScreenContent
import org.example.library.feature.list.model.News

object NewsScreen : Screen {

    override val screenName = defaultScreenName()

    @Composable
    override fun Content(navController: NavController, args: Bundle?) {
        val viewModel = viewModel {
            AppComponent.factory.newsFactory.createListViewModel()
        }

        val state by viewModel.state.observeAsState()

        ScreenContent(
            toolbar = {
                TopAppBar(
                    title = {
                        Text(text = "News")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }

                    },
                    elevation = 8.dp
                )
            }
        ) {
            ResourceContent(
                state = state,
                emptyDataConfig = EmptyDataConfig(
                    message = state.errorValue()?.toString(LocalContext.current).orEmpty(),
                    onRetryClick = viewModel::onRetryClick
                )
            ) { newsList: List<News> ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    itemsIndexed(newsList) { index, news ->
                        NewsItem(
                            id = index.toLong(),
                            title = news.title,
                            description = news.description,
                            isDividerShowed = index != newsList.lastIndex,
                            onClick = {
                                println("onClick: id: ${news.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    id: Long,
    title: String?,
    description: String?,
    isDividerShowed: Boolean,
    onClick: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = MaterialTheme.colors.secondary
                    ),
                    onClick = onClick
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            if (title != null) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(MaterialTheme.colors.surface)
                )
            }
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        if (isDividerShowed) {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray,
                thickness = 1.dp,
            )
        }
    }
}