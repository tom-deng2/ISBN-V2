package com.example.shangjian.isbn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class OnSearchActivity extends Activity {
    private WebView webBrowser;
    private String isbnCode10;
    private String isbnCode13;
    private TextView valid10Label;
    private TextView valid13Label;
    private TextView isbn10TextView;
    private TextView isbn13TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_search);

        valid10Label = findViewById(R.id.valid10Label);
        valid13Label = findViewById(R.id.valid13Label);
        isbn10TextView = findViewById(R.id.isbn10TextView);
        isbn13TextView = findViewById(R.id.isbn13TextView);
        webBrowser = findViewById(R.id.webBrowser);

        Intent intent = getIntent();
        if (intent.getStringExtra("IsbnCode").length() == 10){
            isbnCode10 = intent.getStringExtra("IsbnCode");
            findBookOnWebsite(isbnCode10);
        }
        else{
            isbnCode13 = intent.getStringExtra("IsbnCode");
            findBookOnWebsite(isbnCode13);
        }
        IsbnHelper.setValid10Label(isbnCode10,valid10Label);
        IsbnHelper.setValid13Label(isbnCode13,valid13Label);
        IsbnHelper.displayISBN10(isbnCode10,isbn10TextView);
        IsbnHelper.displayISBN13(isbnCode13,isbn13TextView);

    }
    private void findBookOnWebsite(String code){
        // Enable JavaScript for our WebView
        //webBrowser.getSettings().setJavaScriptEnabled(true);
        // Load URLs in our WebView instead of a separate browser app
        webBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        // Load a URL
        webBrowser.loadUrl("https://www.abebooks.com/servlet/SearchResults?kn=" + code);
    }

}
