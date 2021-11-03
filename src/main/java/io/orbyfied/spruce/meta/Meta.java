package io.orbyfied.spruce.meta;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Meta {
    public static boolean isReturnNullable(Method method) {
        return method.isAnnotationPresent(Nullable.class);
    }

    public static boolean isParameterNullable(Method method, int parameter) {
        if (parameter > method.getParameterCount() - 1)
            throw new IllegalArgumentException("Parameter index out of range.");
        return method.getAnnotatedParameterTypes()[parameter].isAnnotationPresent(Nullable.class);
    }

    public static boolean isParameterNullable(Method method, String parameter) {
        for (Parameter p : method.getParameters())
            if (p.getName().equals(parameter))
                return p.isAnnotationPresent(Nullable.class);
        throw new IllegalArgumentException("Parameter does not exist. (" + parameter + ")");
    }

    public static boolean isNullable(AnnotatedElement e) {
        return e.isAnnotationPresent(Nullable.class);
    }
}
