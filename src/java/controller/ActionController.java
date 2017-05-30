/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.ReflectionMethods;

/**
 *
 * @author luisr
 */
public class ActionController implements controller.action.Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
        ReflectionMethods.getMethod(this.getClass(), request.getParameter("method")).invoke(this, request, response);
    }
    
}
