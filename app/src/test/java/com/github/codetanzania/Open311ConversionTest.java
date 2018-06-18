package com.github.codetanzania;

import com.github.codetanzania.core.api.model.Open311Service;
import com.github.codetanzania.utils.Mocks;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
// @Config(manifest = Config.NONE)
public class Open311ConversionTest {

    private List<Open311Service> mServiceCategories;

    @Before
    public void initializeServices() {
        try {
            mServiceCategories = Open311Service.fromJson(Mocks.validOpen311ServiceCategory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_convertOpen311ServiceCategoryFromJSON() {
        Assert.assertNotNull("Conversion must pass", mServiceCategories);
        Assert.assertEquals("Size must be 8", mServiceCategories.size(), 8);
    }
}
