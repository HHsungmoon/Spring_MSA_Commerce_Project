package com.commerce.monolithic.autoresponse.annotation;

import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

import com.commerce.monolithic.autoresponse.config.AutoResponseConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoResponseConfiguration.class)
public @interface EnableAutoResponse {
}
