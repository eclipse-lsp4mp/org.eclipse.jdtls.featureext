# Implementation Guide: org.eclipse.jdtls.featureext

## Quick Start

This guide provides step-by-step instructions for implementing the multi-module Maven project structure for extending JDT Language Server.

## Prerequisites

- **Java 21** (JDK 21 or later)
- **Maven 3.8+**
- **Git** (for version control)
- **Eclipse IDE** (optional, for development)

## Project Overview

We're creating a 4-module Maven project:

1. **org.eclipse.jdtls.featureext.core** - Core implementation
2. **org.eclipse.jdtls.featureext.test** - Test suite
3. **org.eclipse.jdtls.featureext.tp** - Target platform definition
4. **org.eclipse.jdtls.featureext.site** - Eclipse update site

## Implementation Checklist

### Phase 1: Parent POM Setup ✓

- [ ] Create parent `pom.xml`
- [ ] Define project coordinates (groupId, artifactId, version)
- [ ] Configure Java 21 compiler settings
- [ ] Add Tycho plugin configuration
- [ ] Define module list
- [ ] Configure Eclipse P2 repositories

**Key Properties:**
```xml
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
<maven.compiler.release>21</maven.compiler.release>
<tycho.version>4.0.0</tycho.version>
```

### Phase 2: Core Module Setup

- [ ] Create directory: `org.eclipse.jdtls.featureext.core/`
- [ ] Create `pom.xml` with `eclipse-plugin` packaging
- [ ] Create `META-INF/MANIFEST.MF`
  - Set Bundle-SymbolicName
  - Set Bundle-Version
  - Set Bundle-RequiredExecutionEnvironment: JavaSE-21
  - Define Require-Bundle dependencies
  - Define Export-Package
- [ ] Create `build.properties`
- [ ] Create `plugin.xml` (if needed)
- [ ] Create Java package structure: `src/main/java/org/eclipse/jdtls/featureext/core/`
- [ ] Add `package-info.java` with package documentation

**Directory Structure:**
```
org.eclipse.jdtls.featureext.core/
├── pom.xml
├── META-INF/
│   └── MANIFEST.MF
├── plugin.xml
├── build.properties
└── src/main/java/org/eclipse/jdtls/featureext/core/
    └── package-info.java
```

### Phase 3: Test Module Setup

- [ ] Create directory: `org.eclipse.jdtls.featureext.test/`
- [ ] Create `pom.xml` with `eclipse-test-plugin` packaging
- [ ] Create `META-INF/MANIFEST.MF`
  - Require core module
  - Add JUnit dependencies
- [ ] Create `build.properties`
- [ ] Create test package: `src/test/java/org/eclipse/jdtls/featureext/test/`
- [ ] Add sample test class

**Directory Structure:**
```
org.eclipse.jdtls.featureext.test/
├── pom.xml
├── META-INF/
│   └── MANIFEST.MF
├── build.properties
└── src/test/java/org/eclipse/jdtls/featureext/test/
    └── CoreModuleTest.java
```

### Phase 4: Target Platform Setup

- [ ] Create directory: `org.eclipse.jdtls.featureext.tp/`
- [ ] Create `pom.xml` with `eclipse-target-definition` packaging
- [ ] Create `org.eclipse.jdtls.featureext.target` file
  - Define Eclipse platform version
  - Add JDT.LS dependencies
  - Add required Eclipse plugins
  - Configure P2 repository locations

**Directory Structure:**
```
org.eclipse.jdtls.featureext.tp/
├── pom.xml
└── org.eclipse.jdtls.featureext.target
```

### Phase 5: Update Site Setup

- [ ] Create directory: `org.eclipse.jdtls.featureext.site/`
- [ ] Create `pom.xml` with `eclipse-repository` packaging
- [ ] Create `category.xml`
  - Define feature categories
  - Reference core bundle
- [ ] Create `site.xml` (optional)

**Directory Structure:**
```
org.eclipse.jdtls.featureext.site/
├── pom.xml
├── category.xml
└── site.xml
```

### Phase 6: Build Configuration

- [ ] Configure Tycho compiler plugin
- [ ] Configure Tycho packaging plugin
- [ ] Configure Tycho surefire plugin (for tests)
- [ ] Configure target platform resolution
- [ ] Set up source/binary includes in build.properties

### Phase 7: Documentation

- [ ] Update main README.md
- [ ] Add build instructions
- [ ] Add contribution guidelines
- [ ] Document module purposes
- [ ] Add examples (if applicable)

### Phase 8: Validation

- [ ] Run `mvn clean verify`
- [ ] Verify all modules build successfully
- [ ] Check OSGi bundle manifests
- [ ] Verify target platform resolution
- [ ] Test update site generation
- [ ] Run tests successfully

