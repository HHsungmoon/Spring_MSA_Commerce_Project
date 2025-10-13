package com.commerce.monolithic.autoresponse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.commerce.monolithic.autoresponse.error.GlobalExceptionHandler;

@Configuration
@ComponentScan(basePackageClasses = GlobalExceptionHandler.class)
// 라이브러리 패키지 범위만 스캔해서 Advice 등록 (자동설정 파일 없이 명시 활성화)
public class AutoResponseConfiguration {
}
