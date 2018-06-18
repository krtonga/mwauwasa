package com.github.codetanzania.utils;

import com.github.codetanzania.core.model.Reporter;
import com.github.codetanzania.core.model.ServiceRequest;
import com.github.codetanzania.core.model.Status;
import com.ibm.icu.text.SimpleDateFormat;

import junit.framework.Assert;

/**
 * This shows responses as they are expected from the server, and is used for mocking in tests.
 */

public class Mocks {
    final static private String reporterAccount = "testAccount";
    final static public String reporterPhone = "255111111";
    final static public String reporterName = "testUser";
    final static private String reporterEmail = "test@test.com";
    static public Reporter mockReporter;

    static public Reporter getMockReporter() {
        if (mockReporter == null) {
            Reporter reporter = new Reporter();
            reporter.account = reporterAccount;
            reporter.phone = reporterPhone;
            reporter.name = reporterName;
            reporter.email = reporterEmail;
            mockReporter = reporter;
        }
        return mockReporter;
    }

    final static public String validOpen311ServiceCategory = "{\"services\":[{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"I\",\"name\":\"Illegal\",\"color\":\"#263238\",\"_id\":\"5975c8b57f121c4b1bbb59b6\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5975c8b57f121c4b1bbb59b6\"},\"code\":\"WTH\",\"name\":\"Water Theft\",\"description\":\"Water Theft\",\"color\":\"#D32F2F\",\"priority\":{\"name\":\"Critical\",\"weight\":5,\"color\":\"#F44336\",\"_id\":\"5968b63d48dfc224bb477445\",\"createdAt\":\"2017-07-14T12:17:01.286Z\",\"updatedAt\":\"2017-07-14T12:17:01.286Z\",\"uri\":\"https://dawasco.herokuapp.com/priorities/5968b63d48dfc224bb477445\"},\"isExternal\":true,\"_id\":\"5975ca65e013ad4ceac053a5\",\"createdAt\":\"2017-07-24T10:22:29.946Z\",\"updatedAt\":\"2017-10-16T14:47:06.595Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5975ca65e013ad4ceac053a5\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"N\",\"name\":\"Non Commercial\",\"color\":\"#960F1E\",\"_id\":\"5968b64148dfc224bb477480\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb477480\"},\"code\":\"LW\",\"name\":\"Lack of Water\",\"description\":\"Lack of Water related service request(issue)\",\"color\":\"#C62828\",\"priority\":{\"name\":\"Critical\",\"weight\":5,\"color\":\"#F44336\",\"_id\":\"5968b63d48dfc224bb477445\",\"createdAt\":\"2017-07-14T12:17:01.286Z\",\"updatedAt\":\"2017-07-14T12:17:01.286Z\",\"uri\":\"https://dawasco.herokuapp.com/priorities/5968b63d48dfc224bb477445\"},\"isExternal\":true,\"_id\":\"5968b64148dfc224bb47748d\",\"createdAt\":\"2017-07-14T12:17:05.968Z\",\"updatedAt\":\"2017-10-16T14:44:14.142Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64148dfc224bb47748d\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"N\",\"name\":\"Non Commercial\",\"color\":\"#960F1E\",\"_id\":\"5968b64148dfc224bb477480\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb477480\"},\"code\":\"WL\",\"name\":\"Water Leakage\",\"description\":\"Water Leakage related service request(issue)\",\"color\":\"#AD1457\",\"priority\":{\"name\":\"Critical\",\"weight\":5,\"color\":\"#F44336\",\"_id\":\"5968b63d48dfc224bb477445\",\"createdAt\":\"2017-07-14T12:17:01.286Z\",\"updatedAt\":\"2017-07-14T12:17:01.286Z\",\"uri\":\"https://dawasco.herokuapp.com/priorities/5968b63d48dfc224bb477445\"},\"isExternal\":true,\"_id\":\"5968b64148dfc224bb47748e\",\"createdAt\":\"2017-07-14T12:17:05.977Z\",\"updatedAt\":\"2017-10-16T14:44:56.112Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64148dfc224bb47748e\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"N\",\"name\":\"Non Commercial\",\"color\":\"#960F1E\",\"_id\":\"5968b64148dfc224bb477480\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb477480\"},\"code\":\"MP\",\"name\":\"Meter Problem\",\"description\":\"Meter Problems related service request(issue)\",\"color\":\"#6A1B9A\",\"isExternal\":true,\"_id\":\"5968b64148dfc224bb47748f\",\"createdAt\":\"2017-07-14T12:17:05.987Z\",\"updatedAt\":\"2017-10-16T14:45:17.912Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64148dfc224bb47748f\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"C\",\"name\":\"Commercial\",\"color\":\"#06C947\",\"_id\":\"5968b64148dfc224bb47747f\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb47747f\"},\"code\":\"NW\",\"name\":\"New Connection\",\"description\":\"New Connection related service request(issue)\",\"color\":\"#1A237E\",\"isExternal\":true,\"_id\":\"5968b64248dfc224bb477497\",\"createdAt\":\"2017-07-14T12:17:06.073Z\",\"updatedAt\":\"2017-10-16T14:45:43.799Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64248dfc224bb477497\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"N\",\"name\":\"Non Commercial\",\"color\":\"#960F1E\",\"_id\":\"5968b64148dfc224bb477480\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb477480\"},\"code\":\"SL\",\"name\":\"Sewage Leakage\",\"description\":\"Sewage Leakage related service request(issue)\",\"color\":\"#00838F\",\"priority\":{\"name\":\"Normal\",\"weight\":0,\"color\":\"#4CAF50\",\"_id\":\"5968b63d48dfc224bb477444\",\"createdAt\":\"2017-07-14T12:17:01.167Z\",\"updatedAt\":\"2017-07-14T12:17:01.167Z\",\"uri\":\"https://dawasco.herokuapp.com/priorities/5968b63d48dfc224bb477444\"},\"isExternal\":true,\"_id\":\"5968b64248dfc224bb477494\",\"createdAt\":\"2017-07-14T12:17:06.038Z\",\"updatedAt\":\"2017-10-16T14:46:38.848Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64248dfc224bb477494\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"N\",\"name\":\"Non Commercial\",\"color\":\"#960F1E\",\"_id\":\"5968b64148dfc224bb477480\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb477480\"},\"code\":\"WQ\",\"name\":\"Water Quality\",\"description\":\"Water Quality related service request(issue)\",\"color\":\"#0277BD\",\"priority\":{\"name\":\"Normal\",\"weight\":0,\"color\":\"#4CAF50\",\"_id\":\"5968b63d48dfc224bb477444\",\"createdAt\":\"2017-07-14T12:17:01.167Z\",\"updatedAt\":\"2017-07-14T12:17:01.167Z\",\"uri\":\"https://dawasco.herokuapp.com/priorities/5968b63d48dfc224bb477444\"},\"isExternal\":true,\"_id\":\"5968b64248dfc224bb477493\",\"createdAt\":\"2017-07-14T12:17:06.027Z\",\"updatedAt\":\"2017-10-16T14:47:30.866Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64248dfc224bb477493\"},{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"group\":{\"jurisdiction\":{\"code\":\"GRZ\",\"name\":\"Gerezani\",\"phone\":\"255743451864\",\"email\":\"callcenter@dawasco.com\",\"domain\":\"gerezani.dawasco.com\",\"_id\":\"5968b64148dfc224bb47747e\",\"longitude\":0,\"latitude\":0,\"uri\":\"https://dawasco.herokuapp.com/jurisdictions/5968b64148dfc224bb47747e\"},\"code\":\"O\",\"name\":\"Other\",\"color\":\"#C8B1EF\",\"_id\":\"5968b64148dfc224bb477481\",\"uri\":\"https://dawasco.herokuapp.com/servicegroups/5968b64148dfc224bb477481\"},\"code\":\"RO\",\"name\":\"Request Other\",\"description\":\"Other related service request(issue)\",\"color\":\"#C9D13C\",\"priority\":{\"name\":\"Low\",\"weight\":-5,\"color\":\"#1B5E20\",\"_id\":\"5968b63c48dfc224bb477443\",\"createdAt\":\"2017-07-14T12:17:00.762Z\",\"updatedAt\":\"2017-07-14T12:17:00.762Z\",\"uri\":\"https://dawasco.herokuapp.com/priorities/5968b63c48dfc224bb477443\"},\"isExternal\":true,\"_id\":\"5968b64248dfc224bb4774a2\",\"createdAt\":\"2017-07-14T12:17:06.170Z\",\"updatedAt\":\"2017-10-16T14:46:07.029Z\",\"uri\":\"https://dawasco.herokuapp.com/services/5968b64248dfc224bb4774a2\"}],\"pages\":1,\"count\":2}";

