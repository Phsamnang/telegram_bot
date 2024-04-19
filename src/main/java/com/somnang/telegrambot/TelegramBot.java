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
        }else {
            var p = repository.findByCmd(cmd);

            if (p!=null){
                sendImageFromUrl(chatId, p.getImageUrl());
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
