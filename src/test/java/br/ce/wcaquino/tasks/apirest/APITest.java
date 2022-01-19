package br.ce.wcaquino.tasks.apirest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefa() {
        RestAssured.given()
            .when()
                .get("/todo")
            .then()
                .log().all()
                .statusCode(200)
            ;
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        RestAssured.given()
                .body("{\"task\":\"Teste via API\",\"dueDate\":\"2022-01-30\"}")
                .contentType(ContentType.JSON)
            .when()
                .post("/todo")
            .then()
                .log().all()
                .statusCode(201)
            ;
    }

    @Test
    public void naoDeveRealizarTarefaInvalida() {
        RestAssured.given()
                .body("{\"task\":\"Teste via API\",\"dueDate\":\"2010-01-30\"}")
                .contentType(ContentType.JSON)
            .when()
                .post("/todo")
            .then()
                .log().all()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));
            ;
    }

    @Test
    public void deveRemoverTarefaComSucesso() {
        //create task
        Integer id = RestAssured.given()
                .body("{\"task\":\"Teste via API\",\"dueDate\":\"2022-01-30\"}")
                .contentType(ContentType.JSON)
            .when()
                .post("/todo")
            .then()
                .log().all()
                .statusCode(201)
                .extract().path("id")
            ;
            System.out.println(id);
        //remove task
        RestAssured.given()
            .when()
                .delete("/todo/"+id)
            .then()
                .log().all()
                .statusCode(204)
            ;
    }
}