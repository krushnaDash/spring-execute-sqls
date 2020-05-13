package com.example.springexecutesqls.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
/**
 * 
 * @author k0d03gd
 *
 */
public class SpringJdbcConfig {

	@Value("${db_host}")
	String dbHost;
	@Value("${db_user}")
	String dbUserName;
	@Value("${db_passowrd}")
	String password;

	@Bean
	public DataSource dB2DataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
		dataSource.setUrl(dbHost);
		dataSource.setUsername(dbUserName);
		dataSource.setPassword(password);

		return dataSource;
	}
}