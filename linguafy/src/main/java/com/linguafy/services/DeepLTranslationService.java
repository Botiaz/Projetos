package com.linguafy.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class DeepLTranslationService {

    private final RestClient restClient;
    private final String apiKey;
    private final String defaultTargetLang;

    public DeepLTranslationService(
            @Value("${deepl.base-url:https://api-free.deepl.com}") String baseUrl,
            @Value("${deepl.api-key:}") String apiKey,
            @Value("${deepl.default-target-lang:PT-BR}") String defaultTargetLang) {
        this.restClient = RestClient.builder()
            .baseUrl(Objects.requireNonNull(baseUrl))
                .build();
        this.apiKey = apiKey;
        String normalizedTargetLang = normalizeLanguageCode(defaultTargetLang);
        this.defaultTargetLang = normalizedTargetLang == null ? "PT-BR" : normalizedTargetLang;
    }

    public String translate(String text, String sourceLang) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Texto da palavra não pode ser vazio para tradução automática.");
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("DeepL API key não configurada. Defina deepl.api-key no application.yml ou variável de ambiente.");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("text", text);
        formData.add("target_lang", defaultTargetLang);

        String normalizedSourceLang = normalizeLanguageCode(sourceLang);
        if (normalizedSourceLang != null && !normalizedSourceLang.equals(defaultTargetLang)) {
            formData.add("source_lang", normalizedSourceLang);
        }

        DeepLTranslateResponse response = restClient.post()
                .uri("/v2/translate")
                .header("Authorization", "DeepL-Auth-Key " + apiKey)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_FORM_URLENCODED))
                .body(formData)
                .retrieve()
                .body(DeepLTranslateResponse.class);

        if (response == null || response.translations() == null || response.translations().isEmpty()) {
            throw new IllegalStateException("Resposta inválida da API DeepL.");
        }

        String translatedText = response.translations().get(0).text();
        if (translatedText == null || translatedText.isBlank()) {
            throw new IllegalStateException("A API DeepL não retornou tradução válida.");
        }

        return translatedText;
    }

    public String getDefaultTargetLang() {
        return defaultTargetLang;
    }

    private String normalizeLanguageCode(String langCode) {
        if (langCode == null || langCode.isBlank()) {
            return null;
        }
        return langCode.trim().toUpperCase(Locale.ROOT);
    }

    private record DeepLTranslateResponse(List<DeepLTranslationItem> translations) {
    }

    private record DeepLTranslationItem(String detected_source_language, String text) {
    }
}