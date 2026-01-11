@echo off
REM UrlShortener CLI for Windows

REM Find project root relative to this script
set SCRIPT_DIR=%~dp0
set JAR_FILE=%SCRIPT_DIR%build\libs\url-shortener-1.0.0.jar

if not exist "%JAR_FILE%" (
    echo Error: JAR file not found at %JAR_FILE%
    echo Please run: gradlew.bat build
    exit /b 1
)

java -jar "%JAR_FILE%" %*