# Quick Start Guide

This guide will help you get started with the org.eclipse.jdtls.featureext project.

## Prerequisites Check

Before you begin, ensure you have:

```bash
# Check Java version (must be 21+)
java -version

# Check Maven version (must be 3.8+)
mvn -version

# Check Git
git --version
```

## Initial Setup

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd org.eclipse.jdtls.featureext
```

### 2. First Build

Run your first build to verify everything is set up correctly:

```bash
mvn clean verify
```

This will:
- Download all dependencies
- Compile all modules
- Run tests
- Create the update site

**Expected output**: Build should complete successfully with "BUILD SUCCESS"

### 3. Verify the Build

Check that the update site was created:

```bash
ls -la org.eclipse.jdtls.featureext.site/target/repository/
```

You should see:
- `artifacts.jar`
- `content.jar`
- `plugins/` directory

## Project Structure Overview

```
org.eclipse.jdtls.featureext/
├── pom.xml                                    # Parent POM - start here
├── org.eclipse.jdtls.featureext.core/         # Your code goes here
│   ├── src/main/java/                         # Java source files
│   ├── META-INF/MANIFEST.MF                   # OSGi bundle config
│   └── plugin.xml                             # Eclipse extensions
├── org.eclipse.jdtls.featureext.test/         # Your tests go here
│   └── src/test/java/                         # Test files
├── org.eclipse.jdtls.featureext.tp/           # Dependencies
│   └── *.target                               # Target platform
└── org.eclipse.jdtls.featureext.site/         # Distribution
    └── category.xml                           # Update site config
```

## Common Tasks

### Build Commands

```bash
# Full build with tests
mvn clean verify

# Build without tests (faster)
mvn clean verify -DskipTests

# Build specific module
mvn clean verify -pl org.eclipse.jdtls.featureext.core

# Install to local Maven repository
mvn clean install

# Clean all build artifacts
mvn clean
```

### Development Workflow

1. **Add new code** to `org.eclipse.jdtls.featureext.core/src/main/java/`
2. **Add tests** to `org.eclipse.jdtls.featureext.test/src/test/java/`
3. **Build and test**: `mvn clean verify`
4. **Commit changes**: `git add . && git commit -m "Your message"`

### Adding Dependencies

#### To add Eclipse/OSGi dependencies:

1. Edit `org.eclipse.jdtls.featureext.tp/org.eclipse.jdtls.featureext.target`
2. Add the dependency unit and repository
3. Edit `org.eclipse.jdtls.featureext.core/META-INF/MANIFEST.MF`
4. Add to `Require-Bundle:` section

Example:
```
Require-Bundle: org.eclipse.core.runtime,
 org.eclipse.jdt.core,
 org.eclipse.jdt.ls.core,
 your.new.dependency
```

### Exporting Packages

To make your code available to other plugins:

Edit `org.eclipse.jdtls.featureext.core/META-INF/MANIFEST.MF`:

```
Export-Package: org.eclipse.jdtls.featureext.core,
 org.eclipse.jdtls.featureext.core.utils
```

## IDE Setup

### Eclipse IDE

1. **Import Project**:
   - File → Import → Maven → Existing Maven Projects
   - Select the root directory
   - Click Finish

2. **Set Target Platform**:
   - Window → Preferences → Plug-in Development → Target Platform
   - Select "org.eclipse.jdtls.featureext"
   - Click Apply

3. **Run Tests**:
   - Right-click on test class → Run As → JUnit Plug-in Test

### IntelliJ IDEA

1. **Open Project**:
   - File → Open
   - Select the root `pom.xml`
   - Click OK

2. **Build**:
   - Maven tool window → Lifecycle → verify

### VS Code

1. **Open Folder**:
   - File → Open Folder
   - Select the root directory

2. **Install Extensions**:
   - Extension Pack for Java
   - Maven for Java

3. **Build**:
   - Terminal → `mvn clean verify`

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test

```bash
mvn test -Dtest=CoreModuleTest
```

### Run Tests in IDE

- **Eclipse**: Right-click test → Run As → JUnit Plug-in Test
- **IntelliJ**: Click green arrow next to test method
- **VS Code**: Click "Run Test" above test method

## Troubleshooting

### Build Fails with "Cannot resolve target definition"

**Solution**: The target platform repositories might be temporarily unavailable. Try:
```bash
mvn clean verify -U
```

### Tests Fail

**Solution**: Ensure you're using Java 21:
```bash
java -version
# Should show version 21.x.x
```

### "Package does not exist" errors

**Solution**: Check that the package is exported in MANIFEST.MF:
```
Export-Package: org.eclipse.jdtls.featureext.core
```

### Maven build is slow

**Solution**: Use Maven Wrapper for consistent performance:
```bash
mvn wrapper:wrapper
./mvnw clean verify  # Unix/Linux/Mac
mvnw.cmd clean verify  # Windows
```


## Useful Commands Reference

```bash
# Build
mvn clean verify                    # Full build
mvn clean verify -DskipTests        # Skip tests
mvn clean install                   # Install locally

# Test
mvn test                            # Run tests
mvn test -Dtest=TestClass          # Run specific test

# Clean
mvn clean                           # Remove build artifacts

# Dependency Management
mvn dependency:tree                 # Show dependencies
mvn versions:display-dependency-updates  # Check for updates

# Help
mvn help:describe -Dplugin=tycho   # Tycho plugin help
```

## Success Indicators

You're ready to develop when:

- ✅ `mvn clean verify` completes successfully
- ✅ Update site is generated in `org.eclipse.jdtls.featureext.site/target/repository/`
- ✅ Tests pass
- ✅ IDE recognizes all modules
- ✅ No compilation errors

Happy coding! 🚀