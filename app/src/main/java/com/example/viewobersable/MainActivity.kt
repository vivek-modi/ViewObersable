package com.example.viewobersable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.viewobersable.ui.theme.ViewObersableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewObersableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SaveVariable()
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun SaveVariable() {
    val viewModel = VariableViewModel()
    val pagerState = rememberPagerState()
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.currentIndex = page
        }
    }
//    this code is causing issue
//    LaunchedEffect(key1 = viewModel.setLastCurrentPage) {
//        Log.e(">> LaunchedEffect", "${viewModel.currentIndex}")
//    }
    SaveVariableItem(viewModel.currentIndex, viewModel.uiState, pagerState) {
        viewModel.changeUIi()
        viewModel.setLastCurrentPage = true
    }
}

class VariableViewModel : ViewModel() {
    var uiState by mutableStateOf(ScreenName.FIRST)
    var setLastCurrentPage by mutableStateOf(false)
    var currentIndex by mutableStateOf(0)
    fun changeUIi() {
        uiState = if (uiState == ScreenName.FIRST) {
            ScreenName.SECOND
        } else {
            ScreenName.FIRST
        }
    }
}

enum class ScreenName {
    FIRST,
    SECOND
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaveVariableItem(
    currentIndex: Int,
    uiState: ScreenName,
    pagerState: PagerState,
    changeUiState: () -> Unit,
) {
    Column {
        when (uiState) {
            ScreenName.FIRST -> {
                ScreenFirstView(pagerState)
            }

            ScreenName.SECOND -> {
                ScreenSecondView(currentIndex, "SECOND")
            }
        }
        Button(onClick = { changeUiState() }) {
            Text(text = "Add")
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenFirstView(pagerState: PagerState) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Black)
    ) {
        HorizontalPager(
            modifier = Modifier.padding(20.dp),
            pageCount = 10,
            state = pagerState
        ) { page ->
            Text(
                text = "Page: $page",
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
        }
    }
}

@Composable
fun ScreenSecondView(currentIndex: Int, screenImage: String) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Black)
    ) {
        Text(text = "$currentIndex ==== $screenImage", color = Color.White)
    }
}