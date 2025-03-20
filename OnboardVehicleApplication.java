package com.Truck.Project;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnboardVehicleApplication implements CommandLineRunner {

	private static final String BROKER = "tcp://localhost:1883";
	private static final String VEHICLE_ID = "123";
	private static final String COMMAND_TOPIC = "/vehicle/" + VEHICLE_ID;
	private static final String STATUS_TOPIC = "/vehicle/" + VEHICLE_ID + "/status";
	private String status = "Unknown";
	private MqttClient client;

	public static void main(String[] args) {
		SpringApplication.run(OnboardVehicleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			client = new MqttClient(BROKER, VEHICLE_ID);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setAutomaticReconnect(true);

			client.connect(options);
			System.out.println("✅ Vehicle Connected to MQTT Broker!");

			// Subscribe to the command topic
			client.subscribe(COMMAND_TOPIC, (topic, message) -> {
				String command = new String(message.getPayload());
				System.out.println("📩 Received Command: " + command);
				processCommand(command);
			});

			System.out.println("🚚 Listening for commands on: " + COMMAND_TOPIC);
		} catch (MqttException e) {
			System.err.println("❌ MQTT connection error: " + e.getMessage());
		}
	}

	private void processCommand(String command) {
		if (!client.isConnected()) {
			System.err.println("❌ Client is not connected! Cannot process command.");

		}

		 status = switch (command) {
			case "START" -> "🚀 VEHICLE_STARTED";
			case "STOP" -> "🛑 VEHICLE_STOPPED";
			case "TURN_LEFT" -> "⬅ VEHICLE_TURNED_LEFT";
			case "TURN_RIGHT" -> "➡ VEHICLE_TURNED_RIGHT";
			default -> "❓ UNKNOWN_COMMAND";
		};

		sendStatusUpdate(status);
	}



	private void sendStatusUpdate(String status) {
		try {
			if (!client.isConnected()) {
				System.err.println("❌ Client is not connected! Cannot send status.");
				return;
			}

			MqttMessage message = new MqttMessage(status.getBytes());
			message.setQos(1);
			client.publish(STATUS_TOPIC, message);
			System.out.println("✅ Sent Status: " + status);
		} catch (MqttException e) {
			System.err.println("❌ Failed to send status update: " + e.getMessage());
		}

	}

	public String getLatestStatus() {
		return status;
	}
}
