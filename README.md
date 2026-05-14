# org.eclipse.jdtls.featureext

Base classes and utilities to extend Eclipse JDT Language Server (jdt.ls)

## Project Structure

This is a multi-module Maven project built with Eclipse Tycho:

```
org.eclipse.jdtls.featureext/
├── pom.xml                                    # Parent POM
├── org.eclipse.jdtls.featureext.core/         # Core implementation
├── org.eclipse.jdtls.featureext.test/         # Test suite
├── org.eclipse.jdtls.featureext.tp/           # Target platform definition
└── org.eclipse.jdtls.featureext.site/         # Eclipse update site
```

## Modules

### org.eclipse.jdtls.featureext.core
Core implementation module containing base classes and utilities for extending JDT.LS functionality.

### org.eclipse.jdtls.featureext.test
Test module with unit and integration tests for the core module.

### org.eclipse.jdtls.featureext.tp
Target platform definition specifying Eclipse and JDT.LS dependencies.

### org.eclipse.jdtls.featureext.site
Eclipse update site for distributing the extension as a P2 repository.

## Prerequisites

- **Java 21** or later
- **Maven 3.8+**
- **Git**

## Building

### Quick Build

```bash
mvn clean verify
```

### Build Without Tests

```bash
mvn clean verify -DskipTests
```

### Install to Local Repository

```bash
mvn clean install
```

## Development

### Using Maven Wrapper (Recommended)

Generate Maven Wrapper:
```bash
mvn wrapper:wrapper
```

Then use:
```bash
./mvnw clean verify          # Unix/Linux/Mac
mvnw.cmd clean verify        # Windows
```

### IDE Setup

#### Eclipse
1. Import as "Existing Maven Projects"
2. Select the root directory
3. Eclipse will configure all modules automatically

#### IntelliJ IDEA
1. Open the root `pom.xml` as a project
2. IntelliJ will import all modules automatically

#### VS Code
1. Open the root directory
2. Maven projects will be detected automatically

## Project Configuration

- **Java Version**: 21
- **Eclipse Platform**: 2025-09
- **JDT.LS Version**: 1.58.0
- **Tycho Version**: 5.0.2
- **Packaging**: OSGi bundles with Eclipse Tycho


## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass: `mvn clean verify`
6. Submit a pull request

## License

This project follows Eclipse Foundation licensing standards.

