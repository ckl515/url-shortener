package io.github.ckl515.urlshortener;

import io.github.ckl515.urlshortener.cli.CommandHandler;
import io.github.ckl515.urlshortener.generator.ShortCodeGenerator;
import io.github.ckl515.urlshortener.repository.InMemoryUrlRepository;
import io.github.ckl515.urlshortener.repository.UrlRepository;
import io.github.ckl515.urlshortener.service.UrlShortenerService;

public class UrlShortenerApp {
    public static void main(String[] args) {
        UrlRepository repository = new InMemoryUrlRepository();
        ShortCodeGenerator codeGenerator = new ShortCodeGenerator();
        UrlShortenerService service = new UrlShortenerService(repository, codeGenerator);
        CommandHandler commandHandler = new CommandHandler(service);

        commandHandler.handleCommand(args);
    }
}
