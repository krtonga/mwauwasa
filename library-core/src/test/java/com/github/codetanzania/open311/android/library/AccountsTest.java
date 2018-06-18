package com.github.codetanzania.open311.android.library;

import android.accounts.Account;
import android.os.Parcel;

import com.github.codetanzania.open311.android.library.api.ApiModelConverter;
import com.github.codetanzania.open311.android.library.api.MajiFixAPI;
import com.github.codetanzania.open311.android.library.api.models.ApiCustomerAccount;
import com.github.codetanzania.open311.android.library.auth.Auth;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;
import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccountsTest {

    @Test
    public void canParseFromJson() {
        Gson gson = new Gson();
        ApiCustomerAccount fromJson = gson.fromJson(MocksAcnt.sampleJsonResponse, ApiCustomerAccount.class);
        MocksAcnt.assertAccountMatchesMock(fromJson);
    }

    @Test
    public void canConvertFromApiObject() {
        Gson gson = new Gson();
        ApiCustomerAccount fromJson = gson.fromJson(MocksAcnt.sampleJsonResponse, ApiCustomerAccount.class);
        CustomerAccount fromApiObject = ApiModelConverter.convert(fromJson);
        MocksAcnt.assertAccountMatchesMock(fromApiObject);
    }

    @Test
    public void canParseAccount() {
        Gson gson = new Gson();
        ApiCustomerAccount fromJson = gson.fromJson(MocksAcnt.sampleJsonResponse, ApiCustomerAccount.class);
        CustomerAccount fromApiObject = ApiModelConverter.convert(fromJson);

        Parcel parcel = Parcel.obtain();
        fromApiObject.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        CustomerAccount fromParcel = CustomerAccount.CREATOR.createFromParcel(parcel);
        MocksAcnt.assertAccountMatchesMock(fromParcel);
    }

//    @Test
//    public void integrationTest() throws InterruptedException {
//        MajiFix.setup(RuntimeEnvironment.application,
//                "https://majifix-account.herokuapp.com/v1/", "");
//        final CountDownLatch lock = new CountDownLatch(1);
//
//        final String accountNumb = "92389";
//        String accountPhone = "255719818179";
//
//        Reporter sampleReporter = new Reporter();
//        sampleReporter.setAccount(accountNumb);
//        sampleReporter.setPhone(accountPhone);
//
//        final CustomerAccount[] result = new CustomerAccount[1];
//
//        MajiFixAPI.getInstance()
//                .getCustomerAccount(sampleReporter)
//                .subscribe(new Consumer<CustomerAccount>() {
//                    @Override
//                    public void accept(CustomerAccount customerAccount) throws Exception {
//                        System.out.println("CustomerAccount returned: "+customerAccount);
//                        result[0] = customerAccount;
//                        lock.countDown();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable error) throws Exception {
//                        System.out.println(error);
//                        Assert.fail();
//                        lock.countDown();
//                    }
//                });
//        lock.await(10, TimeUnit.SECONDS);
//
//        assertNotNull(result);
//        assertEquals(accountNumb, result[0].getNumber());
//    }
}
