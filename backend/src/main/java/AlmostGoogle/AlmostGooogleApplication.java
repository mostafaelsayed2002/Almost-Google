package AlmostGoogle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Stack;

@SpringBootApplication
@RestController
public class AlmostGooogleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlmostGooogleApplication.class, args);
	}

	@GetMapping("/")
	String test(){
		return "test!";
	}



}
