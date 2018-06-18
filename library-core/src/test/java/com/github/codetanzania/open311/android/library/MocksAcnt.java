package com.github.codetanzania.open311.android.library;

import com.github.codetanzania.open311.android.library.api.models.ApiAccountAccessor;
import com.github.codetanzania.open311.android.library.api.models.ApiBill;
import com.github.codetanzania.open311.android.library.api.models.ApiCustomerAccount;
import com.github.codetanzania.open311.android.library.models.customer.Accessor;
import com.github.codetanzania.open311.android.library.models.customer.Bill;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;
import com.github.codetanzania.open311.android.library.models.customer.LineItem;
import com.github.codetanzania.open311.android.library.utils.DateUtils;

import static junit.framework.Assert.assertEquals;

public class MocksAcnt {
    private static String mockNumber = "92389";
    private static String mockName = "Marlon Lang";
    private static String mockPhone = "255719818179";
    private static String mockEmail = "thad_konopelski@gmail.com";
    private static String mockNeighborhood = "Spinkamouth";
    private static String mockAddress = "391 Weimann Island";
    private static String mockLocale = "en";
    private static double mockLat = 34.49401440942532;
    private static double mockLong = -62.53416466705956;
    private static Boolean mockActiveAt = true;
    private static String mockId = "5b229dc1a876bf0004db6e57";
    private static String mockUpdatedAt = "2018-06-14T16:54:25.482Z";
    private static String mockCreatedAt = "2018-06-14T16:54:25.482Z";

    private static String mockAccessorLocale = "sw";
    private static String mockAccessorName = "Naomi Powlowski";
    private static String mockAccessorPhone = "255756995672";
    private static String mockAccessorEmail = "deon_murphy@hotmail.com";
    private static String mockAccessorCreatedAt = "2018-06-14T16:54:25.478Z";
    private static String mockAccessorUpdatedAt = "2018-06-14T16:54:25.488Z";
    private static String mockAccessorVerifiedAt = "2018-06-14T16:54:25.488Z";

    private static String mockSubItemName = "Previous Readings";
    private static Integer mockSubItemQuantity = 832;
    private static String mockSubItemUnit = "cbm";
    private static String mockSubItemTime = "2018-05-14T16:54:24.804Z";
    private static Float mockSubItemPrice = 1453f;

    private static String mockItemName = "Unit Consumed";
    private static Integer mockItemQuantity = 427;
    private static String mockItemUnit = "m3";
    private static String mockItemTime = "2018-05-15T16:54:24.804Z";
    private static Float mockItemPrice = 73f;
    // static String mockItemTotal = ""; Should exist?

    private static String mockBillNumber = "14517";
    private static String mockBilledAt = "2018-05-14T16:54:24.804Z";
    private static String mockBillStartedAt = "2018-04-14T16:54:24.804Z";
    private static String mockBillEndedAt = "2018-05-14T16:54:24.804Z";
    private static String mockBillDuedAt = "2018-06-14T16:54:24.804Z";
    private static Float mockBillOutstanding = 2586f;
    private static Float mockBillOpen = 46f;
    private static Float mockBillCharges = 150f;
    private static Float mockBillDebt = 311f;
    private static Float mockBillClose = 437f;
    private static String mockBillCurrency = "UYU UYI";
    private static String mockBillNotes = "Praesentium dolores debitis occaecati.";

    static String sampleJsonResponse = "{\n" +
            "    \"number\": \""+mockNumber+"\",\n" +
            "    \"name\": \""+mockName+"\",\n" +
            "    \"phone\": \""+mockPhone+"\",\n" +
            "    \"email\": \""+mockEmail+"\",\n" +
            "    \"neighborhood\": \""+mockNeighborhood+"\",\n" +
            "    \"address\": \""+mockAddress+"\",\n" +
            "    \"locale\": \""+mockLocale+"\",\n" +
            "    \"location\": {\n" +
            "        \"type\": \"Point\",\n" +
            "        \"coordinates\": [\n" +
            "            "+mockLong+",\n" +
            "            "+mockLat+"\n" +
            "        ]\n" +
            "    },\n" +
            "    \"accessors\": [\n" +
            "        {\n" +
            "            \"locale\": \""+mockAccessorLocale+"\",\n" +
            "            \"name\": \""+mockAccessorName+"\",\n" +
            "            \"phone\": \""+mockAccessorPhone+"\",\n" +
            "            \"email\": \""+mockAccessorEmail+"\",\n" +
            "            \"createdAt\": \""+mockAccessorCreatedAt+"\",\n" +
            "            \"updatedAt\": \""+mockAccessorUpdatedAt+"\",\n" +
            "            \"verifiedAt\": \""+mockAccessorVerifiedAt+"\"\n" +
            "        }" +
            "    ],\n" +
            "    \"bills\": [\n" +
            "        {\n" +
            "            \"items\": [\n" +
            "                {\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"name\": \""+mockSubItemName+"\",\n" +
            "                            \"quantity\": "+ mockSubItemQuantity +",\n" +
            "                            \"unit\": \""+mockSubItemUnit+"\",\n" +
            "                            \"time\": \""+mockSubItemTime+"\",\n" +
            "                            \"price\": \""+mockSubItemPrice+"\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"name\": \""+mockItemName+"\",\n" +
            "                    \"quantity\": "+mockItemQuantity+",\n" +
            "                    \"unit\": \""+mockItemUnit+"\",\n" +
            "                    \"price\": \""+mockItemPrice+"\",\n" +
            "                    \"time\": \""+mockItemTime+"\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"number\": \""+mockBillNumber+"\",\n" +
            "            \"period\": {\n" +
            "                \"billedAt\": \""+mockBilledAt+"\",\n" +
            "                \"startedAt\": \""+ mockBillStartedAt +"\",\n" +
            "                \"endedAt\": \""+ mockBillEndedAt +"\",\n" +
            "                \"duedAt\": \""+ mockBillDuedAt +"\"\n" +
            "            },\n" +
            "            \"balance\": {\n" +
            "                \"outstand\": "+ mockBillOutstanding +",\n" +
            "                \"open\": "+ mockBillOpen +",\n" +
            "                \"charges\": "+ mockBillCharges +",\n" +
            "                \"debt\": "+ mockBillDebt +",\n" +
            "                \"close\": "+ mockBillClose +"\n" +
            "            },\n" +
            "            \"currency\": \""+ mockBillCurrency +"\",\n" +
            "            \"notes\": \""+ mockBillNotes +"\"\n" +
            "        }"+
            "    ],\n" +
            "    \"active\": "+mockActiveAt+",\n" +
            "    \"_id\": \""+mockId+"\",\n" +
            "    \"updatedAt\": \""+mockUpdatedAt+"\",\n" +
            "    \"createdAt\": \""+mockCreatedAt+"\"\n" +
            "}";

