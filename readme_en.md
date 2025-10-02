[简体中文](readme.md)  | **English**

<div align="center">
    <h1>LightNovelReaderPlugin</h1>
    <a><img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white&style=for-the-badge"/></a>
    <a><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-0095D5.svg?logo=kotlin&logoColor=white&style=for-the-badge"/></a>
    <a><img alt="Jetpack Compose" src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white&style=for-the-badge"></a>
    <a href="http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=P__gXIArh5UDBsEq7ttd4WhIYnNh3y1t&authKey=GAsRKEZ%2FwHpzRv19hNJsDnknOc86lYzNIHMPy2Jxt3S3U8f90qestOd760IAj%2F3l&noverify=0&group_code=867785526"><img alt="QQ Group" src="https://img.shields.io/badge/QQ%20Group-867785526-brightgreen.svg?logoColor=white&style=for-the-badge"></a>
    <a href="https://discord.gg/pnf4ABmDJt"><img alt="Discord" src="https://img.shields.io/badge/Discord-JOIN-4285F4.svg?logo=discord&logoColor=white&style=for-the-badge"></a>
    <a href="https://t.me/lightnoble"><img alt="Telegram" src="https://img.shields.io/badge/Telegram-JOIN-188FCA.svg?logo=telegram&logoColor=white&style=for-the-badge"></a>
</div>

## Introduction

