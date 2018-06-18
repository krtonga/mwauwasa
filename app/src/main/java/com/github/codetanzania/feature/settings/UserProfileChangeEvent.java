package com.github.codetanzania.feature.settings;

import com.github.codetanzania.open311.android.library.models.Reporter;

public class UserProfileChangeEvent {

    private Reporter mUser;
    private boolean mAccountChanged;
    private boolean mLanguageChanged;

    public UserProfileChangeEvent(Reporter user, boolean accountChanged, boolean languageChanged ) {
        mUser = user;
        mAccountChanged = accountChanged;
        mLanguageChanged = languageChanged;
    }

    public Reporter getUser() /* const */ {
        return mUser;
    }

    public boolean accountChanged() {
        return mAccountChanged;
    }

    public boolean languageChanged() /* const */ {
        return mLanguageChanged;
    }
}
