package com.example.telegrambotgetcurrencyrate.service;

import com.example.telegrambotgetcurrencyrate.config.BotConfig;
import lombok.AllArgsConstructor;
import com.example.telegrambotgetcurrencyrate.model.CurrencyModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        CurrencyModel currencyModel = new CurrencyModel();
        String currency = "";

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/Почати":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    try {
                        currency = CurrencyService.getCurrencyRate(messageText, currencyModel);

                    } catch (IOException e) {
                        sendMessage(chatId, "Ми не знайшли такої валюти, спробуйте ще раз :(." + "\n" +
                                "Введіть офіційну валюту" + "\n" +
                                "яку валюту ви хочете конвертувати в UAH." + "\n" +
                                "Наприклад: USD");

                    } catch (ParseException e) {
                        throw new RuntimeException("Не можна зв'язатись з нац банком");
                    }
                    sendMessage(chatId, currency);
            }
        }

    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Здраствуйте, " + name + ", раді вас бачити!" + "\n" +
                "Введіть офіційну валюту" + "\n" +
                "яку ви хочете конвертувати в UAH." + "\n" +
                "Наприклад: USD";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}

