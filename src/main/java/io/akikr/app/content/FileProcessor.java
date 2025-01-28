package io.akikr.app.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class FileProcessor
{
	private static final Logger log = LoggerFactory.getLogger(FileProcessor.class);

	@Value("${app.content.excluded-patterns:''}")
	private List<String> excludedPatterns;

	@Value("${app.content.output-directory}")
	private String outputDirectory;

	private Path sourceDirectory;

	public String process(String directoryPath, String outFileName) throws Exception
	{
		log.info("Processing for source directory:[{}] and output directory:[{}]", directoryPath, outputDirectory);

		if (!Files.isDirectory(Paths.get(outputDirectory).normalize().toAbsolutePath()))
		{
			throw new IllegalArgumentException(String.format("Invalid Output Directory path:[%s]", outputDirectory));
		}

		sourceDirectory = Paths.get(directoryPath).normalize().toAbsolutePath();
		if (!Files.exists(sourceDirectory) || !Files.isDirectory(sourceDirectory))
		{
			throw new IllegalArgumentException(String.format("Invalid Source Directory path:[%s]", directoryPath));
		}

		log.info("Reading from sourceDirectory:[{}] files started", directoryPath);
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<Path> srcPath = Files.walk(sourceDirectory))
		{
			//@formatter:off
			srcPath.filter(Files::isRegularFile)
					.filter(this::excludeFiles)
					.forEach(filePath -> readFileContent(filePath, contentBuilder));
			//@formatter:on
		}
		log.info("Reading from sourceDirectory:[{}] files completed", directoryPath);

		log.info("Writing to output directory:[{}] with output file-name:[{}] started", outputDirectory, outFileName);
		Path outputDirectoryPath = Paths.get(outputDirectory);
		Files.createDirectories(outputDirectoryPath);
		Path outputFile = outputDirectoryPath.resolve(outFileName);
		Files.write(outputFile, contentBuilder.toString().getBytes());
		log.info("Writing to output file:[{}] completed", outputFile);
		log.info("Processed source directory contents to:[{}]", outputFile.toAbsolutePath());

		return new String(Files.readAllBytes(outputFile));
	}

	private boolean excludeFiles(Path filePath)
	{
		List<PathMatcher> excludedPathMatchers = getExcludedPathMatchers();
		String relativePath = normalizePath(sourceDirectory.relativize(filePath));
		return excludedPathMatchers.stream().noneMatch(matcher -> matcher.matches(Paths.get(relativePath)));
	}

	private List<PathMatcher> getExcludedPathMatchers()
	{
		//@formatter:off
		return excludedPatterns.stream()
				.filter(Objects::nonNull)
				.map(pattern -> FileSystems.getDefault().getPathMatcher("glob:" + normalizePattern(pattern)))
				.toList();
		//@formatter:on
	}

	private void readFileContent(Path filePath, StringBuilder contentBuilder)
	{
		try
		{
			//@formatter:off
			String relativeFilePath = sourceDirectory.relativize(filePath).toString();
			String fileContent = Files.readString(filePath);
			contentBuilder.append("File: ").append(relativeFilePath).append("\n\n")
					.append(fileContent).append("\n\n");
			//@formatter:on
		}
		catch (IOException e)
		{
			log.warn("Error reading file:[{}], due to: {}", filePath, e.getMessage());
			log.debug(e.getMessage(), e);
		}
	}

	private String normalizePattern(String pattern)
	{
		return pattern.trim().replace('\\', '/');
	}

	private String normalizePath(Path path)
	{
		return path.toString().replace('\\', '/');
	}
}
