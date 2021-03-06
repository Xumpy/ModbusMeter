package com.xumpy.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

public class MQTTPublisher {

    private MqttClient client;
    private String address;
    private Integer port;

    private static final Integer SLEEP_BETWEEN_ERROR = 5000;

    public MQTTPublisher(String address, Integer port) throws MqttException {
        this.address = address;
        this.port = port;

        this.client = new MqttClient("tcp://" + address + ":" + port, MqttClient.generateClientId());
        this.client.setTimeToWait(SLEEP_BETWEEN_ERROR);
    }

    public void connect() {
        try {
            client.connect();
        } catch (MqttException mqttException) {
            mqttException.printStackTrace();
        }
    }

    public void reconnect(){
        while(true) {
            try {
                client.disconnect();
                client.close();
            } catch (Exception exception){
                exception.printStackTrace();
            }
            try{
                Thread.sleep(SLEEP_BETWEEN_ERROR);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try{
                this.client = new MqttClient("tcp://" + this.address + ":" + this.port, MqttClient.generateClientId());
                this.client.setTimeToWait(SLEEP_BETWEEN_ERROR);
                client.connect();
                System.out.println("Reconnected to the client");
                break;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException mqttException) {
            mqttException.printStackTrace();
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
                reconnect();
            }
        }
    }
}
