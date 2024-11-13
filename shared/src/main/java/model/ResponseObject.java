package model;

public class ResponseObject {
    public Integer responseCode;
    public String responseBody;

    public ResponseObject(Integer code, String body){
        responseCode = code;
        responseBody = body;
    }
}
