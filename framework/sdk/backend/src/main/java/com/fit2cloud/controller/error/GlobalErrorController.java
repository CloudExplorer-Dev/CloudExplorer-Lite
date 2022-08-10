package com.fit2cloud.controller.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class GlobalErrorController extends AbstractErrorController {


    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public Object handleError(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));

        //针对非api接口，要把url路径交给前端去路由
        if (HttpStatus.NOT_FOUND.equals(HttpStatus.valueOf((Integer) errorPropertiesMap.get("status"))) && !StringUtils.contains((String) errorPropertiesMap.get("path"), "/api/") || StringUtils.equals((String) errorPropertiesMap.get("path"), "/login")) {
            response.setStatus(HttpStatus.OK.value());
            return "index.html";
        }

        return new ResponseEntity<>(errorPropertiesMap, status);
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        //options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        //options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        return options;
    }

}
