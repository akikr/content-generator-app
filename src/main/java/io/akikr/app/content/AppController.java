package io.akikr.app.content;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController
{
	private final AppService appService;

	public AppController(AppService appService)
	{
		this.appService = appService;
	}

	@GetMapping("/")
	public String index()
	{
		return "index";
	}

	@ResponseBody
	@PostMapping("/generate")
	public ResponseEntity<String> generate(@RequestParam(value = "path") String localPath)
	{
		return appService.generateContent(localPath);
	}
}
