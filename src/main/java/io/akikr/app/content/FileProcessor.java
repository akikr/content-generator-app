package io.akikr.app.content;

import io.akikr.app.content.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileProcessor
{
	private static final Logger log = LoggerFactory.getLogger(FileProcessor.class);

	private final List<PathMatcher> excludedPathMatchers;
	private final List<PathMatcher> includePathMatchers;
	private final String outputDirectory;

	public FileProcessor(
			AppConfig config,
			List<PathMatcher> includePathMatchers)
	{
		if (config.getExcludedPatterns().isEmpty())
			log.warn("excludedPathMatchers list is EMPTY, including all files !!");
		//@formatter:off
        this.includePathMatchers = config.getIncludePattern().stream()
				.filter(Objects::nonNull)
				.map(pattern -> FileSystems.getDefault()
						.getPathMatcher("glob:" + normalizePattern(pattern)))
				.toList();
        this.excludedPathMatchers = config.getExcludedPatterns().stream()
				.filter(Objects::nonNull)
				.map(pattern -> FileSystems.getDefault()
						.getPathMatcher("glob:" + normalizePattern(pattern)))
				.toList();
		//@formatter:on
		this.outputDirectory = config.getOutputDirectory();
	}

	public String process(String directoryPath, String outFileName) throws Exception
	{
		log.info("Processing for source directory:[{}] and output directory:[{}]", directoryPath, outputDirectory);

		Path path = Paths.get(outputDirectory);
		if (!Files.isDirectory(path.normalize().toAbsolutePath()))
		{
			throw new IllegalArgumentException(String.format("Invalid Output Directory path:[%s]", outputDirectory));
		}

		Path sourceDirectory = Paths.get(directoryPath).normalize().toAbsolutePath();
		if (!Files.exists(sourceDirectory) || !Files.isDirectory(sourceDirectory))
		{
			throw new IllegalArgumentException(String.format("Invalid Source Directory path:[%s]", directoryPath));
		}

		log.info("Reading from sourceDirectory:[{}] files started", directoryPath);
		Path outputFile;
		try (Stream<Path> srcPath = Files.walk(sourceDirectory))
		{
			//@formatter:off
			String allFileContents = srcPath.filter(Files::isRegularFile)
					.filter(this::includedFiles)
					.filter(this::notExcludeFiles)
					.map(this::readFileContent)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.joining());
			//@formatter:on
			log.info("Reading from sourceDirectory:[{}] files completed", directoryPath);

			log.info("Writing to output directory:[{}] with output file-name:[{}] started", outputDirectory, outFileName);
			Files.createDirectories(path);
			outputFile = path.resolve(outFileName);
			Files.write(outputFile, allFileContents.getBytes());
			log.info("Writing to output file:[{}] completed", outputFile);
			log.info("Processed source directory contents to:[{}]", outputFile.toAbsolutePath());
		}
		return new String(Files.readAllBytes(Objects.requireNonNull(outputFile, "Output file is NULL")));
	}

	private boolean includedFiles(Path filePath)
	{
		return includePathMatchers.stream().anyMatch(pathMatcher -> pathMatcher.matches(Paths.get(normalizePath(filePath.getFileName()))));
	}

	private boolean notExcludeFiles(Path filePath)
	{
		return excludedPathMatchers.stream().noneMatch(
				pathMatcher -> pathMatcher.matches(Paths.get(normalizePath(filePath.getFileName()))));
	}

	private Optional<String> readFileContent(Path filePath)
	{
		try
		{
			//@formatter:off
			String fileContent = Files.readString(filePath);
			return Optional.of("File: " + filePath + "\n\n" + fileContent + "\n\n");
			//@formatter:on
		}
		catch (IOException e)
		{
			log.warn("Error reading file:[{}], due to: {}", filePath, e.getMessage());
			log.debug(e.getMessage(), e);
			return Optional.empty();
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
