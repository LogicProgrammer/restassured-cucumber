package com.cocomore.step;

import com.cocomore.framework.PropertyHandler;
import com.cocomore.framework.RestClient;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.javafaker.Faker;
import io.cucumber.datatable.DataTable;
import io.cucumber.docstring.DocString;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;

public class RestApiStep {

    PropertyHandler handler;
    RestClient client = new RestClient();
    String body = null;
    String method = null;
    String endPoint = null;
    int userid = 0;
    Map<String,String> current_user = new HashMap<>();


    private Map<String,String> headers = new HashMap<String,String>(){{
        put("Accept","application/json");
        put("content-type","application/json");
    }};

    public RestApiStep(){
        handler = new PropertyHandler("src/main/resources/config.properties");
    }

    public void setup(){
        client.setBaseUrl(handler.getProperty("test.base.uri"));
        headers.put("Authorization","Bearer "+handler.getProperty("access.token"));
        client.addHeaders(headers);
    }

    @Given("user provides url params as :")
    public void addParameters(DataTable table){
        Map<String,String> params = table.asMap(String.class,String.class);
        client.addParams(params);
    }

    @Given("user provides header params as :")
    public void addHeaders(DataTable table){
        Map<String,String> params = table.asMap(String.class,String.class);
        client.addHeaders(params);
    }

    @Given("user provides request body as :")
    public void addBody(DocString string){

    }

    @Given("user sets the method as {string}.")
    public void setMethod(String method){
        this.method = method;
    }

    @When("user hits the service.")
    public void hitService(){
        setup();
        client.doRequest(endPoint,method,null);
    }

    @Given("user provides endPoint as {string}.")
    public void setEndPoint(String endPoint){
        this.endPoint = endPoint;
    }

    @When("user hits the service with request body:")
    public void hitService(DocString docString){
        setup();
        client.doRequest(endPoint,method,docString.getContent());
    }

    @Then("response status code will be {string}.")
    public void checkStatus(String status){
        client.getResponse().then().statusCode(Integer.parseInt(status));
    }

    @Then("response body is according to the schema")
    public void checkSchema(DocString docString){
        client.getResponse().then().assertThat().body(matchesJsonSchema(docString.getContent()));
    }

    @Then("response body is according to the schema file {string}")
    public void checkSchema(String file){
        client.getResponse().then().assertThat().body(matchesJsonSchema(new File(file)));
    }

    @Then("response contains following values :")
    public void assertionValues(DataTable table){
        Map<String,String> map = table.asMap(String.class,String.class);
        map.forEach((k,v)->{
            if(v.equals("null")){
                client.getResponse().then().assertThat().body(k,isEmptyOrNullString());
            }else {
                client.getResponse().then().assertThat().body(k, equalTo(v));
            }
        });
    }

    @Then("response contains {string} as {int}.")
    public void assertIntValue(String path,int value){
        client.getResponse().then().body(path,equalTo(value));
    }

    @Then("user stores new user id.")
    public void storeUserId(){
        userid = client.getResponse().jsonPath().getInt("data.id");
    }

    @When("user creates a new user.")
    public void createUser(){
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = name+ RandomStringUtils.randomNumeric(5) +"@test.com";
        String gender = "Male";
        String status = "Active";
        current_user.put("name",name);
        current_user.put("gender",gender);
        current_user.put("email",email);
        current_user.put("status",status);
        JSONObject object = new JSONObject(current_user);
        setup();
        client.doRequest("/public-api/users","POST",object.toString());
    }

    @When("user deletes newly created user.")
    public void deleteCurrentUser(){
        setup();
        client.doRequest("/public-api/users/"+userid,"DELETE",null);
    }

    @When("user fetches user details of newly created.")
    public void getCurrentUser(){
        setup();
        client.doRequest("/public-api/users/"+userid,"GET",null);
    }



}
