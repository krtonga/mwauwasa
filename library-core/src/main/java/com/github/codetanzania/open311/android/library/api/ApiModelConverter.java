package com.github.codetanzania.open311.android.library.api;

import android.location.Location;
import android.support.annotation.VisibleForTesting;

import com.github.codetanzania.open311.android.library.api.models.ApiAccountAccessor;
import com.github.codetanzania.open311.android.library.api.models.ApiBill;
import com.github.codetanzania.open311.android.library.api.models.ApiChangelog;
import com.github.codetanzania.open311.android.library.api.models.ApiCustomerAccount;
import com.github.codetanzania.open311.android.library.api.models.ApiPriority;
import com.github.codetanzania.open311.android.library.api.models.ApiService;
import com.github.codetanzania.open311.android.library.api.models.ApiStatus;
import com.github.codetanzania.open311.android.library.models.Attachment;
import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.open311.android.library.models.ChangeLog;
import com.github.codetanzania.open311.android.library.models.customer.Accessor;
import com.github.codetanzania.open311.android.library.models.customer.Balance;
import com.github.codetanzania.open311.android.library.models.customer.Bill;
import com.github.codetanzania.open311.android.library.models.customer.BillingPeriod;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;
import com.github.codetanzania.open311.android.library.models.Party;
import com.github.codetanzania.open311.android.library.models.Priority;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.open311.android.library.api.models.ApiAttachment;
import com.github.codetanzania.open311.android.library.api.models.ApiServiceRequest;
import com.github.codetanzania.open311.android.library.api.models.ApiLocation;
import com.github.codetanzania.open311.android.library.api.models.ApiReporter;
import com.github.codetanzania.open311.android.library.api.models.ApiServiceRequestGet;
import com.github.codetanzania.open311.android.library.api.models.ApiServiceRequestPost;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.open311.android.library.models.Status;
import com.github.codetanzania.open311.android.library.models.customer.LineItem;
import com.github.codetanzania.open311.android.library.utils.AttachmentUtils;
import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This is used to saveToFile between the objects from the server, and the objects
 * used within the MajiFix application.
 *
 * Note: Attachments are saved to file system, and accessed from there via a URL, so as
 * to ensure that the can easily be passed around via parcelables and intents.
 */

public class ApiModelConverter {
    public static ApiServiceRequestPost convert(Problem problem) {
        if (problem == null) {
            return null;
        }

        ApiServiceRequestPost request = new ApiServiceRequestPost();
        convertShared(request, problem);
        request.setService(problem.getCategory().getId());
        return request;
    }

    public static Problem convert(ApiServiceRequestGet response) {
        if (response == null) {
            return null;
        }
        Problem.Builder builder = new Problem.Builder(null);
        return builder.buildWithoutValidation(response.getReporter().getName(),
                response.getReporter().getPhone(),
                response.getReporter().getEmail(),
                response.getReporter().getAccount(),
                convert(response.getService()),
                convert(response.getLocation()),
                response.getAddress(),
                response.getDescription(),
                response.getTicketId(),
                convert(response.getStatus()),
                convert(response.getPriority()),
                response.getCreatedAt(),
                response.getUpdatedAt(),
                response.getResolvedAt(),
                saveToFile(response.getAttachments()),
                convert(response.getChangelogs()));
    }

    public static Location convert(ApiLocation apiLocation) {
        if (apiLocation == null) {
            return null;
        }
        Location location = new Location("");
        location.setLatitude(apiLocation.getLatitude());
        location.setLongitude(apiLocation.getLongitude());
        return location;
    }

    public static ApiLocation convert(Location location) {
        if (location == null) {
            return null;
        }
        return new ApiLocation(location.getLatitude(),
                location.getLongitude());
    }

    public static Status convert(ApiStatus apiStatus) {
        if (apiStatus == null) {
            return null;
        }
        return new Status(apiStatus.getId(), apiStatus.getName(), apiStatus.getColor());
    }

