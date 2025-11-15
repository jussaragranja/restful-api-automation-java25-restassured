package br.com.ju.api;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;


import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;


@Epic("Public API restfull tests")
@Feature("Objects resource")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ObjectApiTests extends BaseTest {

    private static String createdId;

    @Test
    @Order(1)
    @Description("GET list of objects - should return 200 and an array")
    public void testGetObjects() {
        Response r = client.getObjects();
        Allure.addAttachment("Response GET /objects", r.asString());
        assertEquals(200, r.statusCode(), "expected 200 on GET /objects");
        assertNotNull(r.getBody());
        assertTrue(r.jsonPath().getList("$") != null, "response should be a JSON array");
    }

    @Test
    @Order(2)
    @Description("Create an object via POST - should return 201 and object with id")
    public void testCreateObject() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Automated Test Device");
        Map<String, Object> data = new HashMap<>();
        data.put("price", 999.99);
        data.put("color", "black");
        payload.put("data", data);
        Response r = client.createObject(payload);
        Allure.addAttachment("Response POST /objects", r.asString());
        assertTrue(r.statusCode() == 201 || r.statusCode() == 200, "create should return 201 or 200 depending on API behavior");
        createdId = r.jsonPath().getString("id");
        assertNotNull(createdId, "created object id should not be null");
    }

    @Test
    @Order(3)
    @Description("Get single object by id - should return 200 and correct id")
    public void testGetObjectById() {
        Assumptions.assumeTrue(createdId != null && !createdId.isBlank(),
                "requires createdId from create test");
        Response r = client.getObjectById(createdId);
        Allure.addAttachment("Response GET /objects/{id}", r.asString());
        assertEquals(200, r.statusCode(), "expected 200 when fetching created object");
                assertEquals(createdId, r.jsonPath().getString("id"), "returned id should match created id");
    }

    @Test
    @Order(4)
    @Description("Update object with PUT - should return 200 and updated fields")
    public void testUpdateObject() {
        Assumptions.assumeTrue(createdId != null && !createdId.isBlank());
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Automated Test Device Updated");
        Map<String, Object> data = new HashMap<>();
        data.put("price", 1099.99);
        data.put("color", "silver");
        payload.put("data", data);
        Response r = client.updateObject(createdId, payload);
        Allure.addAttachment("Response PUT /objects/{id}", r.asString());
        assertTrue(r.statusCode() == 200 || r.statusCode() == 204, "PUT should return 200 or 204");
        if (r.statusCode() == 200) {
            assertEquals("Automated Test Device Updated",
                    r.jsonPath().getString("name"));
        }
    }

    @Test
    @Order(5)
    @Description("Patch object name - should return 200 and updated name")
    public void testPatchObject() {
        Assumptions.assumeTrue(createdId != null && !createdId.isBlank());
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Automated Test Device (Patched)");
        Response r = client.patchObject(createdId, payload);
        Allure.addAttachment("Response PATCH /objects/{id}", r.asString());
        assertTrue(r.statusCode() == 200 || r.statusCode() == 204,
                "PATCH should return 200 or 204");
    }

    @Test
    @Order(6)
    @Description("Delete created object - should return 200/204/202")
    public void testDeleteObject() {
        Assumptions.assumeTrue(createdId != null && !createdId.isBlank());
        Response r = client.deleteObject(createdId);
        Allure.addAttachment("Response DELETE /objects/{id}", r.asString());
        assertTrue(r.statusCode() == 200 || r.statusCode() == 204 ||
                r.statusCode() == 202, "DELETE should return successful status");
    }
}