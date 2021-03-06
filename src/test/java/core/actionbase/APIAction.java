package core.actionbase;

import core.extentreport.TestReportManager;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.output.WriterOutputStream;
import core.extentreport.ReportLogLevel;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType;

public class APIAction {
    protected StringWriter actionInfoWriter;
    protected StringWriter requestWriter;
    protected static PrintStream requestCapture;
    protected StringWriter responseWriter;
    protected static PrintStream responseCapture;
    private ContentType contentType;
    private NumberReturnType numberReturnType;

    public static APIAction instance;
    public static APIAction Instance(){
        if(instance == null) {
            instance = new APIAction();
        }
        return instance;
    }

    public APIAction() {
        initLogWriter();
        contentType = ContentType.JSON;
        numberReturnType = NumberReturnType.BIG_DECIMAL;
    }

    private void initLogWriter() {
        actionInfoWriter = new StringWriter();
        requestWriter = new StringWriter();
        requestCapture = new PrintStream(new WriterOutputStream(requestWriter, "UTF-8"), true);

        responseWriter = new StringWriter();
        responseCapture = new PrintStream(new WriterOutputStream(responseWriter, "UTF-8"), true);
    }

    //endregion
    public Response runGetMethod(String url) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: GET<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("get", specifyRequest(), url);

        return response;
    }

    public Response runPostMethod(String url, String requestBody) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: POST<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre>", ReportLogLevel.LOG_LVL_4);
        RequestSpecification reqSpec = specifyRequest().body(requestBody);

        Response response = executeMethod("post", specifyRequest(), url);

        return response;
    }

    public Response executeMethod(String method, RequestSpecification reqSpec, String url) {
        Response response = null;
        switch (method) {
            case "get":
                response = reqSpec.when().get(url);
                break;
            case "post":
                response = reqSpec.when().post(url);
                break;
            case "put":
                response = reqSpec.when().put(url);
                break;
            case "delete":
                response = reqSpec.when().delete(url);
                break;
        }
        return response;
    }

    public RequestSpecification specifyRequest(Map<String, Object> queryParams) {
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            specifyRequest().queryParam((String) entry.getKey(), entry.getValue());
        }
        return specifyRequest();
    }

    public RequestSpecification specifyRequest() {
        RequestSpecification reqSpec = given()
                .contentType(contentType)
                .filter(new RequestLoggingFilter(requestCapture))
                .filter(new ResponseLoggingFilter(responseCapture))
                .log().ifValidationFails()
                .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(numberReturnType)));;

        return reqSpec;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public NumberReturnType getNumberReturnType() {
        return numberReturnType;
    }

    public void setNumberReturnType(NumberReturnType numberReturnType) {
        this.numberReturnType = numberReturnType;
    }

    //endregion

}
