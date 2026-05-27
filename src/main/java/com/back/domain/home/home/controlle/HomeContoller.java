package com.back.domain.home.home.controlle;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
public class HomeContoller {
	@SneakyThrows
	@GetMapping
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
}
