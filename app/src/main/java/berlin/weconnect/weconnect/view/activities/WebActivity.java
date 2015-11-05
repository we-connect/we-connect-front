package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.WebController;

public class WebActivity extends AppCompatActivity {
    WebController webController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webController = WebController.getInstance();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        WebView wbFacebook = (WebView) findViewById(R.id.wbFacebook);

        wbFacebook.loadUrl(webController.getUrl());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
