import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverterGUI extends JFrame {
    private JTextField amountField;
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JLabel resultLabel;

    public CurrencyConverterGUI() {
        setTitle("Конвертер валют");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // Поля ввода и кнопки
        add(new JLabel("Сумма:"));
        amountField = new JTextField();
        add(amountField);

        add(new JLabel("Из валюты:"));
        fromCurrency = new JComboBox<>(new String[]{"USD", "EUR", "ILS", "GBP", "JPY", "AUD", "CAD", "CHF", "INR", "CNY", "MXN", "RUB", "BRL", "UAH"
        });
        add(fromCurrency);

        add(new JLabel("В валюту:"));
        toCurrency = new JComboBox<>(new String[]{"USD", "EUR", "ILS", "GBP", "JPY", "AUD", "CAD", "CHF", "INR", "CNY", "MXN", "RUB", "BRL", "UAH"
        });
        add(toCurrency);

        JButton convertButton = new JButton("Конвертировать");
        add(convertButton);

        resultLabel = new JLabel("Результат: ");
        add(resultLabel);

        // Обработчик событий
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        setVisible(true);
    }

    private void convertCurrency() {
        String apiKey = "d7cf3361e002b1eedcca5f52"; // Твой API-ключ
        String baseCurrency = "USD";
        String from = (String) fromCurrency.getSelectedItem();
        String to = (String) toCurrency.getSelectedItem();

        try {
            double amount = Double.parseDouble(amountField.getText());

            String urlString = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner UrlScanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (UrlScanner.hasNext()) {
                response.append(UrlScanner.nextLine());
            }
            UrlScanner.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

            if (!rates.has(from) || !rates.has(to)) {
                resultLabel.setText("Ошибка: валюта не найдена.");
                return;
            }

            double fromRate = rates.getDouble(from);
            double toRate = rates.getDouble(to);
            double convertedAmount = (amount / fromRate) * toRate;

            resultLabel.setText(String.format("Результат: %.2f %s", convertedAmount, to));
        } catch (Exception ex) {
            resultLabel.setText("Ошибка при конверсии.");
        }
    }

    public static void main(String[] args) {
        new CurrencyConverterGUI();
    }
}

