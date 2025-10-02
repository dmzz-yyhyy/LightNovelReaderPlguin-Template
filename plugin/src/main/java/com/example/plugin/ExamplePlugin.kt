package com.example.plugin

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.nightfish.lightnovelreader.api.plugin.LightNovelReaderPlugin
import io.nightfish.lightnovelreader.api.plugin.Plugin
import io.nightfish.lightnovelreader.api.ui.components.SettingsClickableEntry
import io.nightfish.lightnovelreader.api.ui.components.SettingsSwitchEntry
import io.nightfish.lightnovelreader.api.userdata.UserDataRepositoryApi

@Suppress("unused")
@Plugin(
    version = 0,
    name = "example",
    versionName = "0.0.1",
    author = "none",
    description = " a example plugin",
    updateUrl = "http://example.org"
)
class ExamplePlugin(
    val userDataRepositoryApi: UserDataRepositoryApi
) : LightNovelReaderPlugin {
    override fun onLoad() {
        Log.i("Plugin", "Ciallo～(∠・ω< )⌒★")
    }

    @Composable
    override fun PageContent(paddingValues: PaddingValues) {
        val content = LocalContext.current
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val checked by userDataRepositoryApi.booleanUserData("TestBooleanUserData").getFlowWithDefault(true).collectAsState(true)
            SettingsSwitchEntry(
                modifier = Modifier.background(colorScheme.surfaceContainer),
                title = "测试选项",
                description = "Ciallo～(∠・ω< )⌒★",
                checked = checked,
                booleanUserData = userDataRepositoryApi.booleanUserData("TestBooleanUserData")
            )
            SettingsClickableEntry(
                modifier = Modifier.background(colorScheme.surfaceContainer),
                title = "测试点击",
                description = "0721",
                onClick = {
                    Toast.makeText(content, "带面纸了吗", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}