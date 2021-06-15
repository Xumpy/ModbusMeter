package com.xumpy.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

public class MQTTPublisher {

    private MqttClient client;
    private static final Integer SLEEP_BETWEEN_ERROR = 5000;

    public MQTTPublisher(String address, Integer port) throws MqttException {
        this.client = new MqttClient("tcp://" + address + ":" + port, MqttClient.generateClientId());
    }

    public void connect() {
        while(true){
            try {
                client.connect();
                client.setTimeToWait(SLEEP_BETWEEN_ERROR);
                break;
            } catch (MqttException mqttException) {
                mqttException.printStackTrace();
            }
        }
    }

    public void disconnect() {
        while(true){
            try {
                client.disconnect();
                break;
            } catch (MqttException mqttException) {
                mqttException.printStackTrace();
            }
        }
    }

    public void publish(Map<String, Object> message, String topic){
        ObjectMapper objectMapper = new ObjectMapper();

        while(true){
            try {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setPayload(objectMapper.writeValueAsString(message).getBytes());
                client.publish(topic, mqttMessage);
                break;

            } catch (JsonProcessingException | MqttException e) {
                e.printStackTrace();
                System.out.println("Publish failed. Probabibly due to connection problems. Trying to reconnect");
                disconnect();
                connect();
            }
        }
    }
}