    final static public String validRequest =
            "{pages:1, \"servicerequests\": [\n" +
            "{\n" +
            "    \"jurisdiction\": {\n" +
            "        \"code\": \"H\",\n" +
            "        \"name\": \"HQ\",\n" +
            "        \"phone\": \"255714999888\",\n" +
            "        \"email\": \"N/A\",\n" +
            "        \"domain\": \"dawasco.org\",\n" +
            "        \"_id\": \"592029e5e8dd8e00048c184b\",\n" +
            "        \"longitude\": 0,\n" +
            "        \"latitude\": 0,\n" +
            "        \"uri\": \"https://dawasco.herokuapp.com/jurisdictions/592029e5e8dd8e00048c184b\"\n" +
            "    },\n" +
            "    \"group\": {\n" +
            "        \"code\": \"N\",\n" +
            "        \"name\": \"Non Commercial\",\n" +
            "        \"color\": \"#960F1E\",\n" +
            "        \"_id\": \"592029e6e8dd8e00048c184d\",\n" +
            "        \"uri\": \"https://dawasco.herokuapp.com/servicegroups/592029e6e8dd8e00048c184d\"\n" +
            "    },\n" +
            "    \"service\": {\n" +
            "        \"code\": \"LW\",\n" +
            "        \"name\": \"Lack of Water\",\n" +
            "        \"color\": \"#960F1E\",\n" +
            "        \"_id\": \"592029e6e8dd8e00048c1852\",\n" +
            "        \"uri\": \"https://dawasco.herokuapp.com/services/592029e6e8dd8e00048c1852\"\n" +
            "    },\n" +
            "    \"call\": {\n" +
            "        \"startedAt\": \"2017-06-18T15:49:48.483Z\",\n" +
            "        \"endedAt\": \"2017-06-18T15:49:48.483Z\",\n" +
            "        \"duration\": 0\n" +
            "    },\n" +
            "    \"reporter\": {\n" +
            "        \"name\": \"Lally Elias\",\n" +
            "        \"phone\": \"255714095061\"\n" +
            "    },\n" +
            "    \"operator\": {\n" +
            "        \"name\": \"Lally Elias\",\n" +
            "        \"phone\": \"255714095061\",\n" +
            "        \"_id\": \"592029e6e8dd8e00048c185d\",\n" +
            "        \"permissions\": [],\n" +
            "        \"email\": \"lallyelias87@gmail.com\",\n" +
            "        \"uri\": \"https://dawasco.herokuapp.com/parties/592029e6e8dd8e00048c185d\"\n" +
            "    },\n" +
            "    \"code\": \"HLW170026\",\n" +
            "    \"description\": \"Test New Apk\",\n" +
            "    \"address\": \"Some address\",\n" +
            "    \"method\": \"Call\",\n" +
            "    \"location\": {\n" +
            "        \"type\": \"Point\",\n" +
            "        \"coordinates\": [120, 110]\n" +
            "    },\n" +
            "    \"status\": {\n" +
            "        \"name\": \"Open\",\n" +
            "        \"weight\": -5,\n" +
            "        \"color\": \"#0D47A1\",\n" +
            "        \"_id\": \"592029e5e8dd8e00048c180d\",\n" +
            "        \"createdAt\": \"2017-05-20T11:35:01.059Z\",\n" +
            "        \"updatedAt\": \"2017-05-20T11:35:01.059Z\",\n" +
            "        \"uri\": \"https://dawasco.herokuapp.com/statuses/592029e5e8dd8e00048c180d\"\n" +
            "    },\n" +
            "    \"priority\": {\n" +
            "        \"name\": \"Normal\",\n" +
            "        \"weight\": 5,\n" +
            "        \"color\": \"#4CAF50\",\n" +
            "        \"_id\": \"592029e5e8dd8e00048c1817\",\n" +
            "        \"createdAt\": \"2017-05-20T11:35:01.601Z\",\n" +
            "        \"updatedAt\": \"2017-05-20T11:35:01.601Z\",\n" +
            "        \"uri\": \"https://dawasco.herokuapp.com/priorities/592029e5e8dd8e00048c1817\"\n" +
            "    },\n" +
            "    \"attachments\": [],\n" +
            "    \"ttr\": 0,\n" +
            "    \"_id\": \"5946a11c593d370004dbfcf3\",\n" +
            "    \"createdAt\": \"2017-06-18T15:49:48.571Z\",\n" +
            "    \"updatedAt\": \"2017-06-18T15:55:58.183Z\",\n" +
            "    \"ttrSeconds\": 0,\n" +
            "    \"ttrMinutes\": 0,\n" +
            "    \"ttrHours\": 0,\n" +
            "    \"longitude\": 120,\n" +
            "    \"latitude\": 110,\n" +
            "    \"uri\": \"https://dawasco.herokuapp.com/servicerequests/5946a11c593d370004dbfcf3\"\n" +
            "}" +
            "]}";

