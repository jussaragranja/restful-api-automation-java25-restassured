package br.com.ju.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;


import java.util.Map;

public class ObjectClient {

    private final String baseUri;


    public ObjectClient(String baseUri) {
        this.baseUri = baseUri;
    }


    public Response getObjects() {
        return given()
                .baseUri(baseUri)
                .accept(ContentType.JSON)
                .when()
                .get("/objects")
                .then()
                .extract().response();
    }


    public Response getObjectById(String id) {
        return given()
                .baseUri(baseUri)
                .accept(ContentType.JSON)
                .when()
                .get("/objects/" + id)
                .then()
                .extract().response();
    }


    public Response createObject(Map<String, Object> body) {
        return given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/objects")
                .then()
                .extract().response();
    }


    public Response updateObject(String id, Map<String, Object> body) {
        return given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/objects/" + id)
                .then()
                .extract().response();
    }


    public Response patchObject(String id, Map<String, Object> body) {
        return given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch("/objects/" + id)
                .then()
                .extract().response();
    }


    public Response deleteObject(String id) {
        return given()
                .baseUri(baseUri)
                .when()
                .delete("/objects/" + id)
                .then()
                .extract().response();
    }

}
