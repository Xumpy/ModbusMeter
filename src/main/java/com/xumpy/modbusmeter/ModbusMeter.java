package com.xumpy.modbusmeter;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.xumpy.modbusmeter.interfaces.Processor;
import com.xumpy.modbusmeter.pojo.ModbusRow;
import com.xumpy.modbusmeter.processors.EntryProcessor;
import com.xumpy.modbusmeter.processors.ModbusFileProcessor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModbusMeter {
    private String address;
    private Integer port;
    private Integer offset;

    private void readFileInModbus(ModbusTCPMaster master, String fileName, String data, Processor processor, Integer sleepTime) throws InterruptedException, ModbusException, IOException {
        Map<String, Object> modbusValues = new LinkedHashMap<>();
        List<ModbusRow> modbusRows = ModbusFileProcessor.process(data);

        for (ModbusRow modbusRow : modbusRows) {
            Register[] registers = master.readMultipleRegisters(modbusRow.getAddress() + offset, modbusRow.getSize());

            Map.Entry<String, Object> entry = EntryProcessor.createEntry(modbusRow, registers);
            modbusValues.put(entry.getKey(), entry.getValue());
        }

        processor.execute(modbusValues, fileName);

        Thread.sleep(sleepTime);
    }

    public void readModbus(String fileNames[], Processor processor, Integer sleepTimeBetween) throws IOException {
        Map<String, String> filesAndContent = new LinkedHashMap<>();
        for(String fileName: fileNames){
            String data = IOUtils.toString(ModbusMeter.class.getResourceAsStream("/" + fileName), "UTF-8");
            filesAndContent.put(fileName, data);
        }

        ModbusTCPMaster master = null;
        try {
            master = new ModbusTCPMaster(this.address, this.port);
            master.connect();

            while(true){
                for(Map.Entry<String, String> entry: filesAndContent.entrySet()){
                    readFileInModbus(master, entry.getKey(), entry.getValue(), processor, sleepTimeBetween);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (master != null) {
                master.disconnect();
            }
        }
    }

    public void readModbus(String fileName, Processor processor, Integer sleepTimeBetween) throws IOException {
        String data = IOUtils.toString(ModbusMeter.class.getResourceAsStream("/" + fileName), "UTF-8");

        ModbusTCPMaster master = null;
        try {
            master = new ModbusTCPMaster(this.address, this.port);
            master.connect();

            while(true) readFileInModbus(master, fileName, data, processor, sleepTimeBetween);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (master != null) {
                master.disconnect();
            }
        }
    }

    public ModbusMeter(String address, Integer port){
        this.address = address;
        this.port = port;
        this.offset = 0;
    }

    public ModbusMeter(String address, Integer port, Integer offset){
        this.address = address;
        this.port = port;
        this.offset = offset;
    }
}
