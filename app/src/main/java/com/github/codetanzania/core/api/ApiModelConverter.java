package com.github.codetanzania.core.api;

import com.github.codetanzania.core.api.model.ApiServiceRequest;
import com.github.codetanzania.core.model.Attachment;
import com.github.codetanzania.core.model.Priority;
import com.github.codetanzania.core.model.Service;
import com.github.codetanzania.core.model.ServiceRequest;
import com.github.codetanzania.core.model.Status;

import java.util.ArrayList;

/**
 * This is used to create app objects from what is returned from the server.
 */

public class ApiModelConverter {

    private static ServiceRequest convert(ApiServiceRequest apiRequest) {
        ServiceRequest request = new ServiceRequest();
        request.id = apiRequest._id;
        request.code = apiRequest.code;
        request.description = apiRequest.description;
        request.reporter = apiRequest.reporter;
        request.service = new Service(apiRequest.service.code,
                apiRequest.service.name, apiRequest.service.color);
        request.jurisdiction = apiRequest.jurisdiction.name;
        request.address = apiRequest.address;
        request.longitude = apiRequest.longitude;
        request.latitude = apiRequest.latitude;
        @Status.Type int type = "CLOSED".equalsIgnoreCase(apiRequest.status.name)
                ? Status.CLOSED : Status.OPEN;
        request.status = new Status(type, apiRequest.status.name, apiRequest.status.color);
        request.priority = new Priority(apiRequest.priority.getWeight(), apiRequest.priority.getName(), apiRequest.priority.getColor());
        request.createdAt = apiRequest.createdAt;
        request.updatedAt = apiRequest.updatedAt;
        request.resolvedAt = apiRequest.resolvedAt;
        if (apiRequest.attachments != null && !apiRequest.attachments.isEmpty()) {
            //System.out.println("Attachment: "+apiRequest.attachments.size()+", "+apiRequest.attachments.get(0).getContent());
            ArrayList<Attachment> firstAttachment = new ArrayList<>(1);
            firstAttachment.add(apiRequest.attachments.get(0));
            request.setAttachments(firstAttachment);
        }
        request.comments = apiRequest.comments;
        return request;
    }

    public static ArrayList<ServiceRequest> convert(ApiServiceRequest[] apiRequests) {
        ArrayList<ServiceRequest> converted = new ArrayList<>();
        for (ApiServiceRequest apiRequest : apiRequests) {
            converted.add(convert(apiRequest));
        }
        return converted;
    }
//
//    public static ServiceRequest convert(Problem fromLibrary) {
//        ServiceRequest request = new ServiceRequest();
////        request.id = fromLibrary.getId();
//        request.code = fromLibrary.getTicketNumber();
//        request.description = fromLibrary.getDescription();
//        if (fromLibrary.getReporter() != null) {
//            request.reporter = new Reporter();
//            request.reporter.name = fromLibrary.getReporter().getName();
//            request.reporter.phone = fromLibrary.getReporter().getPhone();
//            request.reporter.account = fromLibrary.getReporter().getAccount();
//            request.reporter.email = fromLibrary.getReporter().getEmail();
//        }
//        if (fromLibrary.getCategory() != null) {
//            request.service = new Service(fromLibrary.getCategory().getCode(), fromLibrary.getCategory().getName(), null);
//        }
//        request.jurisdiction = null;
//        request.address = fromLibrary.getAddress();
//        if (fromLibrary.getLocation() != null) {
//            request.longitude = (float) fromLibrary.getLocation().getLongitude();
//            request.latitude = (float) fromLibrary.getLocation().getLatitude();
//        }
//        if (fromLibrary.getStatus() != null) {
//            @Status.Type int type = fromLibrary.isOpen() ? Status.OPEN : Status.CLOSED;
//            request.status = new Status(
//                    type, fromLibrary.getStatus().getName(), fromLibrary.getStatus().getColor());
//        }
//        if (fromLibrary.getPriority() != null) {
//            request.priority = new Priority(fromLibrary.getPriority().getWeight(), fromLibrary.getPriority().getName(), fromLibrary.getPriority().getColor());
//        }
//        if (fromLibrary.getCreatedAt() != null) {
//            request.createdAt = fromLibrary.getCreatedAt().getTime();
//        }
//        if (fromLibrary.getUpdatedAt() != null) {
//            request.updatedAt = fromLibrary.getUpdatedAt().getTime();
//        }
//        if (fromLibrary.getResolvedAt() != null) {
//            request.resolvedAt = fromLibrary.getResolvedAt().getTime();
//        }
//        if (fromLibrary.hasAttachments()) {
//            request.setImageUri(fromLibrary.getAttachments().get(0));
//        }
////        request.comments = apiRequest.comments;
//        return request;
//    }
}
