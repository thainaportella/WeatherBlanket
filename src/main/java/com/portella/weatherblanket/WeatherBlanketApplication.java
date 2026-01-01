package com.portella.weatherblanket;

import com.portella.weatherblanket.Enum.ColorsEnum;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class WeatherBlanketApplication {
	private static final String CSV_PATH = "src/main/resources/data/temperaturas.csv";
	private static final ZoneId ZONA_BRASIL = ZoneId.of("America/Sao_Paulo");


	public static void main(String[] args) {
		System.out.println("Temperature Blanket Application v.1 started...");
		agendarColetaDiaria();
	}

	private static void agendarColetaDiaria() {
		java.util.concurrent.ScheduledExecutorService scheduler =
				java.util.concurrent.Executors.newSingleThreadScheduledExecutor();

		scheduler.scheduleAtFixedRate(() -> {
			java.time.LocalTime agora = java.time.LocalTime.now(ZONA_BRASIL);
			DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH:mm");

			if (agora.getHour() != 17) {
				double temperatura = buscarTemperatura();
				if (temperatura != Double.MIN_VALUE) {
					System.out.println("Horário captado. Sem necessidade de registro de temperatura. Hora: " + agora.format(fHora));
				}
			}


			if (agora.getHour() == 17) {
				System.out.println("Horário captado e iniciando registro de temperatura. Hora: " + agora.format(fHora));
				double temperatura = buscarTemperatura();
				if (temperatura != Double.MIN_VALUE) {
					salvarNoCsv(temperatura);
				}

				scheduler.shutdown();
				agendarApos15h();
			}
		}, 0, 1, java.util.concurrent.TimeUnit.HOURS);
	}

	private static void agendarApos15h() {
		java.util.concurrent.ScheduledExecutorService scheduler24h =
				java.util.concurrent.Executors.newSingleThreadScheduledExecutor();


		scheduler24h.scheduleAtFixedRate(() -> {
			double temperatura = buscarTemperatura();
			if (temperatura != Double.MIN_VALUE) {
				salvarNoCsv(temperatura);
			}
		}, 24, 24, java.util.concurrent.TimeUnit.HOURS);
	}

	private static double buscarTemperatura() {
		try {
			var url = new java.net.URL("https://api.open-meteo.com/v1/forecast?latitude=-22.41826&longitude=-42.97477&current=temperature_2m");
			var conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");


			try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
				var json = mapper.readTree(response.toString());
				return json.path("current").path("temperature_2m").asDouble();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Double.MIN_VALUE;
		}
	}

	private static void salvarNoCsv(double temperatura) {
		try {
			Path path = Paths.get(CSV_PATH);
			Files.createDirectories(path.getParent());
			ColorsEnum cor = ColorsEnum.fromTemperatura(temperatura);


			boolean novoArquivo = !Files.exists(path);


			try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH, true))) {
				if (novoArquivo) {
					writer.write("data,hour,temp_celsius,cor,cor_oficial");
					writer.newLine();
				}


				LocalDate data = LocalDate.now(ZONA_BRASIL);
				LocalTime hora = LocalTime.now(ZONA_BRASIL);
				DateTimeFormatter fData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH:mm");


				writer.write(data.format(fData) +
						"," + hora.format(fHora) +
						"," + temperatura +
						"," + cor.name() +
						"," + cor.getNomeOficial());
				writer.newLine();
			}


			System.out.println("Temperatura de " + temperatura + " registrada. Cor associada: " + cor.name());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
