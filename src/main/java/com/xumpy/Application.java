package com.xumpy;

import com.xumpy.modbusmeter.ModbusMeter;
import com.xumpy.modbusmeter.interfaces.Processor;
import com.xumpy.mqtt.MQTTPublisher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class Application implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private String solarEdgeServer = "192.168.1.100";
    private String solarEdgeMQTTBroker = "192.168.1.31";
    private Integer solarEdgePort = 1502;
    private Integer solarEdgeMQTTPort = 1883;
    private String solarEdgeFiles[] = {"solaredge.modbus", "solaredge_battery.modbus", "solaredge_meter.modbus"};
    private MQTTPublisher solarEdgeMqttPublisher;

    Processor commandLinePrinter = new Processor() {
        @Override
        public void execute(Map<String, Object> modbusRows, String fileName) {
            if (fileName.equals("solaredge.modbus")) {
                solarEdgeMqttPublisher.publish(modbusRows, "solar_meter/info");
            }
            if (fileName.equals("solaredge_battery.modbus")) {
                solarEdgeMqttPublisher.publish(modbusRows, "solar_meter/battery1");
            }
            if (fileName.equals("solaredge_meter.modbus")) {
                solarEdgeMqttPublisher.publish(modbusRows, "solar_meter/meter1");
            }
        }
    };

    @Override
    public void run(ApplicationArguments args) {
        Thread modbusThreadSolarEdge = new Thread(){
            public void run(){
                while(true){
                    ModbusMeter modbusMeter = new ModbusMeter(solarEdgeServer, solarEdgePort);
                    try {
                        solarEdgeMqttPublisher = new MQTTPublisher(solarEdgeMQTTBroker, solarEdgeMQTTPort);
                        solarEdgeMqttPublisher.connect();
                        modbusMeter.readModbus(solarEdgeFiles, commandLinePrinter, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        solarEdgeMqttPublisher.disconnect();
                    }
                }
            }
        };
        modbusThreadSolarEdge.start();
    }
}