## File Templates

### Parent pom.xml Template

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>org.eclipse.jdtls</groupId>
    <artifactId>featureext-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    
    <name>JDT.LS Feature Extension Parent</name>
    <description>Base classes and utilities to extend jdt.ls</description>
    
    <properties>
        <tycho.version>4.0.0</tycho.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <modules>
        <module>org.eclipse.jdtls.featureext.core</module>
        <module>org.eclipse.jdtls.featureext.test</module>
        <module>org.eclipse.jdtls.featureext.tp</module>
        <module>org.eclipse.jdtls.featureext.site</module>
    </modules>
    
    <!-- Build configuration continues... -->
</project>
```

### Core Module MANIFEST.MF Template

```
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: JDT.LS Feature Extension Core
Bundle-SymbolicName: org.eclipse.jdtls.featureext.core;singleton:=true
Bundle-Version: 1.0.0.qualifier
Bundle-Vendor: Eclipse Foundation
Bundle-RequiredExecutionEnvironment: JavaSE-21
Require-Bundle: org.eclipse.core.runtime,
 org.eclipse.jdt.core,
 org.eclipse.jdt.ls.core
Export-Package: org.eclipse.jdtls.featureext.core
Automatic-Module-Name: org.eclipse.jdtls.featureext.core
```

### Core Module build.properties Template

```properties
source.. = src/main/java/
output.. = target/classes/
bin.includes = META-INF/,\
               .,\
               plugin.xml
```

### Test Module MANIFEST.MF Template

```
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: JDT.LS Feature Extension Tests
Bundle-SymbolicName: org.eclipse.jdtls.featureext.test
Bundle-Version: 1.0.0.qualifier
Bundle-Vendor: Eclipse Foundation
Bundle-RequiredExecutionEnvironment: JavaSE-21
Require-Bundle: org.eclipse.jdtls.featureext.core,
 org.junit.jupiter.api,
 org.junit.jupiter.engine
Fragment-Host: org.eclipse.jdtls.featureext.core
Automatic-Module-Name: org.eclipse.jdtls.featureext.test
```

## Common Commands

### Build Commands

```bash
# Full build with tests
mvn clean verify

# Build without tests
mvn clean verify -DskipTests

# Build specific module
mvn clean verify -pl org.eclipse.jdtls.featureext.core

# Install to local repository
mvn clean install

# Clean all build artifacts
mvn clean
```

### Development Commands

```bash
# Check for dependency updates
mvn versions:display-dependency-updates

# Check for plugin updates
mvn versions:display-plugin-updates

# Generate Eclipse project files
mvn eclipse:eclipse

# Format code (if configured)
mvn formatter:format
```

## Troubleshooting

### Common Issues

1. **Target Platform Resolution Fails**
   - Check P2 repository URLs in target definition
   - Verify network connectivity
   - Check Eclipse platform version compatibility

2. **Compilation Errors**
   - Verify Java 21 is installed and configured
   - Check JAVA_HOME environment variable
   - Verify Maven compiler plugin configuration

3. **OSGi Bundle Issues**
   - Validate MANIFEST.MF syntax
   - Check bundle symbolic names are unique
   - Verify version qualifiers

4. **Test Failures**
   - Check test dependencies in MANIFEST.MF
   - Verify Fragment-Host configuration
   - Review test execution logs

### Debug Build

```bash
# Run with debug output
mvn clean verify -X

# Run with specific log level
mvn clean verify -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

## Next Steps After Implementation

1. **Add Implementation Code**
   - Create core classes in core module
   - Implement extension points
   - Add utility classes

2. **Write Tests**
   - Unit tests for all public APIs
   - Integration tests for complex scenarios
   - Mock external dependencies

3. **Configure CI/CD**
   - Set up GitHub Actions or Jenkins
   - Configure automated builds
   - Add code quality checks

4. **Documentation**
   - Write API documentation
   - Create usage examples
   - Add contribution guidelines

5. **Release Preparation**
   - Configure version management
   - Set up release process
   - Create release notes template

## Resources

- [Eclipse Tycho Documentation](https://www.eclipse.org/tycho/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [OSGi Specification](https://www.osgi.org/developer/specifications/)
- [Eclipse Plugin Development](https://www.eclipse.org/pde/)
- [JDT.LS Documentation](https://github.com/eclipse/eclipse.jdt.ls)

## Support

For questions or issues:
1. Check the documentation in PLAN.md and ARCHITECTURE.md
2. Review the lsp4mp reference project
3. Consult Eclipse Tycho documentation
4. Open an issue in the project repository

## License

This project structure follows Eclipse Foundation standards. Ensure proper license headers are added to all source files.