package com.github.codetanzania.feature.logincitizen;

import android.text.TextWatcher;

/**
 * Convenience class to make ui code more compact and clear
 */

public abstract class RegistrationTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }
}
