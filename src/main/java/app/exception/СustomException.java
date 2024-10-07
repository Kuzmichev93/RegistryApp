package app.exception;

import org.springframework.http.HttpStatus;

public class СustomException extends Exception {
    private HttpStatus status;
    public СustomException(String arg, HttpStatus status){
        super(arg);
        this.status = status;
    }


    public HttpStatus setStatus(HttpStatus arg){
        status = arg;
        return status;
    }
    public HttpStatus getStatus(){
        return status;
    }
}
