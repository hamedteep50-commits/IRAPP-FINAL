package com.irapp.store;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.net.Uri;
import android.database.Cursor;
import android.provider.OpenableColumns;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
    private WebView web;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setSupportMultipleWindows(false);
        s.setJavaScriptCanOpenWindowsAutomatically(false);
        s.setLoadWithOverviewMode(false);
        web.setFocusable(true);
        web.setClickable(true);
        web.setLongClickable(false);
        web.setFocusableInTouchMode(true);
        web.requestFocusFromTouch();
        web.addJavascriptInterface(new ShareBridge(), "AndroidShare");
        web.addJavascriptInterface(new FileBridge(), "AndroidFile");
        web.addJavascriptInterface(new OpenBridge(), "AndroidOpen");
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);
        web.loadUrl("file:///android_asset/index.html");
    }
    public class FileBridge {
        @JavascriptInterface public void chooseFile() {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(i, 701);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 701 || resultCode != RESULT_OK || data == null || data.getData() == null) return;
        Uri uri = data.getData();
        try {
            String name = "selected_file";
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            if (c != null) { if (c.moveToFirst()) { int ix=c.getColumnIndex(OpenableColumns.DISPLAY_NAME); if(ix>=0) name=c.getString(ix); } c.close(); }
            File dir = new File(getFilesDir(), "irapp_manager"); if(!dir.exists()) dir.mkdirs();
            String safe = name.replaceAll("[^A-Za-z0-9._-]", "_");
            File out = new File(dir, System.currentTimeMillis()+"_"+safe);
            try(InputStream in=getContentResolver().openInputStream(uri); FileOutputStream fos=new FileOutputStream(out)){ byte[] buf=new byte[8192]; int n; while((n=in.read(buf))>0) fos.write(buf,0,n); }
            String js = "javascript:onLocalFileSelected(" + org.json.JSONObject.quote(name) + "," + org.json.JSONObject.quote(out.getAbsolutePath()) + ")";
            web.post(() -> web.evaluateJavascript(js, null));
        } catch(Exception e) { web.post(() -> web.evaluateJavascript("javascript:toast("+org.json.JSONObject.quote("خطا در ذخیره فایل")+")", null)); }
    }

    public class ShareBridge {
        @JavascriptInterface
        public void shareText(String text) {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(send, "ارسال با"));
        }
    }


    public class OpenBridge {
        @JavascriptInterface public void openUrl(String url) {
            try { Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); startActivity(i); }
            catch(Exception e) { web.post(() -> web.evaluateJavascript("javascript:toast(" + org.json.JSONObject.quote("امکان باز کردن این سرویس وجود ندارد") + ")", null)); }
        }
    }

    @Override public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
}
