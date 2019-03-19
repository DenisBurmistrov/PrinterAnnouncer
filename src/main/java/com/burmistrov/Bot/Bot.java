package com.burmistrov.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/** Телеграм-бот */
public class Bot extends TelegramLongPollingBot {

    /**
     * Ответ на полученное сообщение
     * @param update сущность изменения в диалогах
     */
    @Override
    public void onUpdateReceived(Update update) {

        // Проверяем есть ли сообщение и есть ли в этом сообщении текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            System.out.println(chat_id);  //Выводим в консоль айди диалога, информация для настройки кому отправлять логи

            SendMessage message = new SendMessage() // Создаем объект сообщения
                    .setChatId(chat_id)
                    .setText(message_text);
            try {
                execute(message); // Отправляем сообщение
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Конфигурации сообщения
     * @param messageFromFile полученная строка в диалоге
     */
    public void sendMessage(String messageFromFile){
        SendMessage message = new SendMessage()
                .setChatId((long) -295500972)
                .setText(messageFromFile);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /** Никнейм бота */
    @Override
    public String getBotUsername() {
        // TODO
        return "PrinterAnnounce_bot";
    }

    /** Токен бота */
    @Override
    public String getBotToken() {
        // TODO
        return "630440678:AAFflcjplimRh6owX11brAV7IRpYmzdM904";
    }
}