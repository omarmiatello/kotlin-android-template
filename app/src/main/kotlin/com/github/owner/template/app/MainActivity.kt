package com.github.owner.template.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.owner.template.app.ui.theme.TemplateTheme
import com.github.owner.template.library.FactorialCalculator
import com.github.owner.template.library.android.NotificationUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemplateTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    FactorialLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FactorialLayout() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "This is just a template",
            style = MaterialTheme.typography.h3,
        )
        Text(
            text = "You can compute a factorial using the library-kotlin module. The result will " +
                    "be also shown in a notification using the library-android module.",
            style = MaterialTheme.typography.body1,
        )

        var factorial by remember { mutableStateOf("42") }
        TextField(
            value = factorial,
            onValueChange = { factorial = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        val result by derivedStateOf {
            FactorialCalculator.computeFactorial(input = factorial.toIntOrNull() ?: 0)
        }
        val context = LocalContext.current
        val notificationUtil = remember { NotificationUtil(context) }
        Button(
            onClick = {
                notificationUtil.showNotification(
                    context = context,
                    title = "This is just a template",
                    message = "The Result is: $result"
                )
            }
        ) {
            Text(text = "Show notification")
        }
        AnimatedVisibility(visible = result != 1L) {
            Text(
                text = "The Result is: $result",
                style = MaterialTheme.typography.h4,
            )
        }
    }
}
