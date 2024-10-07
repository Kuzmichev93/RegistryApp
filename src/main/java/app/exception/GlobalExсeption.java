package app.exception;

import jakarta.servlet.ServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExсeption {
    @ExceptionHandler(СustomException.class)
    public ResponseEntity<String> customexception(СustomException e, ServletResponse servletResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error",e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(String.valueOf(jsonObject),headers, e.getStatus());
}}