    public static Reporter convert(ApiReporter apiReporter) {
        Reporter reporter = new Reporter();
        reporter.setName(apiReporter.getName());
        reporter.setPhone(apiReporter.getPhone());
        reporter.setEmail(apiReporter.getEmail());
        reporter.setAccount(apiReporter.getAccount());
        return reporter;
    }

    public static Category convert(ApiService apiCategory) {
        return new Category(apiCategory.getName(),
                apiCategory.getId(),
                apiCategory.getPriority(),
                apiCategory.getCode());
    }

    private static void convertShared(ApiServiceRequest request, Problem problem) {
        if (problem == null || request == null) {
            return;
        }
        request.setReporter(new ApiReporter(problem.getReporter()));
        request.setLocation(convert(problem.getLocation()));
        request.setAddress(problem.getAddress());
        request.setDescription(problem.getDescription());
        request.setAttachments(getFromFile(problem.getAttachments()));
    }

    public static List<String> saveToFile(ApiAttachment[] apiAttachments) {
        if (apiAttachments == null) {
            return null;
        }
        List<String> attachments = new ArrayList<>(apiAttachments.length);
        for (ApiAttachment apiAttachment : apiAttachments) {
            Attachment attachment = convert(apiAttachment);
            String path = AttachmentUtils.saveAttachment(attachment);
            attachments.add(path);
        }
        return attachments;
    }

    public static ApiAttachment[] getFromFile(List<String> attachmentUrls) {
        if (attachmentUrls == null || attachmentUrls.isEmpty()) {
            return null;
        }
        ApiAttachment[] apiAttachments = new ApiAttachment[attachmentUrls.size()];
        for (int i = 0; i < attachmentUrls.size(); i++) {
            Attachment attachment = AttachmentUtils.getPicAsAttachment(attachmentUrls.get(i));
            if (attachment != null) {
                apiAttachments[i] = convert(attachment);
            }
        }
        return apiAttachments;
    }

    @VisibleForTesting
    public static Attachment convert(ApiAttachment apiAttachment) {
        if (apiAttachment == null) {
            return null;
        }
        return new Attachment(apiAttachment.getName(), apiAttachment.getCaption(),
                apiAttachment.getMime(), apiAttachment.getContent());
    }

    @VisibleForTesting
    public static ApiAttachment convert(Attachment attachment) {
        if (attachment == null) {
            return null;
        }
        return new ApiAttachment(attachment.getName(), attachment.getCaption(),
                attachment.getMime(), attachment.getContent());
    }

    public static List<ChangeLog> convert(ApiChangelog[] apiChangelogs) {
        if (apiChangelogs == null || apiChangelogs.length < 1) {
            return null;
        }
        List<ChangeLog> changeLogs = new ArrayList<>();
        for (ApiChangelog log : apiChangelogs) {
            if (log == null) {
                return null;
            }
            Party changer = log.getChanger();
            boolean isVisible = log.isPublic();

            if (log.getStatus() != null) {
                changeLogs.add(new ChangeLog(changer, convert(log.getStatus()), log.getCreatedAt(), isVisible));
            }
            if (log.getComment() != null) {
                changeLogs.add(new ChangeLog(changer, log.getComment(), log.getCreatedAt(), isVisible));
            }
            if (log.getAssignee() !=  null) {
                changeLogs.add(new ChangeLog(changer, log.getAssignee(), log.getCreatedAt(), isVisible));
            }
            if (log.getPriority() != null) {
                changeLogs.add(new ChangeLog(changer, convert(log.getPriority()), log.getCreatedAt(), isVisible));
            }
        }
        return changeLogs;
    }

    public static Priority convert(ApiPriority apiPriority) {
        if (apiPriority == null) {
            return null;
        }
        return new Priority(apiPriority.getId(), apiPriority.getName(),
                            apiPriority.getColor(), apiPriority.getWeight());
    }

