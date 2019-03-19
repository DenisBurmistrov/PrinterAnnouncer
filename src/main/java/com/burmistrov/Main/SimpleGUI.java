package com.burmistrov.Main;

import com.burmistrov.Bot.Bot;
import com.burmistrov.checkFile.FileWatcher;
import com.burmistrov.checkFile.SearchString;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/** Графический интерфейс */
public class SimpleGUI extends JFrame {

    /** Путь, который будет выбран в fileChooser */
    public static String path;

    public SimpleGUI() {
        super("Error Announcer");

        JFileChooser fileOpen = new JFileChooser("C:/");
        int ret = fileOpen.showDialog(null, "Открыть файл");

        if (ret == JFileChooser.APPROVE_OPTION) { //Если файл выбран
            File file = fileOpen.getSelectedFile();
            path = file.getPath();
            this.setBounds(900, 500, 350, 100);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Container container = this.getContentPane();
            container.setLayout(new GridLayout(1, 1, 1, 1));
            JButton button = new JButton("Начать отслеживание файла");
            button.addActionListener(new ButtonEventListener());
            container.add(button);
        }
    }

    /** Реагирование на кнопки */
    private class ButtonEventListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String message = "";
            message += "Подтверждение выбранного файла:" + "\n" + path + "\n" + "После подтверждения напишите боту для проверки отклика";
            JOptionPane.showMessageDialog(null,
                    message,
                    "Подтверждение",
                    JOptionPane.PLAIN_MESSAGE);
            System.getProperties().put("proxySet", "true");
           /* System.getProperties().put("socksProxyHost", "197.210.191.131");// Нигерия
            System.getProperties().put("socksProxyPort", "9999");*/
            /*System.getProperties().put("socksProxyHost", "150.109.198.17");// Япония
            System.getProperties().put("socksProxyPort", "1080");*/ //95.216.211.228:1080 финлян
            System.getProperties().put("socksProxyHost", "51.144.228.148");
            System.getProperties().put("socksProxyPort", "1080");

            // TODO Initialize Api Context
            ApiContextInitializer.init();

            // TODO Instantiate Telegram Bots API
            TelegramBotsApi botsApi = new TelegramBotsApi();

            // TODO Register our bot
            try {
                botsApi.registerBot(new Bot());
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }


            // Отслеживание файла
            TimerTask task = new FileWatcher(new File(path)) {
                protected void onChange(File file) {
                    // Выполнение при изменении файла
                    SearchString.readString();
                    System.out.println("File " + file.getName() + " have change !");

                }
            };

            java.util.Timer timer = new Timer();
            // Готовность к проверке файла
            timer.schedule(task, new Date(), 1000);
        }
    }

    public static void main(String[] args) {
        SimpleGUI app = new SimpleGUI();
        app.setVisible(true);
    }
}

