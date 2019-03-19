package com.burmistrov.checkFile;


import com.burmistrov.Bot.Bot;
import com.burmistrov.Main.SimpleGUI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Поиск строки в файле */
public class SearchString {

    /** Массив для внутреннего кеширование строк */
    public static ArrayList<String> errors = new ArrayList<>();


    /** Чтение строки из файла */
    public static void readString() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new
                    FileInputStream(SimpleGUI.path), /*"Cp1251"*/StandardCharsets.UTF_8));
            String strRead = null;
            //Pattern pattern = Pattern.compile("Parsing done, commands count: \\d+, layer correct: False");
            //"set komtppauza/комТПпауза to True" = "ПАУЗА"
            //
            //"set komtpproc/комТПпроц to True" = "Процесс пошёл"
            //
            //"set komtpprer/комТПпрер to True" = "Прерывание процесса"
            Pattern patternO = Pattern.compile(".*Кислорода 0,3%!");
            Pattern patternP = Pattern.compile(".*Давление 1,5 кПа!");
            Pattern patternPause = Pattern.compile(".*пауза*.");
            Pattern patternProcessStarts = Pattern.compile(".*проц*.");
            Pattern patternProcessInterrupt = Pattern.compile(".*прер*.");


            SearchString searchString = new SearchString();

            while ((strRead = br.readLine()) != null) {
                Matcher matcherO = patternO.matcher(strRead);
                Matcher matcherP = patternP.matcher(strRead);
                Matcher matcherPause = patternPause.matcher(strRead);
                Matcher matcherProcessStarts = patternProcessStarts.matcher(strRead);
                Matcher matcherProcessInterrupt = patternProcessInterrupt.matcher(strRead);
                if (matcherO.find()) {
                    findAndSendMessage(strRead, searchString);
                } else if (matcherP.find()) {
                    findAndSendMessage(strRead, searchString);
                } else if (matcherPause.find()) {
                    System.out.println("пауза");
                    findAndSendWord(strRead, searchString, "ПАУЗА");
                } else if (matcherProcessStarts.find()) {
                    System.out.println("процесс");
                    findAndSendWord(strRead, searchString, "Процесс пошёл");
                } else if (matcherProcessInterrupt.find()) {
                    System.out.println("прерыв");
                    findAndSendWord(strRead, searchString, "Прерывание процесса");
                }
            }
            br.close();
        }
        catch (Exception e) {
            System.err.println("Error file");
            e.printStackTrace();
        }
    }

    private static void findAndSendMessage(String strRead, SearchString searchString) {
        if (searchString.existError(strRead)) {
            System.out.println("Уже есть эта строчка");
        }
        else {
            errors.add(strRead);
            System.out.println("Отправляем: " + strRead);
            Bot bot = new Bot();
            bot.sendMessage(strRead);
        }
    }

    private static void findAndSendWord(String strRead, SearchString searchString, String word) {
        if (searchString.existError(strRead)) {
            System.out.println("Уже есть эта строчка");
        }
        else {
            errors.add(strRead);
            System.out.println("найдена" + strRead);
            System.out.println("Отправляем: " + word);
            Bot bot = new Bot();
            bot.sendMessage(word);
        }
    }

    /** Проверка наличия строки в массиве */
    private boolean existError(String error) {
        for (String s : SearchString.errors) {
            if (error.equals(s)) {
                return true;
            }
        }
        return false;
    }
}



