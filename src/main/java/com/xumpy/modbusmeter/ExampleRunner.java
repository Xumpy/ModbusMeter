package com.xumpy.modbusmeter;

import com.xumpy.modbusmeter.interfaces.Processor;
import java.io.IOException;
import java.util.Map;

public class ExampleRunner {
    public static void main(String[] args) throws IOException {
        ModbusMeter modbusMeter = new ModbusMeter("127.0.0.1", 1502);

        Processor commandLinePrinter = new Processor() {
            @Override
            public void execute(Map<String, Object> modbusRows, String fileName) {
                System.out.println(fileName + ": " + modbusRows);
            }
        };

        String[] fileNames = { "solaredge.modbus", "solaredge_battery.modbus", "solaredge_meter.modbus"};

        modbusMeter.readModbus(fileNames, commandLinePrinter, 0);
    }
}
