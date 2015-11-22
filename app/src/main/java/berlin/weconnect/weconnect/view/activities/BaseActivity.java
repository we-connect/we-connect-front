/*
 * Copyright (C) 2014 Antonio Leiva Gordillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package berlin.weconnect.weconnect.view.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.util.ConnectionUtil;
import berlin.weconnect.weconnect.view.dialogs.NoInternetDialog;

public abstract class BaseActivity extends AppCompatActivity implements NoInternetDialog.OnCompleteListener {
    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Load views
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void onResume() {
        super.onResume();

        testInternetConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void testInternetConnection() {
        if (!ConnectionUtil.isNetworkAvailable()) {
            new NoInternetDialog().show(getFragmentManager(), NoInternetDialog.TAG);
        }
    }

    // --------------------
    // Getters / Setters
    // --------------------

    protected abstract int getLayoutResource();

    @NonNull
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    protected void setActionBarIcon(int iconRes) {
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(iconRes);
    }

    protected void setDisplayHomeAsUpEnabled(boolean enabled) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public void onRetry() {
        testInternetConnection();
    }

    @Override
    public void onCloseApp() {
        finish();
        System.exit(0);
    }
}
