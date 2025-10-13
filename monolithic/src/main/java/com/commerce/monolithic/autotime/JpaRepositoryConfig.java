package com.commerce.monolithic.autotime;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
	basePackages = "com.commerce.monolithic",
	repositoryBaseClass = SoftDeleteRepositoryImpl.class
)
public class JpaRepositoryConfig {
}
