package controller.action;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionController {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException;
}
