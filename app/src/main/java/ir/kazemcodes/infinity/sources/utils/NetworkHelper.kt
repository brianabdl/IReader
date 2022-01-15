package ir.kazemcodes.infinity.sources.utils

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import ir.kazemcodes.infinity.data.network.models.*
import ir.kazemcodes.infinity.data.network.utils.AndroidCookieJar
import ir.kazemcodes.infinity.data.network.utils.UserAgentInterceptor
import ir.kazemcodes.infinity.data.network.utils.WebViewClientCompat
import ir.kazemcodes.infinity.data.network.utils.intercepter.CloudflareInterceptor
import ir.kazemcodes.infinity.domain.use_cases.preferences.PreferencesUseCase
import ir.kazemcodes.infinity.util.getHtml
import kotlinx.coroutines.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber
import uy.kohesive.injekt.injectLazy
import java.io.File
import java.util.concurrent.TimeUnit


class NetworkHelper(private val context: Context)  {


    val preferencesUseCase: PreferencesUseCase by injectLazy()

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val cacheDir = File(context.cacheDir, "network_cache")

    private val cacheSize = 5L * 1024 * 1024 // 5 MiB

    val cookieManager = AndroidCookieJar()

    val webView : WebView by injectLazy()

    private val baseClientBuilder: OkHttpClient.Builder
        get() {
            val builder = OkHttpClient.Builder()
                .cookieJar(cookieManager)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(UserAgentInterceptor())

            when (preferencesUseCase.readDohPrefUseCase()) {
                PREF_DOH_CLOUDFLARE -> builder.dohCloudflare()
                PREF_DOH_GOOGLE -> builder.dohGoogle()
                PREF_DOH_ADGUARD -> builder.dohAdGuard()
                PREF_DOH_SHECAN -> builder.dohShecan()
            }

            return builder
        }

    val client by lazy { baseClientBuilder.cache(Cache(cacheDir, cacheSize)).build() }

    val cloudflareClient by lazy {
        client.newBuilder()
            .addInterceptor(CloudflareInterceptor(context))
            .build()
    }

    @SuppressLint("SetJavaScriptEnabled")
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getHtmlFromWebView(url: String, ajaxSelector: String? = null): Document {

        Timber.d("Infinity: GetData Using WebView")

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            setAppCacheEnabled(true)
            useWideViewPort = true
            loadWithOverviewMode = true
            cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
        }
        webView.loadUrl(url)


        var docs: Document = Document("No Data was Found")
        var isLoadUp: Boolean = false
        webView.webViewClient = object : WebViewClientCompat() {
            override fun onPageFinished(view: WebView, url: String) {
                coroutineScope.launch(Dispatchers.Main) {
                    docs = Jsoup.parse(webView.getHtml())
                    if (ajaxSelector != null) {
                        while (docs.select(ajaxSelector).text().isEmpty()) {
                            docs = Jsoup.parse(webView.getHtml())
                        }
                        isLoadUp = true
                    } else {
                        isLoadUp = true
                    }
                }
            }

            override fun onReceivedErrorCompat(
                view: WebView,
                errorCode: Int,
                description: String?,
                failingUrl: String,
                isMainFrame: Boolean,
            ) {
                isLoadUp = true
                Timber.e("WebView: Not shown")
//                throw  Exception("The Page was not Loaded")
            }
        }
        docs = Jsoup.parse(webView.getHtml())
        while (!isLoadUp) {
            delay(200)
        }

        //Timber.e(docs.toString())
        return docs
    }

}