    public static void isSame(ServiceRequest request) {
        Assert.assertEquals("Id should be the same", "5946a11c593d370004dbfcf3", request.id);
        Assert.assertEquals("Code should be the same", "HLW170026", request.code);
        Assert.assertEquals("Description should be the same", "Test New Apk", request.description);
        Assert.assertEquals("Reporter should be the same", "Lally Elias", request.reporter.name);
        Assert.assertEquals("Service code should be the same", "LW", request.service.code);
        Assert.assertEquals("Service name should be the same", "Lack of Water", request.service.name);
        Assert.assertEquals("Service color should be the same", "#960F1E", request.service.color);
        Assert.assertEquals("Jurisdiction name should be the same", "HQ", request.jurisdiction);
        Assert.assertEquals("Address name should be the same", "Some address", request.address);
        Assert.assertEquals("Longitude name should be the same", 120f, request.longitude);
        Assert.assertEquals("Latitude name should be the same", 110f, request.latitude);
        Assert.assertEquals("Status name should be the same", Status.OPEN, request.status.type);
        Assert.assertEquals("Status color should be the same", "#0D47A1", request.status.color);
        Assert.assertEquals("Priority must be 5.0", 5.0f, request.priority.getWeight());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Assert.assertEquals("Date created should be the same", "2017-06-18T15:49:48", formatter.format(request.createdAt));
        Assert.assertEquals("Date updated should be the same", "2017-06-18T15:55:58", formatter.format(request.updatedAt));
        Assert.assertEquals("Date resolved should be the same", null, request.resolvedAt);
        Assert.assertFalse("Attachments should be the same", request.hasPhotoAttachment());
        //Assert.assertEquals("Comments should be the same", 0, request.comments.size());
    }
}
