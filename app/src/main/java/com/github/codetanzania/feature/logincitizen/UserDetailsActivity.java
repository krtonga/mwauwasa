package com.github.codetanzania.feature.logincitizen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.github.codetanzania.feature.settings.UserProfileChangeEvent;
import com.github.codetanzania.feature.initialization.SplashScreenActivity;
import com.github.codetanzania.feature.settings.EditProfileFragment;

import tz.co.codetanzania.R;

public class UserDetailsActivity extends AppCompatActivity implements EditProfileFragment.OnUserProfileChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        findViewById(R.id.btn_Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerDone();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAppBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_done:
                return triggerDone();
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onProfileChanged(UserProfileChangeEvent event) {
        startActivity(new Intent(this, SplashScreenActivity.class));
        finish();
    }

    private boolean triggerDone() {
        return ((EditProfileFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_frag)).verifyAndComplete();
    }

    private void setupAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.registration_toolbar_layout);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
}
