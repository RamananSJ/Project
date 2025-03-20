package com.Server.Project;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class BackendServerApplication implements CommandLineRunner {

	private static final String BROKER = "tcp://localhost:1883";
	private static final String VEHICLE_ID = "123";
	private static final String COMMAND_TOPIC = "/vehicle/" + VEHICLE_ID;
	private static final String STATUS_TOPIC = "/vehicle/" + VEHICLE_ID + "/status";

	private MqttClient client;

	public static void main(String[] args) {
		SpringApplication.run(BackendServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			client = new MqttClient(BROKER, MqttClient.generateClientId());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setAutomaticReconnect(true);

			client.connect(options);
			System.out.println("✅ Backend Server Connected to MQTT Broker!");

			// Subscribe to receive vehicle status updates
			client.subscribe(STATUS_TOPIC, (topic, message) -> {
				String status = new String(message.getPayload());
				System.out.println("📩 Received Status Update: " + status);
			});

			System.out.println("📝 Enter commands to send to vehicle (START, STOP, TURN_LEFT, TURN_RIGHT)");
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String command = scanner.nextLine().trim().toUpperCase();
				sendCommand(command);
			}

		} catch (MqttException e) {
			System.err.println("❌ Failed to connect to MQTT broker: " + e.getMessage());
		}
	}

	public void sendCommand(String command) {
		try {
			if (!client.isConnected()) {
				System.err.println("❌ Client is not connected! Cannot send command.");
				return;
			}

			MqttMessage message = new MqttMessage(command.getBytes());
			message.setQos(1);
			client.publish(COMMAND_TOPIC, message);
			System.out.println("✅ Sent Command: " + command);
		} catch (MqttException e) {
			System.err.println("❌ Failed to send command: " + e.getMessage());
		}
	}
}
