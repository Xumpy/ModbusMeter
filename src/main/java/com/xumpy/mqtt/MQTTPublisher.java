package com.xumpy.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

public class MQTTPublisher {

    private MqttClient client;

    public MQTTPublisher(String address, Integer port) throws MqttException {
        this.client = new MqttClient("tcp://" + address + ":" + port, MqttClient.generateClientId());
    }

    public void connect() throws MqttException {
        client.connect();
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public void publish(Map<String, Object> message, String topic){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(objectMapper.writeValueAsString(message).getBytes());
            client.publish(topic, mqttMessage);

        } catch (JsonProcessingException | MqttException e) {
            e.printStackTrace();
        }
    }
}
