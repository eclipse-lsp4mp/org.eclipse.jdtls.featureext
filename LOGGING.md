# Logging Guide for JDT.LS Feature Extension

This project uses `java.util.logging` with a custom wrapper that supports **property-based configuration** for both console and file logging.

## Quick Start

### 1. Basic Usage

```java
import org.eclipse.jdtls.featureext.core.logging.FeatureExtLogger;
import java.util.logging.Logger;

public class MyClass {
    // Get logger for your class - initialization is automatic
    private static final Logger LOGGER = FeatureExtLogger.getLogger(MyClass.class);
    
    public void myMethod() {
        LOGGER.info("This is an info message");
        LOGGER.warning("This is a warning");
        LOGGER.severe("This is an error");
        
        try {
            // your code
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred", e);
        }
    }
}
```

### 2. Convenience Methods (No Logger Instance Needed)

```java
import org.eclipse.jdtls.featureext.core.logging.FeatureExtLogger;

FeatureExtLogger.info("Quick info message");
FeatureExtLogger.warning("Quick warning");
FeatureExtLogger.severe("Quick error");
FeatureExtLogger.severe("Error with exception", exception);
```

## Configuration

### Property-Based Configuration

Logging is controlled entirely through `logging.properties` file. **No code changes needed!**

#### Configuration Locations (Priority Order)

1. **User Override**: `~/.jdtls-featureext/logging.properties` (highest priority)
2. **Bundled Default**: `src/main/resources/logging.properties` (in classpath)

#### Configuration Properties

```properties
# Console Logging
featureext.logging.console.enabled=true
featureext.logging.console.level=INFO

# File Logging
featureext.logging.file.enabled=true
featureext.logging.file.level=ALL
featureext.logging.file.size=10485760    # 10MB
featureext.logging.file.count=5          # 5 rotating files
```

### Log Levels

From most to least verbose:
- `ALL` - Log everything
- `FINEST` - Highly detailed tracing
- `FINER` - Detailed tracing
- `FINE` - Tracing information
- `CONFIG` - Configuration messages
- `INFO` - Informational messages (default for console)
- `WARNING` - Warning messages
- `SEVERE` - Error messages
- `OFF` - Disable logging

## Common Configurations

### 1. Production Mode (Default)
Minimal console output, detailed file logging:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=INFO
featureext.logging.file.enabled=true
featureext.logging.file.level=ALL
```

**Result:**
- Console: Shows INFO, WARNING, SEVERE
- File: Logs everything (ALL levels)

### 2. Debug Mode
Verbose logging to both console and file:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=FINE
featureext.logging.file.enabled=true
featureext.logging.file.level=ALL
```

**Result:**
- Console: Shows FINE and above (debug info visible)
- File: Logs everything

### 3. Console Only
No file logging:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=INFO
featureext.logging.file.enabled=false
```

**Result:**
- Console: Shows INFO and above
- File: Disabled

### 4. File Only
No console output (useful for background services):

```properties
featureext.logging.console.enabled=false
featureext.logging.file.enabled=true
featureext.logging.file.level=ALL
```

**Result:**
- Console: Disabled
- File: Logs everything

### 5. Errors Only
Only log errors to console, everything to file:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=SEVERE
featureext.logging.file.enabled=true
featureext.logging.file.level=INFO
```

**Result:**
- Console: Shows only SEVERE errors
- File: Logs INFO and above

### 6. Disable All Logging

```properties
featureext.logging.console.enabled=false
featureext.logging.file.enabled=false
```

**Result:**
- No logging at all

## Log File Location

Logs are written to: `~/.jdtls-featureext/logs/jdtls-featureext.log`

With rotation:
- `jdtls-featureext.log` (current)
- `jdtls-featureext.log.1` (previous)
- `jdtls-featureext.log.2`
- ... up to configured count

## Changing Configuration at Runtime

While property files are the recommended approach, you can also change settings programmatically:

```java
import org.eclipse.jdtls.featureext.core.logging.FeatureExtLogger;
import java.util.logging.Level;

// Enable/disable console
FeatureExtLogger.setConsoleEnabled(true);

// Enable/disable file
FeatureExtLogger.setFileEnabled(true);

// Change levels
FeatureExtLogger.setConsoleLevel(Level.FINE);
FeatureExtLogger.setFileLevel(Level.ALL);

// Configure everything at once
FeatureExtLogger.configure(
    true,        // console enabled
    true,        // file enabled
    Level.INFO,  // console level
    Level.ALL    // file level
);

// Check current configuration
String config = FeatureExtLogger.getConfiguration();
System.out.println(config);
```

## Best Practices

### 1. Use Appropriate Log Levels

```java
LOGGER.severe("Critical error that requires immediate attention");
LOGGER.warning("Something unexpected but recoverable");
LOGGER.info("Important business logic events");
LOGGER.config("Configuration information");
LOGGER.fine("General debugging information");
LOGGER.finer("Detailed debugging information");
LOGGER.finest("Highly detailed tracing");
```

### 2. Include Context in Messages

```java
// Good
LOGGER.info("Processing feature: " + featureName);

// Better
LOGGER.info(String.format("Processing feature '%s' for user '%s'", 
    featureName, userName));
```

### 3. Log Exceptions Properly

```java
try {
    // code
} catch (Exception e) {
    // Include both message and exception
    LOGGER.log(Level.SEVERE, "Failed to process feature: " + featureName, e);
}
```

### 4. Use Class-Specific Loggers

```java
// Each class should have its own logger
private static final Logger LOGGER = FeatureExtLogger.getLogger(MyClass.class);
```

## Troubleshooting

### Logs Not Appearing?

1. Check if logging is enabled in `logging.properties`
2. Check log level settings
3. Verify log file location: `~/.jdtls-featureext/logs/`
4. Check file permissions

### Override Default Configuration

Create `~/.jdtls-featureext/logging.properties` with your settings:

```properties
# Your custom configuration
featureext.logging.console.enabled=true
featureext.logging.console.level=FINE
featureext.logging.file.enabled=true
featureext.logging.file.level=ALL
```

This will override the bundled configuration.

## Example: Complete Usage

```java
package org.eclipse.jdtls.featureext.myfeature;

import org.eclipse.jdtls.featureext.core.logging.FeatureExtLogger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyFeature {
    private static final Logger LOGGER = FeatureExtLogger.getLogger(MyFeature.class);
    
    public void processRequest(String request) {
        LOGGER.info("Processing request: " + request);
        
        try {
            // Detailed debug info (only logged if level is FINE or below)
            LOGGER.fine("Validating request format");
            validateRequest(request);
            
            LOGGER.fine("Executing business logic");
            executeLogic(request);
            
            LOGGER.info("Request processed successfully");
            
        } catch (ValidationException e) {
            LOGGER.warning("Invalid request: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to process request", e);
            throw new RuntimeException("Processing failed", e);
        }
    }
    
    private void validateRequest(String request) {
        LOGGER.finer("Validation started for: " + request);
        // validation logic
        LOGGER.finer("Validation completed");
    }
    
    private void executeLogic(String request) {
        LOGGER.finer("Executing logic for: " + request);
        // business logic
        LOGGER.finer("Logic execution completed");
    }
}
```

## Summary

✅ **Property-based configuration** - No code changes needed  
✅ **Console and file logging** - Enable/disable independently  
✅ **Configurable log levels** - Control verbosity  
✅ **Rotating log files** - Automatic log rotation  
✅ **User overrides** - Place config in `~/.jdtls-featureext/`  
✅ **Automatic initialization** - Just use `getLogger()`  
✅ **Thread-safe** - Safe for concurrent use