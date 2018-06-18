package com.github.codetanzania.feature.issuedetails;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.codetanzania.open311.android.library.models.Problem;

/*
 * PhotoMapViewPagerAdapter is a ViewPagerAdapter that does the
 * transitioning of the IssueItems (Map and Picture) which were
 * submitted when user was reporting an issue.
 */
public class PhotoMapViewPagerAdapter extends FragmentStatePagerAdapter {

    private final Problem mServiceRequest;
    private final int nPages;

    public PhotoMapViewPagerAdapter(FragmentManager fm, Problem serviceRequest, int numPages) {
        super(fm);
        mServiceRequest = serviceRequest;
        if (serviceRequest.hasAttachments() &&
                serviceRequest.getAttachments().get(0) != null) {
            nPages = serviceRequest.getAttachments().size()+1;

            for (String filePath : serviceRequest.getAttachments()) {
                Uri uri = Uri.parse(filePath);
            }
        } else {
            nPages = 1;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();

        if (position == nPages-1) {
            return StaticMapFragment.getNewInstance(
                    (float) mServiceRequest.getLocation().getLatitude(),
                    (float) mServiceRequest.getLocation().getLongitude());
        }
        else {
            return PhotoItemFragment.getNewInstance(
                    mServiceRequest.getAttachments().get(position));
        }
    }

    @Override
    public int getCount() {
        return nPages;
    }
}
