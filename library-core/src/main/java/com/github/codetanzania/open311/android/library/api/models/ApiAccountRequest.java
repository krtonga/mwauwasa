package com.github.codetanzania.open311.android.library.api.models;

import android.text.TextUtils;

import com.github.codetanzania.open311.android.library.models.Reporter;

public class ApiAccountRequest {
    public String account;
    public String phone;
    public String name;
    public String email;
    public String locale;

    public ApiAccountRequest(Reporter reporter) {
        if (reporter == null) {
            return;
        }
        this.account = reporter.getAccount();
        this.phone = reporter.getPhone();
        this.name = reporter.getName();
        this.email = reporter.getEmail();
    }

    public Boolean isValid() {
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(phone);
    }
}
