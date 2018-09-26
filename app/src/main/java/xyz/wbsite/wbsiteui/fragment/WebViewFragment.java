package xyz.wbsite.wbsiteui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class WebViewFragment extends BaseSPAFragment {

    @BindView(R.id.webView)
    WebView webView;

    private String cookies = null;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void initView() {
        initWebView("http://192.168.1.103:8080/wap/index#/");
    }

    private void initWebView(final String url) {
        QMUIStatusBarHelper.FlymeSetStatusBarLightMode(getActivity().getWindow(), true);
        //android 5.0 之后需要开启浏览器的整体缓存才能截取整个Web
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        WebSettings webSettings = webView.getSettings();
        //支持获取手势焦点，输入用户名、密码或其他
        webView.requestFocusFromTouch();

        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置可以缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        // 若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setTextZoom(100);//设置文本的缩放倍数，默认为 100
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows();  //多窗口
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        webSettings.setAppCacheEnabled(true);

        if (cookies != null) {
            syncCookie(url, cookies.substring(0, cookies.indexOf(";")));
        }

        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.i("----------", url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
//        webView.addJavascriptInterface(new WebAppInterface(this), "app");
    }

    public void syncCookie(String url, String cookie) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
            cookieManager.setCookie(url, cookie);
            cookieManager.flush();  //强制立即同步cookie

        } else {
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);
            cookieSyncManager.sync();
        }
    }

    /**
     * 对webview截图
     *
     * @param webView
     * @param savePath
     * @return
     */
    public static boolean getFullWebViewSnapshot(WebView webView, String savePath) {
        //重新调用WebView的measure方法测量实际View的大小（将测量模式设置为UNSPECIFIED模式也就是需要多大就可以获得多大的空间）
        webView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //调用layout方法设置布局（使用新测量的大小）
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        //开启WebView的缓存(当开启这个开关后下次调用getDrawingCache()方法的时候会把view绘制到一个bitmap上)
        webView.setDrawingCacheEnabled(true);
        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）
        webView.buildDrawingCache();
        //根据测量结果创建一个大小一样的bitmap
        Bitmap picture = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //已picture为背景创建一个画布
        Canvas canvas = new Canvas(picture);  // 画布的宽高和 WebView 的网页保持一致
        Paint paint = new Paint();
        //设置画笔的定点位置，也就是左上角
        canvas.drawBitmap(picture, 0, webView.getMeasuredHeight(), paint);
        //将webview绘制在刚才创建的画板上
        webView.draw(canvas);
//        try {
//            //将bitmap保存到SD卡
//            FileTools.saveBitmap(picture, savePath);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }
}
