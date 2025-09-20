package com.example.plugin

import android.util.Log
import io.nightfish.lightnovelreader.api.plugin.LightNovelReaderPlugin
import io.nightfish.lightnovelreader.api.plugin.Plugin

@Plugin(
    version = 0,
    name = "example",
    versionName = "0.0.1",
    author = "none",
    description = " a example plugin",
    updateUrl = "http://example.org"
)
class ExamplePlugin: LightNovelReaderPlugin {
    override fun onLoad() {
        Log.i("Plugin", "ciallo~")
    }
}