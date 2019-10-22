package ml.corp.security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

	@GetMapping("example")
	public ResponseEntity<Map<String, String>> example(){
		Map<String, String> response = new HashMap<String, String>();
		response.put("say", "all it´s ok.");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
