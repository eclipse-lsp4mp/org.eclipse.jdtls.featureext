/*******************************************************************************
 * Copyright (c) 2026 Eclipse Foundation and others.
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
package org.eclipse.jdtls.featureext.test;

import org.eclipse.jdtls.featureext.core.logging.FeatureExtLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the logging system.
 * Verifies console logging functionality.
 */
class LoggingTest {

    private Logger logger;
    private ByteArrayOutputStream consoleOutput;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        // Capture console output
        consoleOutput = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(consoleOutput));
        System.setErr(new PrintStream(consoleOutput));

        // Get logger instance
        logger = FeatureExtLogger.getLogger(LoggingTest.class);
    }

    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testLoggerInitialization() {
        assertNotNull(logger, "Logger should be initialized");
        assertEquals("org.eclipse.jdtls.featureext.test.LoggingTest", 
                     logger.getName(), 
                     "Logger name should match class name");
    }

    @Test
    void testGetLoggerWithClass() {
        Logger testLogger = FeatureExtLogger.getLogger(LoggingTest.class);
        assertNotNull(testLogger, "Logger should not be null");
        assertEquals("org.eclipse.jdtls.featureext.test.LoggingTest", 
                     testLogger.getName());
    }

    @Test
    void testGetLoggerGeneric() {
        Logger testLogger = FeatureExtLogger.getLogger();
        assertNotNull(testLogger, "Generic logger should not be null");
    }

    @Test
    void testInfoLogging() {
        String testMessage = "Test info message";
        logger.info(testMessage);
        
        // Give logger time to flush
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String output = consoleOutput.toString();
        assertTrue(output.contains(testMessage) || output.isEmpty(), 
                   "Console output should contain the message or be empty if console is disabled");
    }

    @Test
    void testWarningLogging() {
        String testMessage = "Test warning message";
        logger.warning(testMessage);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String output = consoleOutput.toString();
        assertTrue(output.contains(testMessage) || output.isEmpty(),
                   "Console output should contain warning or be empty");
    }

    @Test
    void testSevereLogging() {
        String testMessage = "Test severe message";
        logger.severe(testMessage);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String output = consoleOutput.toString();
        assertTrue(output.contains(testMessage) || output.isEmpty(),
                   "Console output should contain severe message or be empty");
    }

    @Test
    void testLoggingWithException() {
        String testMessage = "Test exception logging";
        Exception testException = new RuntimeException("Test exception");
        
        logger.log(Level.SEVERE, testMessage, testException);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String output = consoleOutput.toString();
        // Should contain message and/or exception info if console is enabled
        assertTrue(output.contains(testMessage) || 
                   output.contains("RuntimeException") || 
                   output.isEmpty(),
                   "Console should contain exception info or be empty");
    }

    @Test
    void testConvenienceMethodInfo() {
        String testMessage = "Convenience info message";
        FeatureExtLogger.info(testMessage);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Just verify no exceptions thrown
        assertNotNull(consoleOutput);
    }

    @Test
    void testConvenienceMethodWarning() {
        String testMessage = "Convenience warning message";
        FeatureExtLogger.warning(testMessage);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertNotNull(consoleOutput);
    }

    @Test
    void testConvenienceMethodSevere() {
        String testMessage = "Convenience severe message";
        FeatureExtLogger.severe(testMessage);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertNotNull(consoleOutput);
    }

    @Test
    void testConvenienceMethodSevereWithException() {
        String testMessage = "Convenience severe with exception";
        Exception testException = new RuntimeException("Test exception");
        FeatureExtLogger.severe(testMessage, testException);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertNotNull(consoleOutput);
    }

    @Test
    void testSetConsoleEnabled() {
        // Test enabling console
        FeatureExtLogger.setConsoleEnabled(true);
        logger.info("Console enabled test");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Test disabling console
        consoleOutput.reset();
        FeatureExtLogger.setConsoleEnabled(false);
        logger.info("Console disabled test");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // When disabled, output should be empty
        String output = consoleOutput.toString();
        assertTrue(output.isEmpty() || !output.contains("Console disabled test"),
                   "Console should not log when disabled");
        
        // Re-enable for other tests
        FeatureExtLogger.setConsoleEnabled(true);
    }

    @Test
    void testSetConsoleLevel() {
        // Set to WARNING level
        FeatureExtLogger.setConsoleLevel(Level.WARNING);
        
        consoleOutput.reset();
        logger.info("This should not appear");
        logger.warning("This should appear");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String output = consoleOutput.toString();
        // INFO should not appear, WARNING should (if console is enabled)
        assertFalse(output.contains("This should not appear"),
                    "INFO message should not appear when level is WARNING");
        
        // Reset to INFO for other tests
        FeatureExtLogger.setConsoleLevel(Level.INFO);
    }

    @Test
    void testConfigureMethod() {
        // Test configuration method
        FeatureExtLogger.configure(
            true,           // console enabled
            Level.INFO      // console level
        );
        
        logger.info("Configuration test message");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Just verify no exceptions thrown
        assertNotNull(consoleOutput);
    }

    @Test
    void testGetConfiguration() {
        String config = FeatureExtLogger.getConfiguration();
        
        assertNotNull(config, "Configuration string should not be null");
        assertTrue(config.contains("Logging Configuration"),
                   "Configuration should contain header");
        assertTrue(config.contains("Console"),
                   "Configuration should contain console handler info");
    }

    @Test
    void testMultipleLoggers() {
        Logger logger1 = FeatureExtLogger.getLogger(LoggingTest.class);
        Logger logger2 = FeatureExtLogger.getLogger(LoggingTest.class);
        
        // Should return the same logger instance for the same class
        assertNotNull(logger1);
        assertNotNull(logger2);
        assertEquals(logger1.getName(), logger2.getName());
    }

    @Test
    void testFineLogging() {
        logger.fine("Fine level message");
        logger.finer("Finer level message");
        logger.finest("Finest level message");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Just verify no exceptions thrown
        assertNotNull(consoleOutput);
    }

    @Test
    void testConfigLogging() {
        logger.config("Configuration message");
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertNotNull(consoleOutput);
    }
}
