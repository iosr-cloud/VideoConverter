package agh.iosr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VideoConverterApplication {

	@GetMapping
	public ResponseEntity<String> test(){
		return ResponseEntity.ok("test");
	}

	public static void main(String[] args) {
		SpringApplication.run(VideoConverterApplication.class, args);
	}
}
