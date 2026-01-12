# URL Shortener Service

Command-line URL shortener built with Java 17 and Gradle.

## How to Compile
```bash
./gradlew build
```

## How to Run

### Interactive Mode (Recommended)
```bash
./UrlShortener
```

Then enter commands:
```
> shorten https://www.example.com
Shortened URL: K7m2x9

> get K7m2x9
Original URL: https://www.example.com

> stats K7m2x9
Short Code: K7m2x9
Click Count: 1
Created At: 2025-01-11T19:43:59.676Z

> list
K7m2x9 -> https://www.example.com

> exit
```

### Single Command Mode
```bash
./UrlShortener shorten https://www.example.com
./UrlShortener get abc123
./UrlShortener list
./UrlShortener stats abc123
```

Or using JAR directly:
```bash
java -jar build/libs/url-shortener-1.0.0.jar shorten https://example.com
```

**Note:** Single-command mode runs separate processes. Data doesn't persist between commands (in-memory storage per spec). Use interactive mode for testing.

## Commands

### shorten
```bash
$ ./UrlShortener shorten https://www.example.com/very/long/path
Shortened URL: K7m2x9
```

### shorten --custom
```bash
$ ./UrlShortener shorten https://example.com --custom my-link
Shortened URL: my-link
```

### get
```bash
$ ./UrlShortener get K7m2x9
Original URL: https://www.example.com/very/long/path
```

### list
```bash
$ ./UrlShortener list
K7m2x9 -> https://www.example.com/very/long/path
my-link -> https://example.com
```

### stats
```bash
$ ./UrlShortener stats K7m2x9
Short Code: K7m2x9
Original URL: https://www.example.com/very/long/path
Click Count: 1
Created At: 2025-01-11T14:32:15.234Z
```

## Design Decisions

### Interactive Mode
Added to solve usability issue with in-memory storage. Each CLI command runs as a separate process, so data doesn't persist between single commands. Interactive mode keeps one process running for the session, allowing multiple operations on the same data while maintaining functionality and technical requirements (still uses in-memory HashMap).

### Short Code Generation
Random 6-character alphanumeric codes (62^6 = 56.8 billion combinations) using `SecureRandom` (cryptographically strong random number generator). Chose random over sequential approach to prevent enumeration attacks where an attacker tries all possible codes to discover private URLs, protecting user privacy. Includes collision detection with 5-attempt retry mechanism.

### Layered Architecture
Clear separation: CLI → Service → Repository → Model. All dependencies are injected through constructors where service receives repository, generator and validator as parameters rather than creating them internally, making components testable and loosely coupled. Repository uses interface so that storage can be swapped in the future for an external database without changing service code. Generator and Validator are concrete classes since they have single implementations, avoiding unnecessary abstraction.

### URL Validation
Java's `URL` and `URI` classes instead of regex for robust handling of edge cases (internationalized domains, ports, query params). Validates protocol (HTTP/HTTPS only for security), host presence, and enforces 2048 character limit (limit for Google Chrome).

### Multiple Codes per URL
Each shortening creates new code even for duplicate URLs. Enables independent tracking (for different teams/campaigns), preserves privacy (separate stats), supports multiple custom codes for same destination and matches industry standard (bit.ly, TinyURL). Considered alternative of deduplication but this would prevent tracking flexibility.

### Thread-Safety
Uses `ConcurrentHashMap` despite CLI being single-threaded, so that if evolved to web service in the future, same code handles concurrent requests safely with zero or minimal changes.

### Logging
SLF4J + Logback instead of `System.out.println` for better and more professional logging. Features include severity levels (DEBUG/INFO/WARN/ERROR), automatic timestamps, clean separation of user output vs system logs and production-ready configurability via `logback.xml`.

## Extensions Implemented

- **Extension 1: Click Statistics** - Tracks access count and creation timestamp
- **Extension 4: Custom Short Codes** - User-defined codes with validation (alphanumeric; max 6 chars)
- **Extension 5: URL Validation** - HTTP/HTTPS only, validates format, max 2048 chars

## Testing
```bash
./gradlew test
```

40+ tests covering short code generation, URL validation, repository operations and service integration. Uses real objects instead of mocks since all dependencies are lightweight and deterministic.

## Technical Details

- **Language:** Java 17
- **Build Tool:** Gradle 8.5
- **Dependencies:** SLF4J 2.0.9, Logback 1.4.11, JUnit 5.10.0
