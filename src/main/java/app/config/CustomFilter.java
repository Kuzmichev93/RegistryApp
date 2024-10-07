package app.config;

import app.exception.СustomException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;

public class CustomFilter implements Filter {
    private HashMap hashMap = new HashMap();

    private HandlerExceptionResolver resolver;
    public CustomFilter(HandlerExceptionResolver resolver){
        this.resolver = resolver;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            getPath((HttpServletRequest) servletRequest);

            filterChain.doFilter(servletRequest,servletResponse);
        } catch (СustomException e) {
            resolver.resolveException((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse,null,e);

        }

    }

    public void getPath(HttpServletRequest path) throws СustomException {
        hashMap.put("/api/department/all","GET");
        hashMap.put("/api/department/create","POST");
        hashMap.put("/api/department/createall","POST");
        hashMap.put("/api/department/search","POST");
        hashMap.put("/api/department/delete","DELETE");
        hashMap.put("/api/user/all","GET");
        hashMap.put("/api/user/create","POST");
        hashMap.put("/api/user/createall","POST");
        hashMap.put("/api/user/search","POST");
        hashMap.put("/api/user/delete","DELETE");
        if(hashMap.containsKey(path.getRequestURI())){
            String method = (String) hashMap.get(path.getRequestURI());
            check(path,method);

        }
        else{
            throw new СustomException(String.format("Данный путь %s не найден",path.getRequestURI()),HttpStatus.NOT_FOUND);
        }


    }

    public void check(HttpServletRequest request,String nameMethod) throws СustomException {

        if(!request.getMethod().equals(nameMethod)){

            throw new СustomException(String.format("Метод обрабатывает только %s запрос",nameMethod),HttpStatus.BAD_REQUEST);
        }
        if(!request.getMethod().equals("GET")){
            if(request.getHeader("Content-Length").equals("0")){
                throw new СustomException(String.format("Метод %s должен содержать тело запроса",nameMethod),HttpStatus.BAD_REQUEST);
            }
            if(!request.getHeader("Content-Type").equals("application/json")){
                throw new СustomException("Метод принимает данные только в формате json",HttpStatus.BAD_REQUEST);
            }
        }

    }


}
