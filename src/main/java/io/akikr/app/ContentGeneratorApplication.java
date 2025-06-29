package io.akikr.app;

import io.akikr.app.content.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class ContentGeneratorApplication
{
	private static final Logger log = LoggerFactory.getLogger(ContentGeneratorApplication.class);

	public static void main(String[] args)
	{
		log.info("Application started executing 'main' method with arguments:{}", Arrays.asList(args));
		SpringApplication.run(ContentGeneratorApplication.class, args);
		log.info("Completed executing 'main' method");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info("Shutting down Application !!")));
	}
}
