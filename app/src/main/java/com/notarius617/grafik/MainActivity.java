package com.notarius617.grafik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Обвивка за вътрешния график на notarius617.com.
 * Всичко се зарежда от сайта, така че промените по графика стигат до приложението без нова версия.
 */
public class MainActivity extends Activity {

    private static final String HOME = "https://notarius617.com/grafik/?source=app";

    private static final String OFFLINE =
            "<!doctype html><html><head><meta charset='utf-8'>"
            + "<meta name='viewport' content='width=device-width,initial-scale=1'></head>"
            + "<body style='font-family:sans-serif;text-align:center;padding:48px 20px;color:#102c4f'>"
            + "<h2>Няма връзка</h2>"
            + "<p>Графикът се зарежда от сайта на кантората. Проверете интернета и опитайте отново.</p>"
            + "<p><a href='" + HOME + "' style='display:inline-block;margin-top:16px;padding:12px 28px;"
            + "background:#102c4f;color:#fff;border-radius:8px;text-decoration:none'>Опитай отново</a></p>"
            + "</body></html>";

    private WebView web;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.parseColor("#102c4f"));

        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setUserAgentString(s.getUserAgentString() + " Grafik617App/1.0");

        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setAcceptThirdPartyCookies(web, true);

        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return openOutside(request.getUrl());
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    view.loadDataWithBaseURL(HOME, OFFLINE, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager.getInstance().flush();
            }
        });

        if (state != null) {
            web.restoreState(state);
        } else {
            web.loadUrl(HOME);
        }
    }

    /** Страниците на сайта остават в приложението; телефон, имейл и външни линкове се отварят навън. */
    private boolean openOutside(Uri uri) {
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (("https".equals(scheme) || "http".equals(scheme)) && host != null
                && (host.equals("notarius617.com") || host.endsWith(".notarius617.com"))) {
            return false;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            Toast.makeText(this, "Не може да се отвори линкът", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        if (web != null) {
            web.saveState(out);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieManager.getInstance().flush();
    }
}
