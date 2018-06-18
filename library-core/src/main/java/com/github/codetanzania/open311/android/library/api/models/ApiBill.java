package com.github.codetanzania.open311.android.library.api.models;

public class ApiBill {
    public Item[] items;
    public String number;
    public Period period;
    public Balance balance;
    public String currency;
    public String notes;

    public static class Item {
        public String name;
        public Integer quantity;
        public String unit;
        public String time;
        public Float price;
        public Item[] items;
    }

    public static class Period {
        public String billedAt;
        public String startedAt;
        public String endedAt;
        public String duedAt;
    }

    public static class Balance {
        public Float outstand;
        public Float open;
        public Float charges;
        public Float debt;
        public Float close;
    }
}