    public static void assertAccountMatchesMock(ApiCustomerAccount account) {
        assertEquals("Number should be correct", mockNumber, account.number);
        assertEquals("Name should be correct", mockName, account.name);
        assertEquals("Phone should be correct", mockPhone, account.phone);
        assertEquals("Email should be correct", mockEmail, account.email);
        assertEquals("Neighborhood should be correct", mockNeighborhood, account.neighborhood);
        assertEquals("Address should be correct", mockAddress, account.address);
        assertEquals("Locale should be correct", mockLocale, account.locale);
        assertEquals("Lat should be correct", mockLat, account.location.getLatitude());
        assertEquals("Lng should be correct", mockLong, account.location.getLongitude());
        assertEquals("ActiveAt should be correct", mockActiveAt, account.active);
        assertEquals("Id should be correct", mockId, account._id);
        assertEquals("UpdatedAt should be correct", mockUpdatedAt, account.updatedAt);
        assertEquals("CreatedAt should be correct", mockCreatedAt, account.createdAt);

        ApiAccountAccessor accessor = account.accessors[0];
        assertEquals("AccessorLocale should be correct", mockAccessorLocale, accessor.getLocale());
        assertEquals("AccessorName should be correct", mockAccessorName, accessor.getName());
        assertEquals("AccessorPhone should be correct", mockAccessorPhone, accessor.getPhone());
        assertEquals("AccessorEmail should be correct", mockAccessorEmail, accessor.getEmail());
        assertEquals("AccessorCreatedAt should be correct", mockAccessorCreatedAt, accessor.getCreatedAt());
        assertEquals("AccessorUpdatedAt should be correct", mockAccessorUpdatedAt, accessor.getUpdatedAt());
        assertEquals("AccessorVerifiedAt should be correct", mockAccessorVerifiedAt, accessor.getVerifiedAt());

        ApiBill bill = account.bills[0];
        ApiBill.Item item = bill.items[0];
        ApiBill.Item subItem = item.items[0];
        assertEquals("SubItemName should be correct", mockSubItemName, subItem.name);
        assertEquals("SubItemQuantity should be correct", mockSubItemQuantity, subItem.quantity);
        assertEquals("SubItemUnit should be correct", mockSubItemUnit, subItem.unit);
        assertEquals("SubItemTime should be correct", mockSubItemTime, subItem.time);
        assertEquals("SubItemPrice should be correct", mockSubItemPrice, subItem.price);

        assertEquals("ItemName should be correct",  mockItemName, item.name);
        assertEquals("ItemQuantity should be correct", mockItemQuantity, item.quantity);
        assertEquals("ItemUnit should be correct", mockItemUnit, item.unit);
        assertEquals("ItemTime should be correct", mockItemTime, item.time);
        assertEquals("ItemPrice should be correct", mockItemPrice, item.price);
        // static String mockItemTotal = ""; Should exist?

        assertEquals("BillNumber should be correct", mockBillNumber, bill.number);
        assertEquals("BilledAt should be correct", mockBilledAt, bill.period.billedAt);
        assertEquals("BillStartedAt should be correct", mockBillStartedAt, bill.period.startedAt);
        assertEquals("BillEndedAt should be correct", mockBillEndedAt, bill.period.endedAt);
        assertEquals("BillDuedAt should be correct", mockBillDuedAt, bill.period.duedAt);
        assertEquals("BillOutstanding should be correct", mockBillOutstanding, bill.balance.outstand);
        assertEquals("BillOpen should be correct", mockBillOpen, bill.balance.open);
        assertEquals("BillCharges should be correct", mockBillCharges, bill.balance.charges);
        assertEquals("BillDebt should be correct", mockBillDebt, bill.balance.debt);
        assertEquals("BillClose should be correct", mockBillClose, bill.balance.close);
        assertEquals("BillCurrency should be correct", mockBillCurrency, bill.currency);
        assertEquals("BillNotes should be correct", mockBillNotes, bill.notes);
    }

