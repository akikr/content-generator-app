package io.akikr.app.content;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class AppServiceImpl implements AppService
{
	private static final Logger log = LoggerFactory.getLogger(AppServiceImpl.class);

	public static final String FILE_NAME = "output.txt";

	private final FileProcessor fileProcessor;
	private final TemplateEngine templateEngine;

	public AppServiceImpl(
			FileProcessor fileProcessor,
			TemplateEngine templateEngine)
	{
		this.fileProcessor = fileProcessor;
		this.templateEngine = templateEngine;
	}

	@Override
	public ResponseEntity<String> generateContent(String localPath)
	{
		if (Objects.isNull(localPath) || localPath.isBlank())
		{
			return ResponseEntity.badRequest().body("Error: path is NULL or EMPTY");
		}

		try
		{
			String content = fileProcessor.process(localPath, FILE_NAME);
			StringOutput output = new StringOutput();
			templateEngine.render("result.jte", Map.of("content", content, "fileName", FILE_NAME), output);
			return ResponseEntity.ok().body(output.toString());
		}
		catch (Exception e)
		{
			String errorMsg = String.format("Error generating content, due to:%s", e.getMessage());
			log.error(errorMsg);
			log.debug(e.getMessage(), e);
			return ResponseEntity.internalServerError().body(errorMsg);
		}
	}
}
