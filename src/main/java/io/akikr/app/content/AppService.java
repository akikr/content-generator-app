package io.akikr.app.content;

import org.springframework.http.ResponseEntity;

public interface AppService
{
	ResponseEntity<String> generateContent(String localPath);
}
