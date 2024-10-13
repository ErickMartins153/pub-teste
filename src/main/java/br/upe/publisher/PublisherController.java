package br.upe.publisher;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PublisherController {
    private RedisTemplate<String, Object> redisTemplate;
    private ScheduledExecutorService executorService;
    Scanner scanner = new Scanner(System.in);

    @Autowired
    public PublisherController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    @PostConstruct
    public void publishMessage() {
        System.out.println("Digite o canal que você deseja entrar:");
        String canal = scanner.nextLine();

        if (Objects.equals(canal, "temperatura")) {
            temperatura(canal);
        } else {
            chat(canal);
        }
    }

    private void chat(String canal) {
        System.out.printf("Digite a mensagem para enviar para o canal '%s' (ou 'sair' para sair):", canal);

        while (true) {
            String message = scanner.nextLine();
            if ("sair".equalsIgnoreCase(message)) {
                System.out.println("Saindo...");
                break;
            }
            redisTemplate.convertAndSend(canal, message);
            System.out.println("Mensagem publicada: " + message);
        }
        scanner.close();

    }


    private void temperatura(String canal) {
        executorService.scheduleAtFixedRate(() -> {
            String valor = new Random().nextFloat(1, 100) + " C";
            redisTemplate.convertAndSend(canal, valor);
            System.out.println("Temperatura enviada: " + valor);
        }, 0, 5 + new Random().nextInt(6), TimeUnit.SECONDS);
    }
}

