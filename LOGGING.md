# Logging Guide for JDT.LS Feature Extension

This project uses `java.util.logging` with a custom wrapper that supports **property-based configuration** for console logging.

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
```

### Log Levels

From most to least verbose:
- `ALL` - Log everything
- `FINEST` - Highly detailed tracing
- `FINER` - Detailed tracing
- `FINE` - Tracing information
- `CONFIG` - Configuration messages
- `INFO` - Informational messages (default)
- `WARNING` - Warning messages
- `SEVERE` - Error messages
- `OFF` - Disable logging

## Common Configurations

### 1. Production Mode (Default)
Standard console output:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=INFO
```

**Result:**
- Console: Shows INFO, WARNING, SEVERE

### 2. Debug Mode
Verbose logging to console:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=FINE
```

**Result:**
- Console: Shows FINE and above (debug info visible)

### 3. Errors Only
Only log errors:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=SEVERE
```

**Result:**
- Console: Shows only SEVERE errors

### 4. Warnings and Errors
Production mode with minimal output:

```properties
featureext.logging.console.enabled=true
featureext.logging.console.level=WARNING
```

**Result:**
- Console: Shows WARNING and SEVERE only

### 5. Disable All Logging

```properties
featureext.logging.console.enabled=false
```

**Result:**
- No logging at all

## Changing Configuration at Runtime

While property files are the recommended approach, you can also change settings programmatically:

```java
import org.eclipse.jdtls.featureext.core.logging.FeatureExtLogger;
import java.util.logging.Level;

// Enable/disable console
FeatureExtLogger.setConsoleEnabled(true);

// Change level
FeatureExtLogger.setConsoleLevel(Level.FINE);

// Configure everything at once
FeatureExtLogger.configure(
    true,        // console enabled
    Level.INFO   // console level
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
3. Verify console output is not being redirected

### Override Default Configuration

Create `~/.jdtls-featureext/logging.properties` with your settings:

```properties
# Your custom configuration
featureext.logging.console.enabled=true
featureext.logging.console.level=FINE
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
✅ **Console logging** - Enable/disable with configurable levels  
✅ **Configurable log levels** - Control verbosity  
✅ **User overrides** - Place config in `~/.jdtls-featureext/`  
✅ **Automatic initialization** - Just use `getLogger()`  
✅ **Thread-safe** - Safe for concurrent use  
✅ **Lightweight** - No file I/O overhead