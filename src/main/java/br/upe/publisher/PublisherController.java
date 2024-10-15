package br.upe.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class PublisherController {
    private RedisTemplate<String, Object> redisTemplate;
    private ScheduledExecutorService executorService;

    @Autowired
    public PublisherController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/publish")
    public String publishPage() {
        return "publish";
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String canal, @RequestParam String noticia) {
        redisTemplate.convertAndSend(canal, noticia);
        System.out.println("Mensagem publicada no canal '" + canal + "': " + noticia);

        return "redirect:/publish" ;
    }

    @PostMapping("/changeChannel")
    public String changeChannel(@RequestParam String canal, @RequestParam String noticia) {
        return "redirect:/changeChannel?canal=" + canal + "&noticia=" + noticia;
    }

}
