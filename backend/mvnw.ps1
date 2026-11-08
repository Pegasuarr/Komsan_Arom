# ----------------------------------------------------------------------------
# Maven Wrapper PowerShell Script for Windows
# Handles spaces in paths and locates Java automatically
# ----------------------------------------------------------------------------

$ErrorActionPreference = "Stop"

# 1. Locate Java
$javaCmd = $null
if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $javaCmd = "$env:JAVA_HOME\bin\java.exe"
} elseif (Get-Command java -ErrorAction SilentlyContinue) {
    $javaCmd = (Get-Command java).Source
} else {
    # Check common locations (Android Studio JBR, IntelliJ JBR)
    $candidates = @(
        "C:\Program Files\Android\Android Studio\jbr\bin\java.exe",
        "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.6.2\jbr\bin\java.exe",
        "C:\Program Files\Java\jdk-21\bin\java.exe",
        "C:\Program Files\Eclipse Adoptium\jdk-21*\bin\java.exe"
    )
    foreach ($cand in $candidates) {
        $resolved = Resolve-Path $cand -ErrorAction SilentlyContinue
        if ($resolved) {
            $javaCmd = $resolved.Path
            break
        }
    }
}

if (-not $javaCmd) {
    Write-Error "Could not locate java.exe. Please install JDK 21 or set JAVA_HOME."
    exit 1
}

$projectDir = $PSScriptRoot
$wrapperJar = Join-Path $projectDir ".mvn\wrapper\maven-wrapper.jar"

if (-not (Test-Path $wrapperJar)) {
    Write-Host "Downloading Maven wrapper..."
    $wrapperUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
    New-Item -ItemType Directory -Force -Path (Join-Path $projectDir ".mvn\wrapper") | Out-Null
    Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperJar
}

# Ensure target classes files are never marked read-only on Windows
if (Test-Path "$projectDir\target\classes") {
    Get-ChildItem -Path "$projectDir\target\classes" -Recurse -File -ErrorAction SilentlyContinue | ForEach-Object { $_.IsReadOnly = $false }
}

& $javaCmd "-Dmaven.multiModuleProjectDirectory=$projectDir" -cp $wrapperJar org.apache.maven.wrapper.MavenWrapperMain @args
