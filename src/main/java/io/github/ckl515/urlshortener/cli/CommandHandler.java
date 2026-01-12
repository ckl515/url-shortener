package io.github.ckl515.urlshortener.cli;

import io.github.ckl515.urlshortener.model.ShortenedUrl;
import io.github.ckl515.urlshortener.service.UrlShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final UrlShortenerService service;

    public CommandHandler(UrlShortenerService service) {
        this.service = service;
    }

    public void handleCommand(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            String command = args[0].toLowerCase();
            logger.debug("Executing command: {}", command);

            switch (command) {
                case "shorten":
                    handleShorten(args);
                    break;
                case "get":
                    handleGet(args);
                    break;
                case "list":
                    handleList();
                    break;
                case "stats":
                    handleStats(args);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    logger.warn("Unknown command: {}", command);
                    printUsage();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            logger.error("Error executing command: {}", e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            logger.error("Unexpected error", e);
        }
    }

    private void handleShorten(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: shorten <url> [--custom <code>]");
            return;
        }

        String url = args[1];
        String shortCode;

        if (args.length >= 4 && "--custom".equals(args[2])) {
            shortCode = service.shorten(url, args[3]);
        } else {
            shortCode = service.shorten(url);
        }

        System.out.println("Shortened URL: " + shortCode);
    }

    private void handleGet(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: get <shortCode>");
            return;
        }

        String shortCode = args[1];
        ShortenedUrl url = service.get(shortCode);

        System.out.println("Original URL: " + url.getOriginalUrl());
    }

    private void handleList() {
        Collection<ShortenedUrl> urls = service.listAll();

        if (urls.isEmpty()) {
            System.out.println("No URLs found.");
            return;
        }

        for (ShortenedUrl url : urls) {
            System.out.printf("%s -> %s%n", url.getShortCode(), url.getOriginalUrl());
        }
    }

    private void handleStats(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: stats <shortCode>");
            return;
        }
        String shortCode = args[1];
        ShortenedUrl url = service.getStats(shortCode);
        System.out.println("Short Code: " + url.getShortCode());
        System.out.println("Original URL: " + url.getOriginalUrl());
        System.out.println("Click Count: " + url.getAccessCount());
        System.out.println("Created At: " + url.getCreatedAt());
    }

    private void printUsage() {
        System.out.println("URL Shortener - Usage:");
        System.out.println("    shorten <url>                   - Shorten a URL");
        System.out.println("    shorten <url> --custom <code>   - Shorten with custom code");
        System.out.println("    get <shortCode>                 - Get original URL");
        System.out.println("    list                            - List all URLs");
        System.out.println("    stats <short code>              - Show statistics");
    }
}
