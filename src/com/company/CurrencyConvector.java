package com.company;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConvector {
    //Функция самостоятельного выбора валюты
    public static void SelfSelection() throws IOException {
        String fromCode, toCode;
        Scanner scan = new Scanner(System.in);
        double amount;
        System.out.println("Из какой валюты осуществлять перевод? Введите выбранную вами валюту в формате 3х букв, например \"USD\",\"RUB\" и тд. ");
        fromCode = scan.nextLine();

        System.out.println("В какую валюту перевести?Введите выбранную вами валюту в формате 3х букв, например \"USD\",\"RUB\" и тд. ");
        toCode = scan.nextLine();

        System.out.println("Какую сумму будем конвертировать?");
        amount = scan.nextFloat();

        sendHttpGetRequest(fromCode, toCode, amount);
    }
    // Главная функция с выбором из заготовленных валют
    public static void main(String[] args) throws IOException {
        // write your code here
        HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();

        //Add corrency dollars
        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "RUB");
        currencyCodes.put(3, "EUR");
        currencyCodes.put(4, "HKD");
        currencyCodes.put(5, "INR");

        String fromCode, toCode;
        double amount;

        Scanner scan = new Scanner(System.in);
        System.out.println("Добро пожаловать в переводчик валюты\nВы желаете сами ввести наименование валюты для перевода или воспользуетесь заготовленными вариантами?\n" +
                "1.Самостоятельный ввод \n2.Воспользуюсь заготовленными вариантамми");
        int choise = scan.nextInt();
        while (choise < 1 || choise > 2) {
            System.out.println("Вы ввели некорректное значение.Введите корректное значение и попробуйте снова");
            choise = scan.nextInt();
        }
        if (choise == 1) {
            SelfSelection();
        } else {

            System.out.println("Из какой валюты осуществлять перевод?");
            System.out.println("1:USD \t 2:RUB \t 3:EUR \t 4:HKD \t 5:INR");
            int from = scan.nextInt();
            while (from < 1 || from > 6) {
                System.out.println("Вы ввели некорректное значение.Введите корректное значение и попробуйте снова");
                from = scan.nextInt();
            }

            fromCode = currencyCodes.get(from);
            System.out.println(fromCode);

            System.out.println("В какую валюту перевести?\n 1:USD \t 2:RUB \t 3:EUR \t 4:HKD \t 5:INR");
            int to = scan.nextInt();
            while (to < 1 || to > 6) {
                System.out.println("Вы ввели некорректное значение.Введите корректное значение и попробуйте снова");
                to = scan.nextInt();
            }
            toCode = currencyCodes.get(to);

            System.out.println("Какую сумму будем конвертировать?");
            amount = scan.nextFloat();

            sendHttpGetRequest(fromCode, toCode, amount);

        }
    }
    //функция с обращением к api
    private static void sendHttpGetRequest(String fromCode, String toCode, double amount) throws IOException {
        DecimalFormat form = new DecimalFormat("00.00");
        String GET_URL = "https://api.exchangeratesapi.io/latest?base=" + toCode + "&symbols=" + fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == httpURLConnection.HTTP_OK) {//ok
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);

            }
            in.close();

            JSONObject obj = new JSONObject(response.toString());
            Double exChangeRate = obj.getJSONObject("rates").getDouble(fromCode);
            //  System.out.println(obj.getJSONObject("rates"));
            //  System.out.println(exChangeRate);
            //  System.out.println();
            System.out.println(form.format(amount) + fromCode + "=" + form.format(amount / exChangeRate) + toCode);
        } else System.out.println("GET запрос неудачен!");
    }
}


