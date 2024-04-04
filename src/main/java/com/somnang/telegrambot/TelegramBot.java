package com.somnang.telegrambot;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private PhotoRepository repository;
    @Autowired
    private TranslationService translationService;
    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String cmd = update.getMessage().getText();
        var p = repository.findByCmd(cmd);
        if (p!=null){
            sendImageFromUrl(chatId, p.getImageUrl());
        } else if (cmd.equals("/pickLine")) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://rizzapi.vercel.app/random")).build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Gson gson = new Gson();
            PickUpLineResponse res = gson.fromJson(response.body(), PickUpLineResponse.class);
            SendMessage message=new SendMessage();
            String x=translationService.translateText(res.getText(),"kh");
            System.err.println(x);
           /* message.setText(res.getText());
            message.setChatId(chatId);*/

            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void sendImageFromUrl(String chatId, String urlImage) {

        URL url = null;
        try {
            url = new URL(urlImage);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(inputStream, "image.jpg")); // Filename is optional

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "RoNang_bot";
    }

    @Override
    public String getBotToken() {
        return "6702417864:AAHxueufMvp8zuO260VsxuxNg42CPsTt8Hs";
    }
}
