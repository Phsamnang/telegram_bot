package com.somnang.telegrambot;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class TranslationService {

    private final Translate translate;

    public TranslationService(@Value("${google.api.key}") String apiKey) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.create(new ClassPathResource(apiKey).getInputStream());
        this.translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public String translateText(String text, String targetLanguage) {
        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage));
        return translation.getTranslatedText();
    }
}
