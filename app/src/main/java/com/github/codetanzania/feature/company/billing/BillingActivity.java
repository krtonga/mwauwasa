package com.github.codetanzania.feature.company.billing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.codetanzania.feature.company.logincustomer.CompanyRegisterFragment;
import com.github.codetanzania.feature.list.ProgressBarFragment;
import com.github.codetanzania.feature.settings.UserProfileChangeEvent;
import com.github.codetanzania.open311.android.library.api.MajiFixAPI;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;
import com.github.codetanzania.util.LookAndFeelUtils;
import com.github.codetanzania.util.Util;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;
import tz.co.codetanzania.R;

/**
 * This is used to view my current and previous bill.
 */

public class BillingActivity extends AppCompatActivity
        implements MiniStatementFragment.TitleChangeListener,
        CompanyRegisterFragment.RegistrationCallbacks {

    private static final String LOADING_FRAG_TAG = "loading";
    private static final String REGISTER_FRAG_TAG = "register";
    private static final String STATEMENTS_FRAG_TAG = "statements";

    private Disposable mDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        // If account number is already set, attempt to get account object
        Reporter currentReporter = Util.getCurrentReporter(getApplicationContext());
        if (currentReporter.getAccount() != null) {
            sendRegistration(currentReporter);
            showLoadingFragment();
        } else {
            showCustomerLoginFragment();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLoadingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frl_FragmentOutlet,
                new ProgressBarFragment(), LOADING_FRAG_TAG);
        fragmentTransaction.commit();
    }

    private void showCustomerLoginFragment() {
        // Hide keyboard if it is open
        Util.hideSoftInputMethod(this);

        // Show login fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frl_FragmentOutlet,
                new CompanyRegisterFragment(), REGISTER_FRAG_TAG);
        fragmentTransaction.commit();

        // Update action bar title
        LookAndFeelUtils.setupActionBar(this, R.string.login_title, true);
    }

    private void showAccount(CustomerAccount account) {
        // Hide keyboard if it is open
        Util.hideSoftInputMethod(this);

        // Quick safety check
        if (account == null) {
            System.out.println("Account object was null");
            return;
        }

        // Show billing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frl_FragmentOutlet,
                MiniStatementFragment.getInstance(account), STATEMENTS_FRAG_TAG);
        fragmentTransaction.commit();

        // Update action bar title
        LookAndFeelUtils.setupActionBar(this, R.string.bill_current_title, true);
    }

    public void sendRegistration(Reporter reporter) {
        if (reporter == null ||
                reporter.getAccount() == null ||
                reporter.getPhone() == null) {
            return;
        }
        mDisposable = MajiFixAPI.getInstance().getCustomerAccount(reporter).subscribe(new Consumer<CustomerAccount>() {
            @Override
            public void accept(CustomerAccount customerAccount) throws Exception {
                onRegistrationPosted(customerAccount);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable error) throws Exception {
                    onRegistrationFailed(error);
            }
        });
    }

    /**
     * Used by fragments to update activity title.
     * @param newTitle
     */
    @Override
    public void changeToolbarTitle(int newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

    /**
     * Triggered on registration submit. Since registration fragment requires user to submit all
     * details, account number should now be saved in reporter.
     * @param event
     */
    @Override
    public void onProfileChanged(UserProfileChangeEvent event) {
        if (event.getUser().getAccount() != null) {
            sendRegistration(event.getUser());
        } else {
            Toast.makeText(
                    this, R.string.msg_customer_registration_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Triggered on successful registration
     */
    @Override
    public void onRegistrationPosted(CustomerAccount account) {
        showAccount(account);
    }

    @Override
    public void onRegistrationFailed(Throwable error) {
        // Clear login
        Util.requireCompanyLogin(getApplicationContext());

        // Display proper error message
        if (error instanceof HttpException) {
            HttpException httpException = (HttpException) error;
            switch (httpException.code()) {
                case 400:
                    Toast.makeText(getApplicationContext(), R.string.msg_billing_error_invalid_account, Toast.LENGTH_LONG).show();
                    break;
                case 202:
                    Toast.makeText(getApplicationContext(), R.string.msg_billing_error_invalid_account, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, R.string.msg_application_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.msg_application_error, Toast.LENGTH_SHORT).show();
        }

        // If error occurred on initial load, show login
        if (getSupportFragmentManager().findFragmentByTag(LOADING_FRAG_TAG) != null) {
            showCustomerLoginFragment();
        }

        System.out.println(error);
    }
}
