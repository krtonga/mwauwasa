package com.github.codetanzania.feature.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.codetanzania.util.LookAndFeelUtils;

import tz.co.codetanzania.R;

public class EditSettingsActivity extends AppCompatActivity implements
        EditProfileFragment.OnUserProfileChangeListener {

    private static final String USER_INPUT_FRAGMENT_TAG = "userInputFragment";
    public static final String FLAG_LANGUAGE_CHANGED = "FLAG_LANG_CHANGED";
    public static final String FLAG_ACCOUNT_CHANGED = "FLAG_ACNT_CHANGED";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view
        setContentView(R.layout.activity_edit_profile);

        EditProfileFragment fragment = new EditProfileFragment();
        // if (Util.isUserLoggedIn(this)) {
            fragment.setOptionFlags(true, false, false);
        // }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.id_frag_container, fragment, USER_INPUT_FRAGMENT_TAG).commit();

        View fab = findViewById(R.id.fab_EditProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                save();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_done:
                // save and exit
                save();
                return true;
            case android.R.id.home:
                // exit without saving
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.basic_toolbar_layout);
        LookAndFeelUtils.setupActionBar(this, toolbar,
                R.string.title_edit_profile, true, R.drawable.ic_close_black_24dp);
    }

    private void save() {
        ((EditProfileFragment) getSupportFragmentManager()
                .findFragmentByTag(USER_INPUT_FRAGMENT_TAG)).verifyAndComplete();
    }

    @Override
    public void onProfileChanged(UserProfileChangeEvent event) {
        Toast.makeText(EditSettingsActivity.this,
                getString(R.string.text_item_saved), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(FLAG_LANGUAGE_CHANGED, event.languageChanged());
        intent.putExtra(FLAG_ACCOUNT_CHANGED, event.accountChanged());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
