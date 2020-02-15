package io.hanyoung.gulmatebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

@SpringBootApplication
public class GulmateBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulmateBackendApplication.class, args);
	}

}
