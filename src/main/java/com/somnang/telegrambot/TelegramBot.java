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
import java.net.URL;
import java.util.Arrays;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private PhotoRepository repository;
    @Autowired
    private GeminiService geminiService;
    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String cmd = update.getMessage().getText();
        if (cmd.split(" ")[0].equals("/AI")) {
            String[] allText = cmd.split("/AI ");
            String newTex=String.join(" ", Arrays.copyOfRange(allText,1,allText.length));


            String text = geminiService.getGeminiResponse(newTex);
           //  System.err.println(text);
            Gson gson = new Gson();
            GeminiResponse response = gson.fromJson(text, GeminiResponse.class);

            //   System.err.println(response);
            SendMessage sendMessage = new SendMessage();


            response.getCandidates().stream().forEach(c -> {
                c.getContent().getParts().stream().forEach(t -> {

                    System.err.println(t.getText());
                    sendMessage.setText(t.getText());
                });
            });
            sendMessage.setChatId(chatId);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        var p = repository.findByCmd(cmd);

        if (p!=null){
            sendImageFromUrl(chatId, p.getImageUrl());
        }

      /*  else if (cmd.equals("/pickLine")) {
      *//*      String text = cmd.split(" ")[1];
            //System.err.println(text.split(" ")[1]);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://instagram-scraper-2022.p.rapidapi.com/ig/info_username/?user=" + text)).header("X-RapidAPI-Key", "d591e196edmsh71734c4c0f2880ep1b244fjsn3fe5431155dd").header("X-RapidAPI-Host", "instagram-scraper-2022.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> response = null;
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*//*
            // System.out.println(response.body());
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
            // IGResponse res = gson.fromJson(response.body(), IGResponse.class);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(res.getText());
            // sendMessage.setText(res.getUser().getHd_profile_pic_url_info().getUrl());
           *//* } else {
                sendMessage.setText("I can not find this username: " + text + " IG for you");

            }*//*
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }*/

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
