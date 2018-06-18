package com.github.codetanzania.feature.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.codetanzania.core.model.FAQEntry;
import com.github.codetanzania.core.model.Jurisdiction;
import com.github.codetanzania.core.model.PaymentMethod;
import com.github.codetanzania.core.model.Tariff;
import com.github.codetanzania.feature.company.contact.ContactUsFragment;
import com.github.codetanzania.feature.company.faq.FAQsFragment;
import com.github.codetanzania.feature.company.logincustomer.CompanyRegisterFragment;
import com.github.codetanzania.feature.company.payment.PaymentInstructionsFragment;
import com.github.codetanzania.feature.company.newconnection.RequestNewConnectionFragment;
import com.github.codetanzania.feature.company.tariffs.TariffsFragment;
import com.github.codetanzania.util.LanguageUtils;
import com.github.codetanzania.util.LookAndFeelUtils;
import com.github.codetanzania.util.StaticAssetsUtils;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * This is used as a container for Navigation Drawer pages.
 */

public class NavItemActivity extends AppCompatActivity {
    private static final String FRAG_EXTRA = "nav_item_id";

    public static final int REGISTER = R.string.registration_title;
    public static final int PAY = R.string.payment_instructions_title;
    public static final int TARIFFS = R.string.title_tariffs;
    public static final int CONTACT = R.string.text_contact_us;
    public static final int FAQ = R.string.title_faqs;
    public static final int REQUEST_NEW_CONNECTION = R.string.title_request_new_connection;
    public static final int SETTINGS = 7;

    @Retention(SOURCE)
    @IntDef({REGISTER, PAY, CONTACT, TARIFFS, FAQ, SETTINGS, REQUEST_NEW_CONNECTION})
    @interface NavItem {}

    private  @NavItem int mNavItem;

    public static void startNavActivity(Activity currentActivity, int navItem) {
        Intent intent = new Intent(currentActivity, NavItemActivity.class);
        intent.putExtra(FRAG_EXTRA, navItem);
        currentActivity.startActivity(intent);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view
        setContentView(R.layout.activity_basic);
        @NavItem int navItem = getIntent().getIntExtra(FRAG_EXTRA, FAQ);
        mNavItem = navItem;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // get localized asset resource, if it is necessary
        String languageCode = LanguageUtils
                .withBaseContext(getBaseContext())
                .getDefaultLanguageCode();

        Fragment fragment = new Fragment();
        switch (mNavItem) {
            case REGISTER:
                fragment = new CompanyRegisterFragment();
                break;
            case CONTACT:
                String filename = "contact_us_" + languageCode + ".json";
                ArrayList<Jurisdiction> contactsEntries = (ArrayList<Jurisdiction>) StaticAssetsUtils
                        .loadJsonAsItems(this, filename, Jurisdiction.class);
                fragment = ContactUsFragment.newInstance(contactsEntries);
                break;
            case FAQ:
                filename = "FAQs_" + languageCode + ".json";
                List<FAQEntry> faqEntries = StaticAssetsUtils
                        .loadJsonAsItems(this, filename, FAQEntry.class);
                fragment = FAQsFragment.newInstance(faqEntries);
                break;
            case PAY:
                filename = "payment_instructions_" + languageCode + ".json";
                List<PaymentMethod> paymentMethods = StaticAssetsUtils
                        .loadJsonAsItems(this, filename, PaymentMethod.class);
                fragment = PaymentInstructionsFragment.newInstance(
                        (ArrayList<PaymentMethod>) paymentMethods);
                break;
            case SETTINGS:
                break;
            case REQUEST_NEW_CONNECTION:
                fragment = RequestNewConnectionFragment.newInstance();
                break;
            case TARIFFS:
                filename = "tariffs_" + languageCode + ".json";
                List<Tariff> tariffs = StaticAssetsUtils
                        .loadJsonAsItems(this, filename, Tariff.class);
                fragment = TariffsFragment.newInstance(
                        (ArrayList<Tariff>)tariffs);
                break;
            default:
                finish();
        }

        fragmentTransaction.add(R.id.frl_FragmentOutlet, fragment);
        fragmentTransaction.commit();

        LookAndFeelUtils.setupActionBar(this, navItem, true);
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
}