This is a plugin template for the light novel reader app [LightNovelReader](https://github.com/dmzz-yyhyy/LightNovelReader).

We highly welcome plugin development! You can reach out to us in the following ways:

- Submit a bug report or feature request [**here**](https://github.com/dmzz-yyhyy/LightNovelReader/issues/new/choose)
- Join the QQ group: `867785526` | [**Invitation Link**](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=P__gXIArh5UDBsEq7ttd4WhIYnNh3y1t&authKey=GAsRKEZ%2FwHpzRv19hNJsDnknOc86lYzNIHMPy2Jxt3S3U8f90qestOd760IAj%2F3l&noverify=0&group_code=867785526)
- Join our Discord server: [**Invitation Link**](https://discord.gg/pnf4ABmDJt)
- Join the Telegram group: [**Invitation Link**](https://t.me/lightnoble)  

We'll do our best to help you with plugin development.  
And if you like this project, don’t forget to give it a ⭐!

---

### LNR API

This is an API for LightNovelReader.  
The API is currently still limited, but more features will be added gradually. We will try our best to maintain **binary compatibility**.

*Note: At the moment, the app does not load resource files when loading plugins.*

---

#### Creating a Plugin

Your plugin must contain **exactly one** class that implements the `LightNovelReaderPlugin` interface, with the `@Plugin` annotation.  

If the entry point cannot be found, the plugin (including data sources) will not load. If multiple entry points are found, only one will be loaded (the order is undefined).

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
        // code executed when the plugin is loaded
    }
}
````

You can also inject internal APIs through constructor parameters. The app will automatically provide the implementations when initializing the plugin.

```kotlin
@Plugin(
    version = 0,
    name = "example",
    versionName = "0.0.1",
    author = "none",
    description = "a example plugin",
    updateUrl = "http://example.org"
)
class ExamplePlugin(
  val userDataRepositoryApi: UserDataRepositoryApi,
  val userDataDaoApi:   UserDataDaoApi,
  val userDataRepositoryApi: UserDataRepositoryApi,
  val webBookDataSourceManagerApi: WebBookDataSourceManagerApi,
  val textProcessingRepositoryApi: TextProcessingRepositoryApi,
  val localBookDataSourceApi: LocalBookDataSourceApi,
  val bookRepositoryApi: BookRepositoryApi,
  val bookshelfRepositoryApi: BookshelfRepositoryApi,
) : LightNovelReaderPlugin {
    override fun onLoad() {
        // code executed when the plugin is loaded
    }
}
```

You can also override the `PageContent` composable function to show your own UI inside the plugin page.

```kotlin
@Plugin(
    //......
)
class ExamplePlugin(
    //......
) : LightNovelReaderPlugin {
  //......
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
        title = "Test Option",
        description = "Ciallo～(∠・ω< )⌒★",
        checked = checked,
        booleanUserData = userDataRepositoryApi.booleanUserData("TestBooleanUserData")
      )
      SettingsClickableEntry(
        modifier = Modifier.background(colorScheme.surfaceContainer),
        title = "Test Click",
        description = "0721",
        onClick = {
          Toast.makeText(content, "Did you bring tissues?", Toast.LENGTH_LONG).show()
        }
      )
    }
  }
}
```

---

#### Writing Your Data Source

If you're unsure about how to implement a data source, check out the [default implementation](https://github.com/dmzz-yyhyy/LightNovelReader/tree/refactoring/app/src/main/kotlin/indi/dmzz_yyhyy/lightnovelreader/defaultplugin) in the main repository.

##### Main Data Source

A data source must fully implement all non-default functions in the `WebBookDataSource` interface.

Additionally, the data source must be defined as an `object` or have a no-argument constructor.

```kotlin
@WebDataSource(
    name = "ExampleWebDataSource",
    provider = "example.com"
)
object ExampleWebDataSource : WebBookDataSource {
    //......
}
```

Below is the definition of the `WebBookDataSource` interface.

Functions with default implementations are optional to override, but implementing them all can provide a better user experience.

```kotlin
/**
 * Web data source interface for LightNovelReader.
 * By implementing this interface, you can add support for new data sources.
 * Version: 0.4.1
 */
interface WebBookDataSource {
    val id: Int

    /**
     * Check whether the application is currently in offline mode.
     */
    suspend fun isOffLine(): Boolean

    /**
     * Whether the application is currently in offline mode.
     */
    val offLine: Boolean

    /**
     * A data flow indicating whether the application is currently in offline mode.
     * This should be a hot flow, constantly updating the status.
     */
    val isOffLineFlow: Flow<Boolean>

    /**
     * A list of IDs for all explore page data sources.
     */
    val explorePageIdList: List<String>

    /**
     * A map of explore page data source IDs to their corresponding data sources.
     * This function must be thread-safe.
     */
    val explorePageDataSourceMap: Map<String, ExplorePageDataSource>

    /**
     * A map of expanded explore page IDs to their corresponding data sources.
     * This function must be thread-safe.
     */
    val exploreExpandedPageDataSourceMap: Map<String, ExploreExpandedPageDataSource>

    /**
     * A map of search type IDs to their display names.
     */
    val searchTypeMap: Map<String, String>

    /**
     * A map of search type IDs to their search bar placeholder text.
     */
    val searchTipMap: Map<String, String>

    /**
     * An ordered list of search type IDs.
     */
    val searchTypeIdList: List<String>

    /**
     * Blocking function to retrieve book metadata.
     * Thread safety is not required. Should remain blocking until data is retrieved.
     * Implement your own reconnection and retry logic if necessary.
     *
     * @param id Book ID
     * @return Formatted book metadata, or BookInformation.empty() if not found
     */
    suspend fun getBookInformation(id: Int): BookInformation

    /**
     * Blocking function to retrieve the volume and chapter structure of a book.
     * Thread safety is not required. Should remain blocking until data is retrieved.
     * Implement your own reconnection and retry logic if necessary.
     *
     * @param id Book ID
     * @return Formatted volume data, or BookVolumes.empty if the book is not found
     */
    suspend fun getBookVolumes(id: Int): BookVolumes

    /**
     * Blocking function to retrieve the content of a chapter.
     * Thread safety is not required. Should remain blocking until data is retrieved.
     * Implement your own reconnection and retry logic if necessary.
     *
     * @param chapterId Chapter ID
     * @param bookId ID of the book this chapter belongs to
     * @return Formatted chapter content, or ChapterContent.empty() if not found
     */
    suspend fun getChapterContent(chapterId: Int, bookId: Int): ChapterContent

    /**
     * Execute a search task.
     *
     * Should return a hot flow of search results.
     * An empty book metadata object 
     * [io.nightfish.lightnovelreader.api.book.BookInformation.Companion.empty]
     * should be appended to the list to indicate the end of the search.
     * This function itself must be thread-safe.
     *
     * @param searchType Search type/category
     * @param keyword Search keyword
     * @return A flow of search result lists
     */
    fun search(searchType: String, keyword: String): Flow<List<BookInformation>>

    /**
     * Stop all currently running search tasks.
     * This function must be thread-safe.
     */
    fun stopAllSearch()

    /**
     * Handle navigation when a book tag is clicked.
     */
    fun progressBookTagClick(tag: String, navController: NavController) {}

    /**
     * Retrieve the cover image URL of a specific volume, used for EPUB export.
     * Return null if not available.
     *
     * @param bookId Book ID
     * @param volume The volume whose cover is requested
     * @param volumeChapterContentMap A map containing all chapters of the volume, keyed by chapter ID
     */
    fun getCoverUrlInVolume(
        bookId: Int,
        volume: Volume,
        volumeChapterContentMap: Map<Int, ChapterContent>
    ): String? = null
}
```

##### Explore Pages

The explore feature is one of the more complex parts of a data source.

It consists of `ExplorePageDataSource` and `ExploreExpandedPageDataSource`, which provide the data for the primary and secondary explore pages.

*To be continued...*