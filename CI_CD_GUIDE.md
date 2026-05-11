# CI/CD Configuration Guide

This guide provides GitHub Actions workflow configuration similar to the lsp4mp project for building the org.eclipse.jdtls.featureext project.

## GitHub Actions Workflow

Based on the lsp4mp project structure, create the following file:

**File: `.github/workflows/build.yml`**

```yaml
name: Build

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  # Build and test the JDT component
  featureext-jdt-build:
    runs-on: ubuntu-latest
    steps:
      - name: Check out repository code
        uses: actions/checkout@v4
      
      - name: Set up Eclipse Temurin JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      
      - name: Build and test featureext JDT component
        working-directory: ./
        run: ./mvnw -B -U clean verify
```

## Alternative: Simplified Single Job Workflow

If you prefer a simpler single-job workflow:

**File: `.github/workflows/build.yml`**

```yaml
name: Build

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    name: Build and Test
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: 'maven'
      
      - name: Build with Maven
        run: mvn clean verify -B
      
      - name: Upload build artifacts
        if: success()
        uses: actions/upload-artifact@v4
        with:
          name: update-site
          path: org.eclipse.jdtls.featureext.site/target/repository/
          retention-days: 7
      
      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: |
            **/target/surefire-reports/
            **/target/failsafe-reports/
          retention-days: 7
```

## Maven Wrapper Setup

The lsp4mp project uses Maven Wrapper (`mvnw`). To add it to your project:

```bash
# Generate Maven Wrapper
mvn wrapper:wrapper

# This creates:
# - mvnw (Unix/Linux/Mac script)
# - mvnw.cmd (Windows script)
# - .mvn/wrapper/maven-wrapper.jar
# - .mvn/wrapper/maven-wrapper.properties
```

Add to `.gitignore`:
```
# Maven
target/
!.mvn/wrapper/maven-wrapper.jar
```

## Directory Structure for CI/CD

```
org.eclipse.jdtls.featureext/
├── .github/
│   └── workflows/
│       └── build.yml
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
└── [module directories]
```

## Build Commands

### Using Maven Wrapper (Recommended)
```bash
# Unix/Linux/Mac
./mvnw clean verify

# Windows
mvnw.cmd clean verify
```

### Using System Maven
```bash
mvn clean verify
```

## Key Features from lsp4mp Workflow

1. **Java 21**: Uses Eclipse Temurin distribution
2. **Maven Wrapper**: Uses `./mvnw` for consistent Maven version
3. **Working Directory**: Specifies working directory for builds
4. **Clean Verify**: Uses `clean verify` for full build with tests
5. **Batch Mode**: Uses `-B` flag for non-interactive builds
6. **Update Dependencies**: Uses `-U` flag to force dependency updates

## Additional Workflow Examples

### Release Workflow

**File: `.github/workflows/release.yml`**

```yaml
name: Release

on:
  push:
    tags:
      - 'v*.*.*'

jobs:
  release:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: 'maven'
      
      - name: Build release
        run: ./mvnw clean verify -B
      
      - name: Create release archive
        run: |
          cd org.eclipse.jdtls.featureext.site/target/repository
          zip -r ../../../jdtls-featureext-${GITHUB_REF#refs/tags/}.zip .
      
      - name: Create GitHub Release
        uses: softprops/action-gh-release@v1
        with:
          files: jdtls-featureext-*.zip
          generate_release_notes: true
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### Pull Request Workflow

**File: `.github/workflows/pr.yml`**

```yaml
name: Pull Request

on:
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: 'maven'
      
      - name: Build and test
        run: ./mvnw clean verify -B
      
      - name: Comment PR
        if: always()
        uses: actions/github-script@v7
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '✅ Build completed successfully!'
            })
```

## Local Development Scripts

### build.sh (Unix/Linux/Mac)

```bash
#!/bin/bash
set -e

echo "Building org.eclipse.jdtls.featureext..."

# Check if Maven Wrapper exists
if [ -f "./mvnw" ]; then
    MVN_CMD="./mvnw"
else
    MVN_CMD="mvn"
fi

# Clean and build
$MVN_CMD clean verify -B

echo "Build complete!"
echo "Update site: org.eclipse.jdtls.featureext.site/target/repository"
```

### build.bat (Windows)

```batch
@echo off
setlocal

echo Building org.eclipse.jdtls.featureext...

REM Check if Maven Wrapper exists
if exist "mvnw.cmd" (
    set MVN_CMD=mvnw.cmd
) else (
    set MVN_CMD=mvn
)

REM Clean and build
call %MVN_CMD% clean verify -B

if %errorlevel% equ 0 (
    echo Build complete!
    echo Update site: org.eclipse.jdtls.featureext.site\target\repository
) else (
    echo Build failed!
    exit /b 1
)
```

## Setup Instructions

1. **Create workflow directory**:
   ```bash
   mkdir -p .github/workflows
   ```

2. **Add workflow file**:
   - Copy the workflow YAML content above to `.github/workflows/build.yml`

3. **Add Maven Wrapper** (optional but recommended):
   ```bash
   mvn wrapper:wrapper
   chmod +x mvnw
   ```

4. **Commit and push**:
   ```bash
   git add .github/workflows/build.yml
   git add mvnw mvnw.cmd .mvn/
   git commit -m "Add GitHub Actions workflow"
   git push
   ```

5. **Verify workflow**:
   - Go to your repository on GitHub
   - Click on "Actions" tab
   - You should see the workflow running

## Troubleshooting

### Common Issues

1. **Maven Wrapper not executable**:
   ```bash
   chmod +x mvnw
   ```

2. **Java version mismatch**:
   - Ensure workflow uses Java 21
   - Check local Java version: `java -version`

3. **Build fails on CI but works locally**:
   - Check for uncommitted files
   - Verify all dependencies are in pom.xml
   - Check for environment-specific configurations

4. **Artifact upload fails**:
   - Verify path exists: `org.eclipse.jdtls.featureext.site/target/repository/`
   - Check build completed successfully

## Best Practices

1. **Use Maven Wrapper**: Ensures consistent Maven version across environments
2. **Cache Dependencies**: Use `cache: 'maven'` in setup-java action
3. **Fail Fast**: Use `-B` (batch mode) and proper error handling
4. **Artifact Retention**: Set appropriate retention days for artifacts
5. **Branch Protection**: Require CI checks to pass before merging
6. **Status Badges**: Add build status badge to README.md

## Status Badge

Add this to your README.md:

```markdown
![Build Status](https://github.com/Joseph-Bineesh/org.eclipse.jdtls.featureext/workflows/Build/badge.svg)
```

Replace `YOUR_USERNAME` with your GitHub username or organization name.