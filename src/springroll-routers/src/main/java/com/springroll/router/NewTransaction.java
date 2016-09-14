package com.springroll.router;

import java.lang.annotation.*;

/**
 * Created by anishjoseph on 10/09/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NewTransaction
{
    boolean value() default true;

}
