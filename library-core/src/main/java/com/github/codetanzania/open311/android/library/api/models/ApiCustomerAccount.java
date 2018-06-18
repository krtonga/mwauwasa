package com.github.codetanzania.open311.android.library.api.models;

public class ApiCustomerAccount {
    public String number;
    public String name;
    public String phone;
    public String email;
    public String neighborhood;
    public String address;
    public String locale;
    public ApiLocation location;
    public ApiAccountAccessor[] accessors;
    public ApiBill[] bills;
    public Boolean active;
    public String _id;
    public String createdAt;
    public String updatedAt;
}
