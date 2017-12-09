package agh.iosr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VideoConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoConverterApplication.class, args);
	}
}
