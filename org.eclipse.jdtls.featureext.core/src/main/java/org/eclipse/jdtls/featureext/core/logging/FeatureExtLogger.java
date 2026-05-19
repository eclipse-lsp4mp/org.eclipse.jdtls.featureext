/*******************************************************************************
 * Copyright (c) 2024 Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdtls.featureext.core.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Centralized logging utility for JDT.LS Feature Extension.
 * Logs to console with configurable levels.
 */
public class FeatureExtLogger {

    private static final String LOGGER_NAME = "org.eclipse.jdtls.featureext";
    private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);
    private static boolean initialized = false;

    // Configuration properties
    private static final String PROP_CONSOLE_ENABLED = "featureext.logging.console.enabled";
    private static final String PROP_CONSOLE_LEVEL = "featureext.logging.console.level";

    /**
     * Initialize the logger with configuration from properties file.
     * Reads from logging.properties in classpath or uses defaults.
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        // Load configuration
        java.util.Properties props = loadConfiguration();

        // Remove default handlers
        Logger rootLogger = Logger.getLogger("");
        for (var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // Set logger level
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        // Console Handler (if enabled)
        boolean consoleEnabled = Boolean.parseBoolean(
            props.getProperty(PROP_CONSOLE_ENABLED, "true"));
        
        if (consoleEnabled) {
            Level consoleLevel = parseLevel(
                props.getProperty(PROP_CONSOLE_LEVEL, "INFO"));
            
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(consoleLevel);
            consoleHandler.setFormatter(new CustomFormatter());
            LOGGER.addHandler(consoleHandler);
        }

        initialized = true;
    }

    /**
     * Load configuration from properties file.
     * Looks for logging.properties in classpath, then user home, then uses defaults.
     */
    private static java.util.Properties loadConfiguration() {
        java.util.Properties props = new java.util.Properties();
        
        // Try to load from classpath
        try (var stream = FeatureExtLogger.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {
            if (stream != null) {
                props.load(stream);
                return props;
            }
        } catch (Exception e) {
            // Ignore, will use defaults
        }

        // Use defaults
        return props;
    }

    /**
     * Parse log level from string.
     */
    private static Level parseLevel(String levelStr) {
        try {
            return Level.parse(levelStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Level.INFO;
        }
    }

    /**
     * Get the logger instance.
     * Automatically initializes if not already done.
     */
    public static Logger getLogger() {
        if (!initialized) {
            initialize();
        }
        return LOGGER;
    }

    /**
     * Get a logger for a specific class.
     */
    public static Logger getLogger(Class<?> clazz) {
        if (!initialized) {
            initialize();
        }
        return Logger.getLogger(clazz.getName());
    }

    /**
     * Enable or disable console logging.
     */
    public static void setConsoleEnabled(boolean enabled) {
        for (var handler : LOGGER.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(enabled ? Level.INFO : Level.OFF);
            }
        }
    }

    /**
     * Set the console log level.
     */
    public static void setConsoleLevel(Level level) {
        for (var handler : LOGGER.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(level);
            }
        }
    }

    /**
     * Configure logging with specific options.
     * 
     * @param consoleEnabled Enable console logging
     * @param consoleLevel Console log level (if enabled)
     */
    public static void configure(boolean consoleEnabled, Level consoleLevel) {
        if (!initialized) {
            initialize();
        }

        for (var handler : LOGGER.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(consoleEnabled ? consoleLevel : Level.OFF);
            }
        }

        LOGGER.info(String.format("Logging configured - Console: %s (%s)",
                consoleEnabled, consoleLevel));
    }

    /**
     * Get current logging configuration as a string.
     */
    public static String getConfiguration() {
        StringBuilder config = new StringBuilder();
        config.append("Logging Configuration:\n");
        
        for (var handler : LOGGER.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                config.append("  Console: ")
                      .append(handler.getLevel() != Level.OFF ? "ENABLED" : "DISABLED")
                      .append(" (Level: ").append(handler.getLevel()).append(")\n");
            }
        }
        
        return config.toString();
    }

    /**
     * Custom formatter for log messages.
     */
    private static class CustomFormatter extends Formatter {
        private static final String FORMAT = "[%1$tF %1$tT] [%2$s] [%3$s] %4$s%5$s%n";

        @Override
        public String format(LogRecord record) {
            String source;
            if (record.getSourceClassName() != null) {
                source = record.getSourceClassName();
                if (record.getSourceMethodName() != null) {
                    source += "." + record.getSourceMethodName();
                }
            } else {
                source = record.getLoggerName();
            }

            String message = formatMessage(record);
            String throwable = "";
            if (record.getThrown() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n");
                sb.append(record.getThrown().toString());
                sb.append("\n");
                for (StackTraceElement element : record.getThrown().getStackTrace()) {
                    sb.append("\tat ").append(element.toString()).append("\n");
                }
                throwable = sb.toString();
            }

            return String.format(FORMAT,
                    record.getMillis(),
                    record.getLevel().getName(),
                    source,
                    message,
                    throwable);
        }
    }

    // Convenience methods for logging

    public static void severe(String message) {
        getLogger().severe(message);
    }

    public static void severe(String message, Throwable thrown) {
        getLogger().log(Level.SEVERE, message, thrown);
    }

    public static void warning(String message) {
        getLogger().warning(message);
    }

    public static void warning(String message, Throwable thrown) {
        getLogger().log(Level.WARNING, message, thrown);
    }

    public static void info(String message) {
        getLogger().info(message);
    }

    public static void info(String message, Throwable thrown) {
        getLogger().log(Level.INFO, message, thrown);
    }

    public static void config(String message) {
        getLogger().config(message);
    }

    public static void fine(String message) {
        getLogger().fine(message);
    }

    public static void finer(String message) {
        getLogger().finer(message);
    }

    public static void finest(String message) {
        getLogger().finest(message);
    }
}
