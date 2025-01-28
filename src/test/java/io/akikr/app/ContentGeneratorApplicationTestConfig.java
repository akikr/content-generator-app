package io.akikr.app;

import gg.jte.TemplateEngine;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ContentGeneratorApplicationTestConfig
{
	@Bean
	@Primary
	public TemplateEngine templateEngine()
	{
		return mock(TemplateEngine.class);
	}
}
