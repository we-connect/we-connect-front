package berlin.weconnect.weconnect.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.util.Configuration;
import berlin.weconnect.weconnect.model.util.MailUtil;

public class AboutActivity extends BaseActivity {
    // View
    private TextView tvName;
    private TextView tvCompany;
    private TextView tvVersion;
    private TextView tvLicense;
    private TextView tvWebsite;
    private ImageView ivMail;
    private ImageView ivGithub;

    private Activity activity;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Resources res = getResources();

        // Load layout
        tvName = (TextView) findViewById(R.id.tvName);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvLicense = (TextView) findViewById(R.id.tvLicense);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);
        ivMail = (ImageView) findViewById(R.id.ivMail);
        ivGithub = (ImageView) findViewById(R.id.ivGithub);

        String versionMajor = Configuration.getGradleProperty(this, res.getString(R.string.gradle_version_major));
        String versionMinor = Configuration.getGradleProperty(this, res.getString(R.string.gradle_version_minor));
        String versionPatch = Configuration.getGradleProperty(this, res.getString(R.string.gradle_version_patch));

        String version = versionMajor + "." + versionMinor + "." + versionPatch;

        // Set values
        tvName.setText(res.getString(R.string.app_name));
        tvCompany.setText(Configuration.getGradleProperty(activity, res.getString(R.string.gradle_company)));
        tvVersion.setText(version);
        tvLicense.setText(Configuration.getGradleProperty(activity, res.getString(R.string.gradle_license)));
        tvWebsite.setText(Configuration.getGradleProperty(activity, res.getString(R.string.gradle_website)));

        // Add actions
        ivGithub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Configuration.getGradleProperty(activity, res.getString(R.string.gradle_sourcecode))));
                startActivity(i);
            }
        });
        ivMail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MailUtil.sendFeedback(activity);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}