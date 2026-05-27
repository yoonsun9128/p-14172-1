package com.back.domain.home.home.controlle;

import lombok.SneakyThrows;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@RestController
public class HomeContoller {
	@SneakyThrows
	@GetMapping(produces = TEXT_HTML_VALUE)
	public String main() {
		InetAddress localHost = InetAddress.getLocalHost();

		return """
				<h1>API 서버</h1>
				                <p>Host Name: %s</p>
				                <p>Host Address: %s</p>
				                <div>
				                    <a href="/swagger-ui/index.html">API 문서로 이동</a>
				                </div>
				""".formatted(localHost.getHostName(), localHost.getHostAddress());
	}

	@Bean
	public GroupedOpenApi groupApiV1() {
		return GroupedOpenApi.builder()
				.group("apiV1")
				.pathsToMatch("/api/v1/**")
				.build();
	}

	@Bean
	public GroupedOpenApi groupController() {
		return GroupedOpenApi.builder()
				.group("home")
				.pathsToExclude("/api/**")
				.build();
	}
}
