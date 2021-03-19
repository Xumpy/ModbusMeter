package com.xumpy.modbusmeter.processors;

import com.ghgande.j2mod.modbus.procimg.Register;
import com.xumpy.modbusmeter.pojo.ModbusRow;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class EntryProcessor {
    private static Object createEntryValue(ModbusRow modbusRow, Register[] registers) throws IOException {
        if (modbusRow.getType().equals("string")){
            return RegistryProcessor.registerToString(registers);
        }
        if (modbusRow.getType().equals("int")){
            return RegistryProcessor.registerToInt(registers);
        }
        if (modbusRow.getType().equals("floatle")){
            return RegistryProcessor.registerToFloatLittleEndian(registers);
        }
        if (modbusRow.getType().equals("float")){
            return RegistryProcessor.registerToFloat(registers);
        }
        return "type not recognized";
    }

    public static Map.Entry<String, Object> createEntry(ModbusRow modbusRow, Register[] registers) throws IOException {
        Map.Entry<String,Object> entry =
                new AbstractMap.SimpleEntry<>(modbusRow.getIdentifier(),
                        createEntryValue(modbusRow, registers));
        return entry;
    }
}
