package io.akikr.app.content.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app.content")
public class AppConfig
{
	@NotNull(message = "excludedPatterns cannot be NULL")
	private final List<String> excludedPatterns;
	@NotEmpty(message = "includePattern cannot be NULL or EMPTY")
	private final List<String> includePattern;
	@NotNull(message = "outputDirectory cannot be NULL")
	private final String outputDirectory;

	public AppConfig(
			List<String> excludedPatterns,
			List<String> includePattern,
			String outputDirectory)
	{
		this.excludedPatterns = excludedPatterns;
		this.includePattern = includePattern;
		this.outputDirectory = outputDirectory;
	}

	public List<String> getExcludedPatterns()
	{
		return excludedPatterns;
	}

	public List<String> getIncludePattern()
	{
		return includePattern;
	}

	public String getOutputDirectory()
	{
		return outputDirectory;
	}
}
