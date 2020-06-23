package com.example.cathdev;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class Screening extends Fragment {

    public Screening() {
        // Required empty public constructor
    }

private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_screening2, container, false);
         webView=(WebView)v.findViewById(R.id.webView);
         webView.loadUrl("https://myhealth.alberta.ca/Journey/COVID-19/Pages/Assessment.aspx");
         //Enable JavaScript
        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //FOrce links to open in WebView
        webView.setWebViewClient(new WebViewClient());
        return v;
    }
}
