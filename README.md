# Content Generator App

Welcome to the Content Generator App!
This Spring Boot application provides a modern web interface for generating a consolidated text file from the contents of a local directory.
You can feed this generated consolidated text file with a proper PROMPT to any LLM for preparing documentation, analyzing codebases, or code-review, this tool streamlines the process through an intuitive UI.

- Tip: See a README generator PROMPT [here](HELP.md)

## Features

- Single-page web interface with modern design
- Real-time file processing with progress indication
- Support for directory traversal and content aggregation
- Configurable file exclusion patterns
- Copy and download functionality for generated content
- Development and production deployment profiles

## Project Requirements

- Java 21 (GraalVM)
- Maven 3.8.7+
- SDKMAN for development environment setup
- Modern web browser with JavaScript enabled

## Core Dependencies

- Spring Boot 3.4.2
- JTE Template Engine 3.1.12
- HTMX 1.9.10
- Tailwind CSS
- Spring Framework 6.2.2

## Getting Started

### Environment Setup

#### The project uses SDKMAN for managing Java and Maven versions. To install SDKMAN refer: [sdkman.io](https://sdkman.io/install)

Initialize your development environment using SDKMAN CLI:

```bash
sdk env install
sdk env
```

### Configuration

The application supports both development and production profiles. Key configuration properties:

```yaml
# Development (application.yaml)
server:
  port: 8081
app:
  content:
    output-directory: "/path/to/local/output/directory"
    excluded-patterns: ".git/**, .gitignore, .gitattributes, .env, *.env, .DS_Store, **/.DS_Store, .mvn/**, mvnw, mvnw.cmd, .vscode/**, .idea/**, .classpath, .project, .settings, *.class, **/*.class, target/**, **/target/**"
```

- Tip: Keep all file/folder list from your project's `.gitignore` file here in `excluded-patterns`

```yaml
# Production (application-prod.yaml)
gg:
  jte:
    usePrecompiledTemplates: true
    developmentMode: false
app:
  content:
    output-directory: "/path/to/output/directory"
```

## Running the Application

### Development Mode

```bash
make run-dev
```

This command starts the application with hot-reload enabled for template changes.

### Production Mode

```bash
make run
```

This uses pre-compiled templates and production configurations.

### Build and Test

```bash
# Clean build artifacts
make clean

# Run tests
make test

# Build application
make build
```

## Usage Example

The service provides a web interface accessible at `http://localhost:8081`. Here's how to use it:

1. Navigate to the home page
2. Enter a local directory path in the input field
3. Click "Generate Content"
4. View, copy, or download the generated output

## Architecture Highlights

The application follows a clean architecture with clear separation of concerns:

- **Controller Layer**: Handles HTTP requests and route mapping
- **Service Layer**: Contains business logic and file processing
- **Template Layer**: JTE templates for dynamic HTML generation
- **Configuration**: Profile-based settings for development and production

## Contributing

Feel free to contribute to this project! Areas for potential enhancement include:

- Additional file format support
- Custom output formatting options
- Cloud storage integration
- Advanced file filtering capabilities

## Conclusion

This Content Generator app offers a robust solution for file content aggregation with a modern web interface. Its flexible configuration and straightforward architecture make it both powerful and maintainable. Try it out and let us know how it enhances your development workflow!

For questions or issues, please open a GitHub issue or submit a pull request.

Happy coding! ✌️

---

### reference: [repo-content-generator](https://github.com/danvega/repo-content-generator)
