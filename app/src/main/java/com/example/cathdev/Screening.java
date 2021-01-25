package com.example.cathdev;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Screening extends Fragment {

    public Screening() {
        // Required empty public constructor
    }

private WebView webView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_screening2, container, false);
        progressBar=(ProgressBar)v.findViewById(R.id.ProgBar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Self-Screening");
         webView=(WebView)v.findViewById(R.id.webView);
         webView.loadUrl("https://myhealth.alberta.ca/Journey/COVID-19/Pages/Assessment.aspx");
         //Enable JavaScript
        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //FOrce links to open in WebView
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        return v;
    }
}
