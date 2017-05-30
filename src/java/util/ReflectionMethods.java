package util;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionMethods {

    public static Method getMethod(Class originClass, String methodName) {
        Class clazz;
        try {
            clazz = Class.forName(originClass.getCanonicalName());
            Method[] methods = clazz.getDeclaredMethods();
            for (Method currentMethod : methods) {
                if (currentMethod.getName().equals(methodName)) {
                    return currentMethod;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReflectionMethods.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ReflectionMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   
}
