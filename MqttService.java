package com.Truck.Project;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;

@Service
public class MqttService {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String CLIENT_ID = "backend-server";
    private MqttClient client;

    public MqttService() {
        connectToBroker();
    }

    private void connectToBroker() {
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            client.connect(options);

            // Subscribe to vehicle status updates
            client.subscribe("/vehicle/+/status", (topic, message) -> {
                String receivedMessage = new String(message.getPayload());
                System.out.println("Received from " + topic + ": " + receivedMessage);
            });

            System.out.println("Backend connected to MQTT Broker.");
        } catch (MqttException e) {
            System.err.println("MQTT connection failed: " + e.getMessage());
        }
    }

    public void publishCommand(String vehicleId, String command) {
        String topic = vehicleId.equals("commands") ? "/vehicle/commands" : "/vehicle/" + vehicleId;
        try {
            MqttMessage message = new MqttMessage(command.getBytes());
            message.setQos(1);
            client.publish(topic, message);
            System.out.println("Sent command: " + command + " to topic: " + topic);
        } catch (MqttException e) {
            System.err.println("Failed to send command: " + e.getMessage());
        }
    }

    @PreDestroy
    public void cleanUp() {
        try {
            if (client.isConnected()) {
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            System.err.println("Error closing MQTT connection: " + e.getMessage());
        }
    }
}
