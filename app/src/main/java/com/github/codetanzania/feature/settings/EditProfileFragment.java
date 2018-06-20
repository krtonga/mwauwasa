package com.github.codetanzania.feature.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.github.codetanzania.feature.logincitizen.RegistrationTextWatcher;
import com.github.codetanzania.feature.views.SingleItemSelectionDialog;
import com.github.codetanzania.feature.logincitizen.OTPVerificationActivity;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.util.LanguageUtils;
import com.github.codetanzania.feature.logincitizen.PhoneVerificationUtils;
import com.github.codetanzania.util.Util;

import tz.co.codetanzania.R;

public class EditProfileFragment extends Fragment implements
        DialogInterface.OnClickListener,
        SingleItemSelectionDialog.OnAcceptSelection {

    /* Used by The Logcat */
    private static final String TAG = "EditProfileFragment";

    /* If true, will show account number and email fields */
    private boolean mShowFullInfo = false;
    /* If true, verify account number */
    private boolean mVerifyAccount;
    /* If true, verify email */
    private boolean mVerifyEmail;

    /* the flag to track if user has changed account number */
    private boolean accountChanged;

    /* the flag to track if user has changed language or not */
    private boolean languageChanged;
    private String mSelectedLanguage;
    private String mDefaultLanguage;

    private TextInputLayout tilUserName;
    private TextInputEditText etUserName;
    private TextInputEditText etAreaCode;
    private TextInputLayout tilPhone;
    private TextInputEditText etPhone;
    private TextInputLayout tilAccountNo;
    private TextInputEditText etAccountNo;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputEditText etUserDefaultLanguage;

    /*
     * Bridges communication between fragment and activity
     */
    private OnUserProfileChangeListener mListener;

    public void setOptionFlags(boolean showAll, boolean verifyAccount, boolean verifyEmail) {
        mShowFullInfo = showAll;
        mVerifyAccount = verifyAccount;
        mVerifyEmail = verifyEmail;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        /* only update the UI if language was changed */
        if (languageChanged) {
            etUserDefaultLanguage.setText(mSelectedLanguage);
            LanguageUtils languageUtils = LanguageUtils
                    .withBaseContext(getActivity().getBaseContext());

            if (LanguageUtils.SWAHILI_LANG.equals(mSelectedLanguage)) {
                languageUtils.setSwahiliAsDefaultLanguage();
            } else {
                languageUtils.setEnglishAsDefaultLanguage();
            }

            // update the default language
            mDefaultLanguage = mSelectedLanguage;
        }
    }

    @Override
    public void onItemSelected(String item, int position) {
        mSelectedLanguage = item;
        // assign the flag depending on weather the language was changed or not
        languageChanged = !mSelectedLanguage.equals(mDefaultLanguage);
    }

    public interface OnUserProfileChangeListener {
        void onProfileChanged(UserProfileChangeEvent event);
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        // cast context
        try {
            mListener = (OnUserProfileChangeListener) ctx;
        } catch (ClassCastException cce) {
            throw new IllegalStateException(String.format("%s must implement %s",
                    getActivity().getClass().getName(),
                    ctx.getClass().getName()));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDefaultLanguage = LanguageUtils.withBaseContext(getActivity().getBaseContext())
                .getDefaultLanguageName();
    }

    /* fragment lifecycle callback. create fragment's view */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.frag_edit_profile, group, false);
    }

    /* fragment lifecycle callback. attach events */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // view.findViewById(R.id.til_Email).setVisibility(View.GONE);
        tilUserName = (TextInputLayout) view.findViewById(R.id.til_UserName);
        etUserName = (TextInputEditText) view.findViewById(R.id.et_userName);
        etAreaCode = (TextInputEditText) view.findViewById(R.id.et_AreaCode);
        tilPhone = (TextInputLayout) view.findViewById(R.id.til_PhoneNumber);
        etPhone = (TextInputEditText) view.findViewById(R.id.et_phoneNumber);

        // update error messages after each user input
        etUserName.addTextChangedListener(new RegistrationTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isValidUsername(s);
            }
        });

        etPhone.addTextChangedListener(new RegistrationTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isValidPhoneNumber(s);
            }
        });

        if (mShowFullInfo) {
            setupExtraFields(view);
        } else {
            // save user info after phone input
            etPhone.setImeOptions(EditorInfo.IME_ACTION_DONE);
            etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return actionId == EditorInfo.IME_ACTION_DONE && verifyAndComplete();
                }
            });
        }

        // this fragment is used in multiple activities.
        // only show etUserDefaultLanguage if current activity is edit profile
        if (getActivity() instanceof EditSettingsActivity) {
            etUserDefaultLanguage = (TextInputEditText) view.findViewById(R.id.et_DefaultUserLanguage);
            etUserDefaultLanguage.setVisibility(View.VISIBLE);

            // Display a dialog to allow user to select default language
            etUserDefaultLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLanguageChooserDialog();
                }
            });
        }

        showCurrentReporter();
    }

    private void setupExtraFields(View view) {
        // find views
        tilAccountNo = (TextInputLayout) view.findViewById(R.id.til_accountNo);
        etAccountNo = (TextInputEditText) view.findViewById(R.id.et_accountNo);
        tilEmail = (TextInputLayout) view.findViewById(R.id.til_email);
        etEmail = (TextInputEditText) view.findViewById(R.id.et_email);

        // update error messages after each user input
        etAccountNo.addTextChangedListener(new RegistrationTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isValidAccountNo(s);
            }
        });
        etEmail.addTextChangedListener(new RegistrationTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isValidEmail(s);
            }
        });

        // email should trigger done action
        etEmail.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return actionId == EditorInfo.IME_ACTION_DONE && verifyAndComplete();
            }
        });

        // setup done, so show extra fields
        tilAccountNo.setVisibility(View.VISIBLE);
        tilEmail.setVisibility(View.VISIBLE);
    }

    private void showLanguageChooserDialog() {
        SingleItemSelectionDialog.Builder dialogBuilder =
            SingleItemSelectionDialog.Builder.withContext(getActivity());
        dialogBuilder.addItems(R.array.languages)
                .setSelectedItem(mDefaultLanguage)
                .setTitle(R.string.title_select_default_language)
                .setActionSelectText(R.string.action_select)
                .setOnAcceptSelection(this)
                .setOnActionListener(this);
        dialogBuilder.build().open();
    }

    public void showCurrentReporter() {
        Reporter currentReporter = Util.getCurrentReporter(getContext());
        if (currentReporter != null) {
            etUserName.setText(currentReporter.getName());
            String zip = currentReporter.getPhone().substring(0, 3);
            String phone = currentReporter.getPhone().substring(3);
            etAreaCode.setText(zip);
            etPhone.setText(phone);

            if (mShowFullInfo) {
                etAccountNo.setText(currentReporter.getAccount());
                etEmail.setText(currentReporter.getEmail());
            }
        }
        if (etUserDefaultLanguage != null) {
            String defaultLanguage = LanguageUtils.withBaseContext(getActivity().getBaseContext())
                    .getDefaultLanguageName();
            etUserDefaultLanguage.setText(defaultLanguage);
        }
    }

    public boolean verifyAndComplete() {
        Editable userName = etUserName.getText();
        Editable phoneNumber = etPhone.getText();
        Editable accountNo = null;
        Editable email = null;
        boolean extrasValid = true;

        // if form includes accountNo and email check them
        if (mShowFullInfo) {
            accountNo = etAccountNo.getText();
            email = etEmail.getText();
            extrasValid = extraUserInputsAreValid(accountNo, email);
        }

        // if all inputs are valid, submit
        if (userInputsAreValid(userName, phoneNumber) && extrasValid) {
            saveReporter(userName, phoneNumber, accountNo, email);

//            TODO: This must be uncommented as soon as server is ready
//            if (!PhoneVerificationUtils.isVerified(getActivity())) {
//                // start verification code
//                startOTPVerification();
//                return false;
//            }

            return true;
        }
        return false;
    }

    private void startOTPVerification() {
        startActivity(new Intent(
                getActivity(), OTPVerificationActivity.class));
    }

    private void saveReporter(Editable username, Editable phone, Editable account, Editable email) {
        Reporter oldReporter = Util.getCurrentReporter(getContext());

        Reporter reporter = new Reporter();
        reporter.setName(username.toString());
        reporter.setPhone(formatPhoneNumber(phone.toString()));
        if (mShowFullInfo) {
            reporter.setAccount(account.toString());
            reporter.setEmail(email.toString());

            // check whether account number has changed
            if (oldReporter != null) {
                accountChanged = !reporter.getAccount().equals(oldReporter.getAccount());
            }
        }

        Util.storeCurrentReporter(getContext(), reporter);
        Util.hideSoftInputMethod(getActivity());

        UserProfileChangeEvent event = new UserProfileChangeEvent(reporter, accountChanged, languageChanged);
        mListener.onProfileChanged(event);
    }

    private boolean userInputsAreValid(Editable userName, Editable phone) {
        // update both error messages
        boolean isValidUsername = isValidUsername(userName);
        boolean isValidPhone = isValidPhoneNumber(phone);

        // only return true if both are valid
        return isValidUsername && isValidPhone;
    }

    private boolean extraUserInputsAreValid(Editable accountNo, Editable email) {
        // update both error messages
        boolean isValidAccount = isValidAccountNo(accountNo);
        boolean isValidPhone = isValidEmail(email);

        // only return true if both are valid
        return isValidAccount && isValidPhone;
    }

    private boolean isValidUsername(Editable userName) {
        // username is required
        boolean usernameValid = !TextUtils.isEmpty(userName);
        if (usernameValid) {
            if (!userName.toString().trim().contains(" ")) {
                tilUserName.setError(getString(R.string.error_first_and_last_required));
                usernameValid = false;
            } else {
                tilUserName.setErrorEnabled(false);
            }
        } else {
            tilUserName.setError(getString(R.string.error_username_required));
        }
        return usernameValid;
    }

    private boolean isValidPhoneNumber(Editable phoneNumber) {
        // phone number is required TODO: Improve verification, potentially with OTP
        boolean phoneValid = !TextUtils.isEmpty(phoneNumber);
        if (phoneValid) {
            if (phoneNumber.charAt(0) == 0) {
                tilPhone.setError(getString(R.string.error_phone_number_starts_with_0));
                phoneValid = false;
            }
            if (phoneNumber.toString().trim().length() != 9) {
                tilPhone.setError(getString(R.string.error_phone_invalid));
                phoneValid = false;
            } else {
                tilPhone.setErrorEnabled(false);
            }
        } else {
            tilPhone.setError(getString(R.string.error_phone_required));
        }
        return phoneValid;
    }

    private boolean isValidAccountNo(Editable accountNo) {

        if (!mVerifyAccount) {
            return true;
        }

        // username is required
        boolean accountNoValid = !TextUtils.isEmpty(accountNo);
        if (accountNoValid) {
            tilAccountNo.setErrorEnabled(false);
        } else {
            tilAccountNo.setError(getString(R.string.error_account_no_required));
        }
        return accountNoValid;
    }

    private boolean isValidEmail(Editable email) {

        if (!mVerifyEmail) {
            return true;
        }

        // username is required
        boolean usernameValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (usernameValid) {
            tilEmail.setErrorEnabled(false);
        } else {
            tilEmail.setError(getString(R.string.error_email_required));
        }
        return usernameValid;
    }

    private String formatPhoneNumber(String phoneNumber) {
        String areaCode = etAreaCode.getText().toString().trim();
        if (TextUtils.isEmpty(areaCode)) {
            areaCode = getResources().getString(R.string.default_area_code);
        }
        return String.format("%s%s", areaCode, phoneNumber);
    }
}
