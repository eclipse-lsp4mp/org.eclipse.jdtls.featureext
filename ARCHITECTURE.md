# Architecture Overview: org.eclipse.jdtls.featureext

## Project Structure Diagram

```mermaid
graph TD
    A[org.eclipse.jdtls.featureext<br/>Parent POM] --> B[org.eclipse.jdtls.featureext.core<br/>Core Module]
    A --> C[org.eclipse.jdtls.featureext.test<br/>Test Module]
    A --> D[org.eclipse.jdtls.featureext.tp<br/>Target Platform]
    A --> E[org.eclipse.jdtls.featureext.site<br/>Update Site]
    
    C -.depends on.-> B
    B -.uses.-> D
    C -.uses.-> D
    E -.packages.-> B
    
    style A fill:#e1f5ff
    style B fill:#c8e6c9
    style C fill:#fff9c4
    style D fill:#f8bbd0
    style E fill:#d1c4e9
```

## Module Dependencies

```mermaid
graph LR
    subgraph "Build Order"
        TP[Target Platform<br/>org.eclipse.jdtls.featureext.tp]
        CORE[Core Module<br/>org.eclipse.jdtls.featureext.core]
        TEST[Test Module<br/>org.eclipse.jdtls.featureext.test]
        SITE[Update Site<br/>org.eclipse.jdtls.featureext.site]
    end
    
    TP --> CORE
    TP --> TEST
    CORE --> TEST
    CORE --> SITE
    
    style TP fill:#f8bbd0
    style CORE fill:#c8e6c9
    style TEST fill:#fff9c4
    style SITE fill:#d1c4e9
```

## Component Relationships

```mermaid
classDiagram
    class ParentPOM {
        +groupId: org.eclipse.jdtls
        +artifactId: featureext-parent
        +packaging: pom
        +modules: List
        +properties: Map
        +pluginManagement: Configuration
    }
    
    class CoreModule {
        +packaging: eclipse-plugin
        +MANIFEST.MF
        +plugin.xml
        +build.properties
        +Java Source Code
    }
    
    class TestModule {
        +packaging: eclipse-test-plugin
        +MANIFEST.MF
        +build.properties
        +Test Source Code
    }
    
    class TargetPlatform {
        +packaging: eclipse-target-definition
        +target file
        +Eclipse Dependencies
        +P2 Repositories
    }
    
    class UpdateSite {
        +packaging: eclipse-repository
        +category.xml
        +site.xml
        +Generated P2 Repository
    }
    
    ParentPOM --> CoreModule : aggregates
    ParentPOM --> TestModule : aggregates
    ParentPOM --> TargetPlatform : aggregates
    ParentPOM --> UpdateSite : aggregates
    TestModule --> CoreModule : depends on
    CoreModule --> TargetPlatform : resolves from
    TestModule --> TargetPlatform : resolves from
    UpdateSite --> CoreModule : packages
```

## Build Flow

```mermaid
sequenceDiagram
    participant Maven
    participant Parent as Parent POM
    participant TP as Target Platform
    participant Core as Core Module
    participant Test as Test Module
    participant Site as Update Site
    
    Maven->>Parent: mvn clean verify
    Parent->>TP: Build target definition
    TP-->>Parent: Target platform ready
    
    Parent->>Core: Build core plugin
    Core->>TP: Resolve dependencies
    TP-->>Core: Dependencies resolved
    Core->>Core: Compile Java sources
    Core->>Core: Package OSGi bundle
    Core-->>Parent: Core bundle ready
    
    Parent->>Test: Build test plugin
    Test->>TP: Resolve dependencies
    TP-->>Test: Dependencies resolved
    Test->>Core: Require core bundle
    Core-->>Test: Core bundle available
    Test->>Test: Compile test sources
    Test->>Test: Execute tests
    Test-->>Parent: Tests passed
    
    Parent->>Site: Build update site
    Site->>Core: Package core bundle
    Core-->>Site: Bundle packaged
    Site->>Site: Generate P2 repository
    Site-->>Parent: Update site ready
    
    Parent-->>Maven: Build complete
```

## File Structure Details

### Parent Module
```
org.eclipse.jdtls.featureext/
├── pom.xml                    # Parent POM with module aggregation
├── README.md                  # Project documentation
├── PLAN.md                    # Implementation plan
├── ARCHITECTURE.md            # This file
└── .gitignore                 # Git ignore patterns
```

