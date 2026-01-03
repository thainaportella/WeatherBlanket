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
	private static final int REGISTERING_HOUR = 15;
	private static int await = 1;


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

			if (agora.getHour() < REGISTERING_HOUR) {
				await = REGISTERING_HOUR - agora.getHour();

				System.out.println(
						"""
                        A temperatura será registrada mais tarde hoje. Hora agora: %s
                        Tempo de espera até o próximo registro: %s hora(s)
                        """.formatted(
								agora.format(fHora),
								await
						)
				);
			} else if(agora.getHour() > REGISTERING_HOUR) {
				await = (24 - agora.getHour()) + REGISTERING_HOUR;

				System.out.println(
						"""
                        Sem necessidade de registro de temperatura hoje. Hora: %s
                        Tempo de espera até o próximo registro: %s hora(s)
                        """.formatted(
								agora.format(fHora),
								await
						)
				);
			}

			if (agora.getHour() == REGISTERING_HOUR) {
				System.out.println("Horário captado e iniciando registro de temperatura. Hora: " + agora.format(fHora));
				double temperatura = buscarTemperatura();
				if (temperatura != Double.MIN_VALUE) {
					salvarNoCsv(temperatura);
				}
				scheduler.shutdown();
				agendarApos15h();
			}
		}, 0, await, java.util.concurrent.TimeUnit.HOURS);
	}

	private static void agendarApos15h() {
		java.util.concurrent.ScheduledExecutorService scheduler24h =
				java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
		System.out.println("Próximo registro de temperatura para daqui a 24 horas.");

		scheduler24h.scheduleAtFixedRate(() -> {
			java.time.LocalTime agora = java.time.LocalTime.now(ZONA_BRASIL);
			DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH:mm");

			System.out.println("Horário captado e iniciando registro de temperatura. Hora: " + agora.format(fHora));
			double temperatura = buscarTemperatura();
			if (temperatura != Double.MIN_VALUE) {
				salvarNoCsv(temperatura);
			}
		}, 2, 2, java.util.concurrent.TimeUnit.HOURS);
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

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH, true))) {
				ColorsEnum cor = ColorsEnum.fromTemperatura(temperatura);

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

				System.out.println("Temperatura de " + temperatura + " registrada. Cor associada: " + cor.name());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
