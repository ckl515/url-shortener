package io.github.ckl515.urlshortener;

import io.github.ckl515.urlshortener.cli.CommandHandler;
import io.github.ckl515.urlshortener.generator.ShortCodeGenerator;
import io.github.ckl515.urlshortener.repository.InMemoryUrlRepository;
import io.github.ckl515.urlshortener.repository.UrlRepository;
import io.github.ckl515.urlshortener.service.UrlShortenerService;

import java.util.Scanner;

public class UrlShortenerApp {
    public static void main(String[] args) {
        UrlRepository repository = new InMemoryUrlRepository();
        ShortCodeGenerator codeGenerator = new ShortCodeGenerator();
        UrlShortenerService service = new UrlShortenerService(repository, codeGenerator);
        CommandHandler commandHandler = new CommandHandler(service);

        if (args.length == 0) {
            // Interactive mode
            runInteractiveMode(commandHandler);
        } else {
            // Single command mode
            commandHandler.handleCommand(args);
        }
    }

    private static void runInteractiveMode(CommandHandler commandHandler) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'exit' to quit");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equals("exit")) {
                System.out.println("URL Shortener program exited");
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            String[] args = input.split("\\s+");
            commandHandler.handleCommand(args);
            System.out.println();
        }

        scanner.close();
    }
}