    public static void assertAccountMatchesMock(CustomerAccount account) {
        assertEquals("Number should be correct", mockNumber, account.getNumber());
        assertEquals("Name should be correct", mockName, account.getName());
        assertEquals("Phone should be correct", mockPhone, account.getPhone());
        assertEquals("Email should be correct", mockEmail, account.getEmail());
        assertEquals("Neighborhood should be correct", mockNeighborhood, account.getNeighborhood());
        assertEquals("Address should be correct", mockAddress, account.getAddress());
        assertEquals("Locale should be correct", mockLocale, account.getLocale());
        assertEquals("Lat should be correct", mockLat, account.getLocation().getLatitude());
        assertEquals("Lng should be correct", mockLong, account.getLocation().getLongitude());
        assertEquals("ActiveAt should be correct", mockActiveAt, account.isActive());
        assertEquals("Id should be correct", mockId, account.getId());
        assertEquals("UpdatedAt should be correct", mockUpdatedAt, DateUtils.formatForServer(account.getUpdatedAt()));
        assertEquals("CreatedAt should be correct", mockCreatedAt, DateUtils.formatForServer(account.getCreatedAt()));

        Accessor accessor = account.getAccessors().get(0);
        assertEquals("AccessorLocale should be correct", mockAccessorLocale, accessor.getLocale());
        assertEquals("AccessorName should be correct", mockAccessorName, accessor.getName());
        assertEquals("AccessorPhone should be correct", mockAccessorPhone, accessor.getPhone());
        assertEquals("AccessorEmail should be correct", mockAccessorEmail, accessor.getEmail());
        assertEquals("AccessorCreatedAt should be correct", mockAccessorCreatedAt, DateUtils.formatForServer(accessor.getCreatedAt()));
        assertEquals("AccessorUpdatedAt should be correct", mockAccessorUpdatedAt, DateUtils.formatForServer(accessor.getUpdatedAt()));
        assertEquals("AccessorVerifiedAt should be correct", mockAccessorVerifiedAt, DateUtils.formatForServer(accessor.getVerifiedAt()));

        Bill bill = account.getBills().get(0);
        LineItem item = bill.getItems().get(0);
        LineItem subItem = item.getItems().get(0);
        assertEquals("SubItemName should be correct", mockSubItemName, subItem.getName());
        assertEquals("SubItemQuantity should be correct", mockSubItemQuantity, subItem.getQuantity());
        assertEquals("SubItemUnit should be correct", mockSubItemUnit, subItem.getUnit());
        assertEquals("SubItemTime should be correct", mockSubItemTime, DateUtils.formatForServer(subItem.getTime()));
        assertEquals("SubItemPrice should be correct", mockSubItemPrice, subItem.getPrice());

        assertEquals("ItemName should be correct",  mockItemName, item.getName());
        assertEquals("ItemQuantity should be correct", mockItemQuantity, item.getQuantity());
        assertEquals("ItemUnit should be correct", mockItemUnit, item.getUnit());
        assertEquals("ItemTime should be correct", mockItemTime, DateUtils.formatForServer(item.getTime()));
        assertEquals("ItemPrice should be correct", mockItemPrice, item.getPrice());
        // static String mockItemTotal = ""; Should exist?

        assertEquals("BillNumber should be correct", mockBillNumber, bill.getNumber());
        assertEquals("BilledAt should be correct", mockBilledAt, DateUtils.formatForServer(bill.getPeriod().getBilledAt()));
        assertEquals("BillStartedAt should be correct", mockBillStartedAt, DateUtils.formatForServer(bill.getPeriod().getStartedAt()));
        assertEquals("BillEndedAt should be correct", mockBillEndedAt, DateUtils.formatForServer(bill.getPeriod().getEndedAt()));
        assertEquals("BillDuedAt should be correct", mockBillDuedAt, DateUtils.formatForServer(bill.getPeriod().getDuedAt()));
        assertEquals("BillOutstanding should be correct", mockBillOutstanding, bill.getBalance().getOutstanding());
        assertEquals("BillOpen should be correct", mockBillOpen, bill.getBalance().getOpen());
        assertEquals("BillCharges should be correct", mockBillCharges, bill.getBalance().getCharges());
        assertEquals("BillDebt should be correct", mockBillDebt, bill.getBalance().getDebt());
        assertEquals("BillClose should be correct", mockBillClose, bill.getBalance().getClose());
        assertEquals("BillCurrency should be correct", mockBillCurrency, bill.getCurrency());
        assertEquals("BillNotes should be correct", mockBillNotes, bill.getNotes());
    }
}
