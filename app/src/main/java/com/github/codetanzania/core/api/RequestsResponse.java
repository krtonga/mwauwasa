package com.github.codetanzania.core.api;

import com.github.codetanzania.core.model.ServiceRequest;
import com.github.codetanzania.util.ServiceRequestsUtil;

import java.util.ArrayList;


/**
 * This is the response object returned from the server.
 */

public class RequestsResponse {
    private ArrayList<ServiceRequest> mRequests;
    private int mPages;

    public RequestsResponse(ArrayList<ServiceRequest> requests, int pages) {
        mRequests = requests;
        ServiceRequestsUtil.sort(requests);
        mPages = pages;
    }

    public ArrayList<ServiceRequest> getRequests() {
        return mRequests;
    }

    public int getPages() {
        return mPages;
    }
}
