# Project Structure Plan: org.eclipse.jdtls.featureext

## Overview
This plan outlines the creation of a multi-module Maven project structure similar to [eclipse-lsp4mp/lsp4mp](https://github.com/eclipse-lsp4mp/lsp4mp/tree/master/microprofile.jdt), designed for extending JDT Language Server functionality.

## Reference Architecture
Based on the lsp4mp project structure:
- **Parent POM**: Aggregates all modules and defines common configuration
- **Core Module**: Contains the main implementation code
- **Test Module**: Contains test cases and test utilities
- **Target Platform (TP)**: Defines Eclipse dependencies and target platform
- **Site Module**: Creates Eclipse update site for distribution

## Project Structure

```
org.eclipse.jdtls.featureext/
├── pom.xml (parent)
├── README.md
├── org.eclipse.jdtls.featureext.core/
│   ├── pom.xml
│   ├── META-INF/
│   │   └── MANIFEST.MF
│   ├── plugin.xml
│   ├── build.properties
│   └── src/
│       └── main/
│           └── java/
│               └── org/eclipse/jdtls/featureext/core/
├── org.eclipse.jdtls.featureext.test/
│   ├── pom.xml
│   ├── META-INF/
│   │   └── MANIFEST.MF
│   ├── build.properties
│   └── src/
│       └── test/
│           └── java/
│               └── org/eclipse/jdtls/featureext/test/
├── org.eclipse.jdtls.featureext.tp/
│   ├── pom.xml
│   └── org.eclipse.jdtls.featureext.target
└── org.eclipse.jdtls.featureext.site/
    ├── pom.xml
    ├── category.xml
    └── site.xml
```

## Module Descriptions

### 1. Parent Module (org.eclipse.jdtls.featureext)
**Purpose**: Root aggregator project that manages all child modules

**Key Components**:
- Parent `pom.xml` with:
  - Project metadata (groupId, artifactId, version)
  - Module declarations
  - Common properties (Java version, Tycho version, etc.)
  - Plugin management (Tycho plugins)
  - Dependency management
  - Build configuration

**Packaging**: `pom`

### 2. Core Module (org.eclipse.jdtls.featureext.core)
**Purpose**: Main implementation module containing base classes and utilities for extending JDT.LS

**Key Components**:
- `META-INF/MANIFEST.MF`: OSGi bundle manifest
  - Bundle-SymbolicName
  - Bundle-Version
  - Bundle-Vendor
  - Required bundles/dependencies
  - Exported packages
- `plugin.xml`: Eclipse plugin extensions (if needed)
- `build.properties`: Build configuration
  - Source folders
  - Binary includes
- `src/main/java/`: Java source code
- `pom.xml`: Module-specific Maven configuration

**Packaging**: `eclipse-plugin`

### 3. Test Module (org.eclipse.jdtls.featureext.test)
**Purpose**: Contains unit and integration tests

**Key Components**:
- `META-INF/MANIFEST.MF`: Test bundle manifest
  - Requires core module
  - Test framework dependencies (JUnit, etc.)
- `build.properties`: Test build configuration
- `src/test/java/`: Test source code
- `pom.xml`: Test module configuration

**Packaging**: `eclipse-test-plugin`

### 4. Target Platform Module (org.eclipse.jdtls.featureext.tp)
**Purpose**: Defines Eclipse platform dependencies and versions

**Key Components**:
- `org.eclipse.jdtls.featureext.target`: Target definition file
  - Eclipse platform version
  - Required Eclipse plugins
  - P2 repository locations
- `pom.xml`: Target platform configuration

**Packaging**: `eclipse-target-definition`

### 5. Site Module (org.eclipse.jdtls.featureext.site)
**Purpose**: Creates Eclipse update site for distribution

**Key Components**:
- `category.xml`: Feature categorization
- `site.xml`: Update site definition (optional)
- `pom.xml`: Site generation configuration

**Packaging**: `eclipse-repository`

## Technology Stack

### Build System
- **Maven 3.x**: Project management and build automation
- **Tycho 2.x+**: Maven plugins for Eclipse/OSGi development
  - `tycho-maven-plugin`: Core Tycho functionality
  - `tycho-compiler-plugin`: Java compilation
  - `tycho-packaging-plugin`: OSGi bundle packaging
  - `tycho-surefire-plugin`: Test execution
  - `target-platform-configuration`: Target platform resolution

### Key Tycho Plugins Configuration
```xml
<plugin>
  <groupId>org.eclipse.tycho</groupId>
  <artifactId>tycho-maven-plugin</artifactId>
  <version>${tycho.version}</version>
  <extensions>true</extensions>
</plugin>
```

### Java Version
- **Java 21** (LTS version with modern language features)

## Build Configuration Details

### Parent POM Key Sections

1. **Properties**:
   - `tycho.version`: Tycho version (e.g., 4.0.0)
   - `maven.compiler.source`: 21
   - `maven.compiler.target`: 21
   - `maven.compiler.release`: 21
   - `project.build.sourceEncoding`: UTF-8

2. **Modules**:
   ```xml
   <modules>
     <module>org.eclipse.jdtls.featureext.core</module>
     <module>org.eclipse.jdtls.featureext.test</module>
     <module>org.eclipse.jdtls.featureext.tp</module>
     <module>org.eclipse.jdtls.featureext.site</module>
   </modules>
   ```

3. **Plugin Management**: Define versions for all Tycho plugins

4. **Repositories**: Eclipse P2 repositories for dependencies

### Core Module MANIFEST.MF Structure
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
```

## Implementation Steps

### Phase 1: Parent Project Setup
1. Create parent `pom.xml` with basic project structure
2. Define common properties and plugin management
3. Configure Tycho plugins
4. Set up Eclipse P2 repositories

### Phase 2: Core Module
1. Create module directory structure
2. Create `pom.xml` with `eclipse-plugin` packaging
3. Create `META-INF/MANIFEST.MF` with bundle metadata
4. Create `build.properties`
5. Set up basic Java package structure
6. Create placeholder classes (if needed)

### Phase 3: Test Module
1. Create test module directory structure
2. Create test `pom.xml` with `eclipse-test-plugin` packaging
3. Create test `META-INF/MANIFEST.MF`
4. Create `build.properties` for tests
5. Set up test package structure

### Phase 4: Target Platform
1. Create TP module directory
2. Create `pom.xml` with `eclipse-target-definition` packaging
3. Create `.target` file with Eclipse platform dependencies
4. Define required Eclipse plugins and versions

### Phase 5: Site Module
1. Create site module directory
2. Create `pom.xml` with `eclipse-repository` packaging
3. Create `category.xml` for feature categorization
4. Configure update site generation

### Phase 6: Documentation & Validation
1. Update main README.md with project structure
2. Add build instructions
3. Test Maven build: `mvn clean verify`
4. Validate all modules build successfully

## Key Considerations

### OSGi Bundle Requirements
- Each module must be a valid OSGi bundle
- Proper bundle symbolic names
- Correct version qualifiers
- Appropriate bundle dependencies

### Tycho-Specific Requirements
- Use `eclipse-plugin` packaging for plugins
- Use `eclipse-test-plugin` for test bundles
- Use `eclipse-target-definition` for target platform
- Use `eclipse-repository` for update site

### Version Management
- Use `.qualifier` suffix for development versions
- Align versions across all modules
- Use Maven properties for version management

### Dependency Resolution
- Tycho resolves dependencies from target platform
- P2 repositories must be properly configured
- Bundle dependencies declared in MANIFEST.MF

## Build Commands

```bash
# Clean build
mvn clean verify

# Build without tests
mvn clean verify -DskipTests

# Build specific module
mvn clean verify -pl org.eclipse.jdtls.featureext.core

# Install to local repository
mvn clean install
```

## Success Criteria

- [ ] All modules build successfully with `mvn clean verify`
- [ ] Proper OSGi bundle structure in each module
- [ ] Target platform resolves all dependencies
- [ ] Update site generates correctly
- [ ] Tests execute successfully
- [ ] Project follows Eclipse/Tycho best practices

## Next Steps After Implementation

1. Add actual implementation code to core module
2. Write comprehensive tests
3. Configure CI/CD pipeline
4. Set up code quality tools (Checkstyle, SpotBugs, etc.)
5. Create contribution guidelines
6. Set up GitHub Actions or Jenkins for automated builds

## References

- [Eclipse Tycho Documentation](https://www.eclipse.org/tycho/)
- [LSP4MP Project Structure](https://github.com/eclipse-lsp4mp/lsp4mp/tree/master/microprofile.jdt)
- [Eclipse Plugin Development](https://www.eclipse.org/pde/)
- [OSGi Bundle Development](https://www.osgi.org/)