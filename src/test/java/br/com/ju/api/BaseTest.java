package br.com.ju.api;

import br.com.ju.client.ObjectClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import java.io.InputStream;

public abstract class BaseTest {
    protected static ObjectClient client;
    protected static String baseUri;


    @BeforeAll
    public static void setup() throws Exception {
        String envBase = System.getProperty("api.baseUri");
        if (envBase != null && !envBase.isBlank()) {
            baseUri = envBase;
        } else {
            InputStream is = BaseTest.class.getClassLoader().getResourceAsStream("application.yml");
            if (is != null) {
                String content = new String(is.readAllBytes());
                int idx = content.indexOf("baseUri:");
                if (idx >= 0) {
                    int start = content.indexOf('"', idx);
                    int end = content.indexOf('"', start + 1);
                    baseUri = content.substring(start + 1, end);
                }
            }
        }


        if (baseUri == null || baseUri.isBlank()) {
            baseUri = "https://api.restful-api.dev";
        }


        RestAssured.baseURI = baseUri;
        client = new ObjectClient(baseUri);
    }
}