### Core Module Structure
```
org.eclipse.jdtls.featureext.core/
├── pom.xml                    # Module POM (eclipse-plugin)
├── META-INF/
│   └── MANIFEST.MF            # OSGi bundle manifest
├── plugin.xml                 # Eclipse plugin extensions
├── build.properties           # Build configuration
├── src/
│   └── main/
│       └── java/
│           └── org/eclipse/jdtls/featureext/core/
│               ├── package-info.java
│               └── [implementation classes]
└── target/                    # Build output (generated)
```

### Test Module Structure
```
org.eclipse.jdtls.featureext.test/
├── pom.xml                    # Test module POM (eclipse-test-plugin)
├── META-INF/
│   └── MANIFEST.MF            # Test bundle manifest
├── build.properties           # Test build configuration
├── src/
│   └── test/
│       └── java/
│           └── org/eclipse/jdtls/featureext/test/
│               └── [test classes]
└── target/                    # Test output (generated)
```

### Target Platform Structure
```
org.eclipse.jdtls.featureext.tp/
├── pom.xml                    # TP module POM (eclipse-target-definition)
└── org.eclipse.jdtls.featureext.target  # Target definition file
```

### Update Site Structure
```
org.eclipse.jdtls.featureext.site/
├── pom.xml                    # Site module POM (eclipse-repository)
├── category.xml               # Feature categorization
├── site.xml                   # Update site definition (optional)
└── target/
    └── repository/            # Generated P2 repository
        ├── artifacts.jar
        ├── content.jar
        ├── features/
        └── plugins/
```

## Key Technologies

### Maven Tycho
- **Purpose**: Build Eclipse plugins and OSGi bundles with Maven
- **Version**: 4.0.0+
- **Key Features**:
  - P2 repository resolution
  - OSGi bundle packaging
  - Eclipse plugin compilation
  - Target platform management

### OSGi
- **Purpose**: Modular Java platform
- **Key Concepts**:
  - Bundles: Modular units with metadata
  - Services: Dynamic service registry
  - Dependencies: Explicit bundle requirements
  - Versioning: Semantic versioning support

### Eclipse Plugin Development
- **Purpose**: Extend Eclipse IDE and tools
- **Key Components**:
  - Extension points: Define extensibility
  - Extensions: Implement extension points
  - Plugin manifest: Define plugin metadata
  - Build properties: Configure build process

## Integration Points

### JDT Language Server Integration
```mermaid
graph LR
    A[JDT.LS Core] --> B[Extension Point]
    B --> C[org.eclipse.jdtls.featureext.core]
    C --> D[Custom Features]
    
    style A fill:#e3f2fd
    style B fill:#fff3e0
    style C fill:#c8e6c9
    style D fill:#f3e5f5
```

### Dependency Resolution
```mermaid
graph TD
    A[Maven Build] --> B{Target Platform}
    B --> C[Eclipse Platform]
    B --> D[JDT Core]
    B --> E[JDT.LS Core]
    B --> F[Other Dependencies]
    
    C --> G[Core Module]
    D --> G
    E --> G
    F --> G
    
    style A fill:#e1f5ff
    style B fill:#f8bbd0
    style G fill:#c8e6c9
```

## Build Lifecycle

1. **Initialize**: Maven reads parent POM and discovers modules
2. **Validate**: Check project structure and configuration
3. **Compile**: 
   - Resolve target platform
   - Compile Java sources
   - Process resources
4. **Test**: Execute unit and integration tests
5. **Package**: Create OSGi bundles and P2 repository
6. **Verify**: Run additional checks and validations
7. **Install**: Install artifacts to local Maven repository
8. **Deploy**: Deploy to remote repository (if configured)

## Extension Patterns

### Adding New Features
1. Implement in core module
2. Export packages in MANIFEST.MF
3. Add tests in test module
4. Update documentation

### Adding Dependencies
1. Add to target platform definition
2. Declare in MANIFEST.MF Require-Bundle
3. Update version constraints
4. Rebuild project

### Creating Extensions
1. Define extension point (if needed)
2. Implement extension in plugin.xml
3. Provide implementation classes
4. Register services (if applicable)

## Best Practices

1. **Version Management**
   - Use semantic versioning
   - Keep versions synchronized across modules
   - Use `.qualifier` for development builds

2. **Dependency Management**
   - Minimize dependencies
   - Use version ranges carefully
   - Prefer API bundles over implementation

3. **Testing**
   - Write unit tests for all public APIs
   - Use integration tests for complex scenarios
   - Mock external dependencies

4. **Documentation**
   - Document public APIs with Javadoc
   - Maintain README files
   - Update architecture diagrams

5. **Build Configuration**
   - Keep build configuration DRY
   - Use parent POM for common settings
   - Configure CI/CD early