package controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionController {

    public void processRequest(HttpServletRequest request, HttpServletResponse response);
}
