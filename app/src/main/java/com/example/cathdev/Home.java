package com.example.cathdev;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;





/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }
Toolbar toolbar;
private WebView webView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Latest News on Covid-19");

        webView=(WebView)v.findViewById(R.id.webView2);
        webView.loadUrl("https://businesstech.co.za/news/?s=Corona");
        //Enable JavaScript
        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //FOrce links to open in WebView
        webView.setWebViewClient(new WebViewClient(){

        });
        return v;
    }
}
