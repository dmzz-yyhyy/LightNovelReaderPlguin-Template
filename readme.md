<div align="center">
    <h1>LightNovelReaderPlugin</h1>
    <a><img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white&style=for-the-badge"/></a>
    <a><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-0095D5.svg?logo=kotlin&logoColor=white&style=for-the-badge"/></a>
    <a><img alt="Jetpack Compose" src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white&style=for-the-badge"></a>
    <a href="http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=P__gXIArh5UDBsEq7ttd4WhIYnNh3y1t&authKey=GAsRKEZ%2FwHpzRv19hNJsDnknOc86lYzNIHMPy2Jxt3S3U8f90qestOd760IAj%2F3l&noverify=0&group_code=867785526"><img alt="QQ Group" src="https://img.shields.io/badge/QQ讨论群-867785526-brightgreen.svg?logoColor=white&style=for-the-badge"></a>
    <a href="https://discord.gg/fCxRfsFS"><img alt="Discord" src="https://img.shields.io/badge/Discord-JOIN-4285F4.svg?logo=discord&logoColor=white&style=for-the-badge"></a>
    <a href="https://t.me/lightnoble"><img alt="Discord" src="https://img.shields.io/badge/Telegram-JOIN-188FCA.svg?logo=telegram&logoColor=white&style=for-the-badge"></a>
</div>

## 简介

