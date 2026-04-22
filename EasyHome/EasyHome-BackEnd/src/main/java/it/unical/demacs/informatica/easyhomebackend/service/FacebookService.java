package it.unical.demacs.informatica.easyhomebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FacebookService {

    private final WebClient webClient;
    private final String ACCESS_TOKEN = "EAAZAKNzyWZA9UBOxmoJ79rPXr3YzpSl7XUS8WN78rQ3ex6XCvrGoh45MStZCJZCUvlK4OerJDmEJFzLygIn90ZBwNSlVAMxesjlTSDS5GJJKFsBLBIs8gFP5OxE2bCjR9SftVkcbUwjjOZCOdWsQXfeqN1R3qyoOOCOeribgLJInFg7L2C4hVulSVp54E2ZCsvJO1YeE4XwN67MdPAdMliOzpZAzNA0ZD";
    private final String AD_ACCOUNT_ID = "1770450960410581";  // ID dell'account pubblicitario

    public FacebookService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://graph.facebook.com/v22.0").build();
    }

    public Mono<String> createAd(String adName, String adsetId, String creativeId) {
        return webClient.post()
                .uri("/" + AD_ACCOUNT_ID + "/ads")
                .bodyValue(
                        "name=" + adName +
                                "&adset_id=" + adsetId +
                                "&creative={\"creative_id\":\"" + creativeId + "\"}" +
                                "&status=PAUSED" +
                                "&access_token=" + ACCESS_TOKEN
                )
                .retrieve()
                .bodyToMono(String.class);
    }
}

