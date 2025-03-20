package com.Truck.Project;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisherService {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String CLIENT_ID = "backend-server";
    private MqttClient client;

    public MqttPublisherService() {
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String command) {
        try {
            MqttMessage message = new MqttMessage(command.getBytes());
            message.setQos(1);
            client.publish(topic, message);
            System.out.println("Published Command: " + command + " to " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
