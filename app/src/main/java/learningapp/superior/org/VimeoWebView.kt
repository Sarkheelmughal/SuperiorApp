package learningapp.superior.org

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.vimeo.networking.Configuration
import com.vimeo.networking.Configuration.Builder
import com.vimeo.networking.VimeoClient
import kotlinx.android.synthetic.main.activity_vimeo_web_view.*


class VimeoWebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vimeo_web_view)

        mContentWebView.setWebChromeClient(WebChromeClient())
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON)
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND)
        mContentWebView.setWebViewClient(WebViewClient())
        mContentWebView.getSettings().setJavaScriptEnabled(true)

//        ---------------------done start
//        val htmlWithVideosString="<html><body> <iframe src=\"https://player.vimeo.com/video/228773975\" width=\"340\" height=\"260\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen></iframe></body></html>"
//
//
//        mContentWebView.loadDataWithBaseURL("https://vimeo.com/228773975", htmlWithVideosString,
//            "text/html; charset=utf-8", "UTF-8", null);
//    -----------------------------done webview end


        /**
         * @param clientId      The client id provided to you from [the developer console](https://developer.vimeo.com/apps/)
         * @param clientSecret  The client secret provided to you from [the developer console](https://developer.vimeo.com/apps/)
         * @param scope         Space separated list of [scopes](https://developer.vimeo.com/api/authentication#scopes)
         *
         *
         * Example: "private public create"
         * @param accountStore  (Optional, Recommended) An implementation that can be used to interface with Androids [Account Manager](http://developer.android.com/reference/android/accounts/AccountManager.html)
         */
        val configBuilder: Configuration.Builder = Builder(
            "e2824e84f266ad2ab5884aa459aaa951b6942971",
          "YARXRKAVtyFal9o3P9QPyeW3qR9vxGbTbQQJb9M2oElzg4xYjDRvTseSrdMHmvRfifYrZX7Wm7PhfzLCehu8L5GLO8GwsMMNBDBWyBs6NMV29jwvm+0PcqcqFFfMc71A",
            "private"
        )
            .setCacheDirectory(this.cacheDir)
        VimeoClient.initialize(configBuilder.build())
// VimeoClient is now ready for use
    }
}