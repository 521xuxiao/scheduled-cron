package com.scheduled.globalException;

import com.scheduled.returnData.ReturnData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

@ResponseBody
@ControllerAdvice
public class GlobalException {
//    private Logger logger= LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(RuntimeException.class)
    public ReturnData doHandleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return new ReturnData("系统异常");
    }
}
