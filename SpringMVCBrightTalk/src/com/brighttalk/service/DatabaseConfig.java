package com.brighttalk.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

	@Bean
	public DriverManagerDataSource getDataSource() {
		DriverManagerDataSource driverDataSource = new DriverManagerDataSource();
		driverDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverDataSource.setUrl("jdbc:mysql://localhost:3306/brighttalk");
		driverDataSource.setUsername("root");
		driverDataSource.setPassword("Indian@123");
		return driverDataSource;

	}
}
