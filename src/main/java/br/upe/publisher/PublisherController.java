package br.upe.publisher;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class PublisherController {
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PublisherController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void publishMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a mensagem para enviar ao Redis (ou 'sair' para sair):");

        while (true) {
            String message = scanner.nextLine();
            if ("sair".equalsIgnoreCase(message)) {
                System.out.println("Saindo...");
                break;
            }
            redisTemplate.convertAndSend("testa", message);
            System.out.println("Mensagem publicada: " + message);
        }
        scanner.close();

    }
}

