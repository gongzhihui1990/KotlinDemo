package com.bus.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    final String KEY_ALGORITHM = "AES";
    final String algorithmStr = "AES/CBC/NoPadding";
    boolean isInited = false;
    private Cipher cipher;
    private Key key;

    public static byte[] paddingContent(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length % 16 != 0) {
            int j = paramArrayOfByte.length % 16;
            byte[] arrayOfByte1 = new byte[paramArrayOfByte.length + (16 - j)];
            System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 0, paramArrayOfByte.length);
            byte[] arrayOfByte2 = new byte[16 - j];
            for (int i = 0; i < 16 - j; i++) {
                arrayOfByte2[i] = ((byte) (16 - j));
            }
            System.arraycopy(arrayOfByte2, 0, arrayOfByte1, paramArrayOfByte.length, arrayOfByte2.length);
            paramArrayOfByte = arrayOfByte1;
        }
        return paramArrayOfByte;
    }

    public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        byte[] arrayOfByte1 = paddingContent(paramArrayOfByte1);
        paramArrayOfByte1 = null;
        init(paramArrayOfByte2);
        try {
            Cipher localCipher = this.cipher;
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(DataConstant.IV.getBytes());
            localCipher.init(2, this.key, localIvParameterSpec);
            paramArrayOfByte1 = this.cipher.doFinal(arrayOfByte1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramArrayOfByte1;
    }

    public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        byte[] arrayOfByte1 = paddingContent(paramArrayOfByte1);
        paramArrayOfByte1 = null;
        init(paramArrayOfByte2);
        try {
            Cipher localCipher = this.cipher;
            Key localKey = this.key;
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(DataConstant.IV.getBytes());
            localCipher.init(1, localKey, localIvParameterSpec);
            paramArrayOfByte2 = this.cipher.doFinal(arrayOfByte1);
            paramArrayOfByte1 = paramArrayOfByte2;
        } catch (Exception e) {
                e.printStackTrace();
        }
        return paramArrayOfByte1;
    }

    public void init(byte[] paramArrayOfByte) {
        Security.addProvider(new BouncyCastleProvider());
        this.key = new SecretKeySpec(paramArrayOfByte, "AES");
        try {
            this.cipher = Cipher.getInstance("AES/CBC/NoPadding", "BC");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
