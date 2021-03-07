package com.cocomore.framework;

import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class RestClient {

    private RequestSpecBuilder builder = new RequestSpecBuilder();
    private Response response = null;
    private Cookies cookies = null;

    public RestClient() {
    }

    public void setBaseUrl(String url) {
        builder.setBaseUri(url);
    }

    public void addParam(String key, String value) {
        builder.addParam(key, value);
    }

    public void addParam(String key,String... values){
        builder.addParam(key,values);
    }

    public void addParams(Map<String, String> values) {
        builder.addParams(values);
    }

    public void addHeaders(Map<String, String> headers) {
        builder.addHeaders(headers);
    }

    public void addCookies(Cookies cookies) {
        builder.addCookies(cookies);
    }

    public void doRequest(String endPoint) {
        doRequest(endPoint,"GET",null);
    }

    public void doRequest(String endPoint,String method,String body){
        if(getCookies() != null){
            builder.addCookies(cookies);
        }
        if(body != null){
            builder.setBody(body);
        }
        RequestSpecification request = given(builder.build());
        request.log().everything();
        response = request.request(method,endPoint);
        System.out.println("response time : "+response.timeIn(TimeUnit.MILLISECONDS)+" m sec");
        System.out.println(response.statusLine());
        response.prettyPrint();
        setCookies(response.getDetailedCookies());
        builder = new RequestSpecBuilder();
    }

    public void setCookies(Cookies cookies){
        this.cookies = cookies;
    }

    public Cookies getCookies(){
        return cookies;
    }

    public String getResponseAsString() {
        return response.getBody().asPrettyString();
    }

    public Response getResponse() {
        return response;
    }

    public void addBasicAuth(String username,String password){
        BasicAuthScheme scheme = new BasicAuthScheme();
        scheme.setUserName(username);
        scheme.setPassword(password);
        builder.setAuth(scheme);
    }

}
