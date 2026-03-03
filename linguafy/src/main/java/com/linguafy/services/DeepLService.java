package com.linguafy.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepLService {

    private static final String DEEPL_API_URL = "https://api-free.deepl.com/v2/translate";

    private final RestTemplate restTemplate;

    @Value("${deepl.api-key:}")
    private String apiKey;

    public DeepLService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String translate(String text, String sourceLang, String targetLang) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("DeepL API key não configurada. Defina a variável de ambiente DEEPL_API_KEY ou 'deepl.api-key' no application.yml");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

        Map<String, Object> requestBody = Map.of(
                "text", List.of(text),
                "source_lang", sourceLang.toUpperCase(),
                "target_lang", targetLang.toUpperCase()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map<String, Object> response;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = restTemplate.postForObject(DEEPL_API_URL, request, Map.class);
            response = result;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao chamar a API DeepL para tradução de '" + text + "' (" + sourceLang + " -> " + targetLang + "): " + e.getMessage(), e);
        }

        if (response == null) {
            throw new RuntimeException("Resposta vazia da API DeepL para '" + text + "' (" + sourceLang + " -> " + targetLang + ")");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, String>> translations = (List<Map<String, String>>) response.get("translations");

        if (translations == null || translations.isEmpty()) {
            throw new RuntimeException("Nenhuma tradução retornada pela API DeepL para '" + text + "' (" + sourceLang + " -> " + targetLang + ")");
        }

        return translations.get(0).get("text");
    }
}
