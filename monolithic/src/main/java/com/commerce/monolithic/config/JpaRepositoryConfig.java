package com.commerce.monolithic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.commerce.monolithic.autotime.SoftDeleteRepositoryImpl;

@Configuration
@EnableJpaRepositories(
	basePackages = "com.commerce.monolithic",
	repositoryBaseClass = SoftDeleteRepositoryImpl.class
)
public class JpaRepositoryConfig {
}
