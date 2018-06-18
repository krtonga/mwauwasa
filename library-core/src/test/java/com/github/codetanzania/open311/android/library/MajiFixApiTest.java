package com.github.codetanzania.open311.android.library;

import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Dave - Work on 12/1/2017.
 */

//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class)
public class MajiFixApiTest {

    static String getEndpoint() {
        MajiFix.setup(RuntimeEnvironment.application, MocksSrvcRq.mockEndPoint, "com.github.codetanzania.open311.android.library.fileprovider");
        String currentEndpoint = MajiFix.getBaseEndpoint();
        if (currentEndpoint.equals(MocksSrvcRq.mockEndPoint))
            return null;
        return currentEndpoint;
    }
//
//    @Test
//    public void problemsByPhoneNumberTest() throws IOException {
//        int responses = 42;
//        String[] mock = MocksSrvcRq.generateProblemQueryResponse(responses);
//
//        MockWebServer server = new MockWebServer();
//        for (int i = 0; i < mock.length; i++) {
//            server.enqueue(new MockResponse().setBody(mock[i]));
//        }
//
//        String endpoint = getEndpoint();
//        if (endpoint == null) {
//            server.start();
//            MajiFix.setBaseEndpoint(server.url("/").toString());
//        } else {
//            String[] split = endpoint.split("[:/]");
//            endpoint = split[split.length - 1];
//            server.start(Integer.valueOf(endpoint));
//        }
//
//
//
//        TestObserver<ArrayList<Problem>> tester =
//                MajiFixAPI.getInstance().getProblemsByPhoneNumber("8675309")
//                        .test();
//
//        assertTrue("Stream didn't terminate", tester.awaitTerminalEvent());
//        assertEquals("Should get out as many problems as were requested",
//                responses,
//                ((ArrayList<Problem>) tester.getEvents().get(0).get(0)).size());
//        assertEquals("Made the wrong number of server calls", mock.length, server.getRequestCount());
//
//        server.shutdown();
//    }
}
