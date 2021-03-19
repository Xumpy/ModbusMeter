package com.xumpy.modbusmeter.processors;

import com.xumpy.modbusmeter.pojo.ModbusRow;

import java.util.ArrayList;
import java.util.List;

public class ModbusFileProcessor {
    public static List<ModbusRow> process(String file){
        String[] rows = file.split("\n");
        List<ModbusRow> modbusRows = new ArrayList<>();
        for (String row: rows){
            String[] fields = row.split(",");

            ModbusRow modbusRow = new ModbusRow();
            modbusRow.setIdentifier(fields[0].trim());
            modbusRow.setAddress(Integer.parseInt(fields[1].trim().replace("0x", ""), 16));
            modbusRow.setSize(Integer.parseInt(fields[2].trim()));
            modbusRow.setType(fields[3].trim());

            modbusRows.add(modbusRow);
        }

        return modbusRows;
    }
}