    public static CustomerAccount convert(ApiCustomerAccount apiAccount) {
        if (apiAccount == null) {
            return null;
        }
        return new CustomerAccount(
                apiAccount.number,
                apiAccount.name,
                apiAccount.phone,
                apiAccount.email,
                apiAccount.neighborhood,
                apiAccount.address,
                apiAccount.locale,
                convert(apiAccount.location),
                convert(apiAccount.accessors),
                convert(apiAccount.bills),
                apiAccount.active,
                apiAccount._id,
                DateUtils.getCalendarFromMajiFixApiString(apiAccount.createdAt),
                DateUtils.getCalendarFromMajiFixApiString(apiAccount.updatedAt));
    }

    public static ArrayList<Accessor> convert(ApiAccountAccessor[] apiAccessors) {
        if (apiAccessors == null) {
            return null;
        }
        ArrayList<Accessor> accessors = new ArrayList<>(apiAccessors.length);
        for (ApiAccountAccessor apiAccessor : apiAccessors) {
            accessors.add(convert(apiAccessor));
        }
        return accessors;
    }

    public static Accessor convert(ApiAccountAccessor apiAccessor) {
        if (apiAccessor == null) {
            return null;
        }
        return new Accessor(
                apiAccessor.getLocale(),
                apiAccessor.getName(),
                apiAccessor.getPhone(),
                apiAccessor.getEmail(),
                DateUtils.getCalendarFromMajiFixApiString(apiAccessor.getVerifiedAt()),
                DateUtils.getCalendarFromMajiFixApiString(apiAccessor.getCreatedAt()),
                DateUtils.getCalendarFromMajiFixApiString(apiAccessor.getUpdatedAt()));
    }

    public static ArrayList<Bill> convert(ApiBill[] apiBills) {
        if (apiBills == null) {
            return null;
        }
        ArrayList<Bill> bills = new ArrayList<>(apiBills.length);
        for (ApiBill apiBill : apiBills) {
            bills.add(convert(apiBill));
        }
        return bills;
    }

    public static Bill convert(ApiBill apiBill) {
        if (apiBill == null) {
            return null;
        }
        return new Bill(
                convert(apiBill.items),
                apiBill.number,
                convert(apiBill.period),
                convert(apiBill.balance),
                apiBill.currency,
                apiBill.notes);
    }

    public static ArrayList<LineItem> convert(ApiBill.Item[] apiItems) {
        if (apiItems == null) {
            return null;
        }
        ArrayList<LineItem> items = new ArrayList<>(apiItems.length);
        for (ApiBill.Item apiItem : apiItems) {
            items.add(convert(apiItem));
        }
        return items;
    }

    public static LineItem convert(ApiBill.Item apiItem) {
        if (apiItem == null) {
            return null;
        }
        return new LineItem(
                apiItem.name,
                apiItem.quantity,
                apiItem.unit,
                DateUtils.getCalendarFromMajiFixApiString(apiItem.time),
                apiItem.price,
                convert(apiItem.items));
    }

    public static BillingPeriod convert(ApiBill.Period apiPeriod) {
        if (apiPeriod == null) {
            return null;
        }
        return new BillingPeriod(
                DateUtils.getCalendarFromMajiFixApiString(apiPeriod.billedAt),
                DateUtils.getCalendarFromMajiFixApiString(apiPeriod.startedAt),
                DateUtils.getCalendarFromMajiFixApiString(apiPeriod.endedAt),
                DateUtils.getCalendarFromMajiFixApiString(apiPeriod.duedAt));
    }

    public static Balance convert(ApiBill.Balance apiBalance) {
        if (apiBalance == null) {
            return null;
        }
        return new Balance(
                apiBalance.outstand,
                apiBalance.open,
                apiBalance.charges,
                apiBalance.debt,
                apiBalance.close);
    }
}
