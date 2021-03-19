package com.xumpy.modbusmeter.interfaces;

import java.util.Map;

public interface Processor {
    public void execute(Map<String, Object> modbusRows, String fileName);
}