这是用于轻小说阅读器[LightNovelReader](https://github.com/dmzz-yyhyy/LightNovelReader)的一个插件模板

我们非常欢迎对于插件的开发, 您可以通过以下方式联系我们

- 在 [**此处**](https://github.com/dmzz-yyhyy/LightNovelReader/issues/new/choose) 提交一个 Bug
  反馈或新功能请求
- 欢迎加入 QQ 讨论群：`867785526` | [**邀请链接**](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=P__gXIArh5UDBsEq7ttd4WhIYnNh3y1t&authKey=GAsRKEZ%2FwHpzRv19hNJsDnknOc86lYzNIHMPy2Jxt3S3U8f90qestOd760IAj%2F3l&noverify=0&group_code=867785526)
- 欢迎加入 Discord 服务器：[**邀请链接**](https://discord.gg/fCxRfsFS)
- 欢迎加入 Telegram 讨论群组：[**邀请链接**](https://t.me/lightnoble)
  我们会尽量解决您在插件开发中遇到的问题

喜欢的话不要忘记点个star噢!

### LNR Api

这是一套适用于LightNovelReader的api
目前api的内容不多, 后续会逐渐添加, 我们会**尽量**保证该api的二进制兼容性

*注意: 目前软件在加载插件时并不会加载资源文件

#### 创建一个插件

你需要确保插件内有且仅有一个实现了```LightNovelReaderPlugin```接口, 并且为其添加```@Plugin```注解

当软件无法找到入口点时, 将不会加载插件(包括数据源), 当存在多个时, 软件仅会加载一个入口点(
加载顺序属于未定义行为)

```kotlin
@Plugin(
    version = 0,
    name = "example",
    versionName = "0.0.1",
    author = "none",
    description = "a example plugin",
    updateUrl = "http://example.org"
)
class ExamplePlugin : LightNovelReaderPlugin {
    override fun onLoad() {
        //write something that will be execute when the plugin is loaded
    }
}
```

#### 编写自己的数据源

编写数据源时如果有搞不懂的地方可以去看软件仓库的[默认数据源实现](https://github.com/dmzz-yyhyy/LightNovelReader/tree/refactoring/app/src/main/kotlin/indi/dmzz_yyhyy/lightnovelreader/defaultplugin)

##### 主要数据源

一个数据源必须**完全**实现WebDataSource接口中没有默认实现的函数

同时, 你必须确数据源属于```object```或默认构造器无需传参

```kotlin
@WebDataSource(
    name = "ExampleWebDataSource",
    provider = "example.com"
)
object ExampleWebDataSource : WebBookDataSource {
    ......
}
```

以下是接口```ExampleWebDataSource```的定义

对于有默认实现的函数, 是否重写是可选的, 但是完全实现```ExampleWebDataSource```可以获得更好的体验

```kotlin
/**
 * LightNovelReader 的网络数据提供源接口，可以通过实现此接口使软件支持新的数据源
 * 版本: 0.4.1
 */
interface WebBookDataSource {
    val id: Int

    /**
     * 获取当前软件整体是否处于离线状态
     */
    suspend fun isOffLine(): Boolean

    /**
     * 当前软件整体是否处于离线状态
     */
    val offLine: Boolean

    /**
     * 获取当前软件整体是否处于离线状态的数据流
     * 此数据流应当为热数据流, 并且不断对状态进行更新
     */
    val isOffLineFlow: Flow<Boolean>

    /**
     * 所有探索页页面数据源的id
     */
    val explorePageIdList: List<String>

    /**
     * 获取探索页面数据源的id和页面数据源的对应表
     * 此函数应当保证主线程安全
     */
    val explorePageDataSourceMap: Map<String, ExplorePageDataSource>

    /**
     * 获取各个探索页横栏的展开页的id与展开页数据源的对应表
     * 此函数应当保证主线程安全
     */
    val exploreExpandedPageDataSourceMap: Map<String, ExploreExpandedPageDataSource>

    /**
     * 搜索类型id和名称的对应表
     */
    val searchTypeMap: Map<String, String>

    /**
     * 搜索类型id和搜索栏提示的对应表
     */
    val searchTipMap: Map<String, String>

    /**
     * 搜索类型id的有序列表
     */
    val searchTypeIdList: List<String>

    /**
     * 此函数无需保证主线程安全性, 为阻塞函数, 获取到数据前应当保持阻塞
     * 此函数应当自行实现断线重连等逻辑
     *
     * @param id 书本id
     * @return 经过格式化后的书本元数据, getBookInformation.empty()
     */
    suspend fun getBookInformation(id: Int): BookInformation

    /**
     * 此函数无需保证主线程安全性, 为阻塞函数, 获取到数据前应当保持阻塞
     * 此函数应当自行实现断线重连等逻辑
     *
     * @param id 书本id
     * @return 经过格式化后的书本章节目录数据, 如未找到改书则返回BookVolumes.empty
     */
    suspend fun getBookVolumes(id: Int): BookVolumes

    /**
     * 此函数无需保证主线程安全性, 为阻塞函数, 获取到数据前应当保持阻塞
     * 此函数应当自行实现断线重连等逻辑
     *
     * @param chapterId 章节id
     * @param bookId 章节所属书本id
     * @return 经过格式化后的书本章节类容录数据, 如未找到改书则返回ChapterContent.empty()
     */
    suspend fun getChapterContent(chapterId: Int, bookId: Int): ChapterContent

    /**
     * 执行搜索任务
     *
     * 应当返回搜索结果的热数据流
     * 并且以空书本元数据[io.nightfish.lightnovelreader.api.book.BookInformation.Companion.empty]作为列表结尾时表示搜索结束
     * 此函数本身应当保证主线程安全
     *
     * @param searchType 搜索类别
     * @param keyword 搜索关键词
     * @return 搜索结果的数据流
     */
    fun search(searchType: String, keyword: String): Flow<List<BookInformation>>

    /**
     * 停止当前所执行的所有搜索任务
     * 此函数应当保证主线程安全
     *
     */
    fun stopAllSearch()

    /**
     * 用于处理书本tag的点击跳转事件
     */
    fun progressBookTagClick(tag: String, navController: NavController) {}

    /**
     * 根据卷获取该卷封面的Url, 用于EPUB分卷导出
     * 如无, 则返回null
     *
     * @param bookId 书本id
     * @param volume 需要搜索封面的卷id
     * @param volumeChapterContentMap 包含搜索卷全部章节的Map, 以章节id为key
     */
    fun getCoverUrlInVolume(
        bookId: Int,
        volume: Volume,
        volumeChapterContentMap: Map<Int, ChapterContent>
    ): String? = null
}
```

##### 探索页面

探索页面的数据提供是一个很复杂的部分

探索页面分为```ExplorePageDataSource```和```ExploreExpandedPageDataSource```
他们两个分别提供了探索页面的一级页面数据和二级页面数据

未完待续......
