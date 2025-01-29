package com.jsanzo.figerprintshortcut.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainActivityScreen(
    enableService: (value: Boolean) -> Unit,
    enableNotification: (value: Boolean) -> Unit,
    enableInCamera: (value: Boolean) -> Unit,
    onSlideUp: () -> Unit,
    onSlideDown: () -> Unit,
    onSlideLeft: () -> Unit,
    onSlideRight: () -> Unit,
) {
    MainActivityLayout(
        enableService = enableService,
        enableNotification = enableNotification,
        enableInCamera = enableInCamera,
        onSlideUp = onSlideUp,
        onSlideDown = onSlideDown,
        onSlideLeft = onSlideLeft,
        onSlideRight = onSlideRight
    )
}

@Composable
fun MainActivityLayout(
    enableService: (value: Boolean) -> Unit,
    enableNotification: (value: Boolean) -> Unit,
    enableInCamera: (value: Boolean) -> Unit,
    onSlideUp: () -> Unit,
    onSlideDown: () -> Unit,
    onSlideLeft: () -> Unit,
    onSlideRight: () -> Unit,
) {
    var serviceEnabled by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(false) }
    var inCameraEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Ajustes Generales",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsRow(
            title = "Activar FingerShortcut",
            description = "FingerShortcut esta desactivado",
            trailElement = {
                Switch(
                    checked = serviceEnabled,
                    onCheckedChange = { value ->
                        serviceEnabled = value
                        enableService(value)
                    }
                )
            }
        )

        RowDivider()

        SettingsRow(
            title = "Activar notificacion",
            description = "Mantener una notificacion activa en la barra de tareas",
            trailElement = {
                Checkbox(
                    checked = notificationEnabled,
                    onCheckedChange = { value ->
                        notificationEnabled = value
                        enableNotification(value)
                    }
                )
            }
        )

        RowDivider()

        SettingsRow(
            title = "Activar FingerShortcut en la camara",
            description = "Desliza en cualquier direccion para hacer una foto cuando estes usando la camara",
            trailElement = {
                Checkbox(
                    checked = inCameraEnabled,
                    onCheckedChange = { value ->
                        inCameraEnabled = value
                        enableInCamera(value)
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Gestos con la huella",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsRow(
            title = "Deslizar Arriba",
            description = "Nada",
            onRowClick = onSlideUp
        )

        RowDivider()

        SettingsRow(
            title = "Deslizar Abajo",
            description = "Nada",
            onRowClick = onSlideDown
        )

        RowDivider()

        SettingsRow(
            title = "Deslizar Derecha",
            description = "Nada",
            onRowClick = onSlideRight
        )

        RowDivider()

        SettingsRow(
            title = "Deslizar Izquierda",
            description = "Nada",
            onRowClick = onSlideLeft
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    description: String,
    trailElement: @Composable () -> Unit = {},
    onRowClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRowClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(9f)
        ) {
            Text(
                text = title,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        trailElement()
    }
}

@Composable
fun RowDivider() {
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(color = Color.Gray, thickness = 1.dp)
    Spacer(modifier = Modifier.height(12.dp))
}


@Composable
@Preview(
    showBackground = true,
    widthDp = 420
)
fun MainActivityLayoutPreview() {
    MainActivityLayout(
        enableService = {},
        enableNotification = {},
        enableInCamera = {},
        onSlideUp = {},
        onSlideDown = {},
        onSlideLeft = {},
        onSlideRight = {}
    )
}