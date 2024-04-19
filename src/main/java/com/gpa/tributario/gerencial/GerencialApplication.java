package com.gpa.tributario.gerencial;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.TimeZone;
import java.util.concurrent.Executor;


@SpringBootApplication
@EnableAsync
@SecurityScheme(name = "bearer-schema", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class GerencialApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerencialApplication.class, args);
	}

	@Bean(name = "relatorioasync")
	Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(10000);
		executor.setThreadNamePrefix("relatorioasync-");
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor(executor);
	}

	@Bean
	TimeZone timeZone() {
		return TimeZone.getTimeZone("America/Sao_Paulo");
	}

}
