package com.junl.wpwx.common.Bean2Map;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Bean2MapField {
    public String value() default "";
}
