import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apiKey = "d7cf3361e002b1eedcca5f52";

        // Вводим исходную валюту
        System.out.print("Введите исходную валюту (например, ILS): ");
        String initial_currency = scanner.nextLine().toUpperCase();

        // Вводим сумму
        System.out.print("Введите сумму: ");
        double user_amount = scanner.nextDouble();
        scanner.nextLine();

        // Вводим валюту в которую хотите конвертировать
        System.out.print("Введите валюту в которую хотите конвертировать (например, USD): ");
        String target_currency = scanner.nextLine().toUpperCase();

        String baseCurrency = "USD"; // Базовая валюта в API

        try {
            // Формируем URL для запроса
            String urlString =  "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Получаем ответ
            Scanner Urlscanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (Urlscanner.hasNext()) {
                response.append(Urlscanner.nextLine());
            }
            Urlscanner.close();

            // Разбираем JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

            // Проверяем, есть ли введенные валюты в API
            if (!rates.has(initial_currency) || !rates.has(target_currency)) {
                System.out.println("Ошибка: указанная валюта не поддерживается.");
                return;
            }

            // Получаем курсы валют относительно USD
            double initialRate = rates.getDouble(initial_currency);
            double targetRate = rates.getDouble(target_currency);

            // Конвертируем сумму
            double convertedAmount = (user_amount / initialRate) * targetRate;

            // Выводим результат
            System.out.printf("%.2f %s = %.2f %s%n", user_amount, initial_currency, convertedAmount, target_currency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

