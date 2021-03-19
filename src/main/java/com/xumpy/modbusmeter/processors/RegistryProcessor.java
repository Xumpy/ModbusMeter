package com.xumpy.modbusmeter.processors;

import com.ghgande.j2mod.modbus.procimg.Register;
import org.apache.commons.lang3.ArrayUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RegistryProcessor {
    protected static String registerToString(Register[] registers) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        for(Register register: registers){
            outputStream.write(register.toBytes());
        }
        return new String(outputStream.toByteArray(), "UTF-8").trim();
    }

    protected static Integer registerToInt(Register[] registers) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        for(Register register: registers){
            outputStream.write(register.toBytes());
        }

        return new BigInteger(outputStream.toByteArray()).intValue();
    }

    protected static Float registerToFloat(Register[] registers) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        for(Register register: registers){
            outputStream.write(register.toBytes());
        }

        return new BigInteger(outputStream.toByteArray()).floatValue();
    }

    protected static Float registerToFloatLittleEndian(Register[] registers) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        for(Register register: registers){
            byte[] bytes = register.toBytes();
            ArrayUtils.reverse(bytes);
            outputStream.write(bytes);
        }

        return ByteBuffer.wrap(outputStream.toByteArray()).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
}
