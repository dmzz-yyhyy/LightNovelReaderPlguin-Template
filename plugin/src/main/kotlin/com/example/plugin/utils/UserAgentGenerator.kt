package com.example.plugin.utils

import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.random
import kotlin.random.Random

object UserAgentGenerator {

    // 主流浏览器及版本范围
    private val chromeVersions = (90..115).map { "Chrome/$it.0.${Random.nextInt(1000, 9999)}.${Random.nextInt(100)}" }
    private val firefoxVersions = (90..115).map { "Firefox/$it.0" }
    private val safariVersions = (14..17).map { "Version/$it.${Random.nextInt(0, 5)}" }
    private val edgeVersions = (90..115).map { "Edg/$it.0.${Random.nextInt(1000, 9999)}.${Random.nextInt(100)}" }

    // 操作系统配置
    private val windowsPlatforms = listOf(
        "(Windows NT 10.0; Win64; x64)",
        "(Windows NT 11.0; Win64; x64)"
    )

    private val macPlatforms = listOf(
        "(Macintosh; Intel Mac OS X 10_15_7)",
        "(Macintosh; Intel Mac OS X 11_0)",
        "(Macintosh; Intel Mac OS X 12_0)",
        "(Macintosh; Apple Silicon)"
    )

    private val linuxPlatforms = listOf(
        "(X11; Linux x86_64)",
        "(X11; Ubuntu; Linux x86_64)"
    )

    // 渲染引擎
    private val webkitEngines = listOf(
        "AppleWebKit/537.36 (KHTML, like Gecko)",
        "AppleWebKit/605.1.15 (KHTML, like Gecko)"
    )

    private val geckoEngines = listOf(
        "Gecko/20100101"
    )

    // 生成随机UA的主函数
    fun generate(): String {
        return when (Random.nextInt(0, 100)) {
            in 0..69 -> generateChromeBased()   // 70% 概率 Chrome/Edge
            in 70..89 -> generateFirefox()      // 20% 概率 Firefox
            else -> generateSafari()            // 10% 概率 Safari
        }
    }

    private fun generateChromeBased(): String {
        val isEdge = Random.nextBoolean()
        val platform = getRandomPlatform()

        return listOf(
            "Mozilla/5.0",
            platform,
            webkitEngines.random(),
            if (isEdge) {
                "${chromeVersions.random()} Safari/537.36 ${edgeVersions.random()}"
            } else {
                "${chromeVersions.random()} Safari/537.36"
            }
        ).joinToString(" ")
    }

    private fun generateFirefox(): String {
        return listOf(
            "Mozilla/5.0",
            getRandomPlatform(),
            "rv:${Random.nextInt(90, 115)}.0",
            geckoEngines.random(),
            firefoxVersions.random()
        ).joinToString(" ")
    }

    private fun generateSafari(): String {
        return listOf(
            "Mozilla/5.0",
            macPlatforms.random(),  // Safari 主要存在于 macOS
            webkitEngines.random(),
            safariVersions.random(),
            "Safari/605.1.15"
        ).joinToString(" ")
    }

    private fun getRandomPlatform(): String {
        return when (Random.nextInt(0, 100)) {
            in 0..79 -> windowsPlatforms.random()  // 80% Windows
            in 80..94 -> macPlatforms.random()    // 15% macOS
            else -> linuxPlatforms.random()        // 5% Linux
        }
    }
}