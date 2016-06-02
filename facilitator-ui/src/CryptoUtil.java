import eduze.vms.facilitator.logic.FacilitatorController;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Admin on 6/1/2016.
 */
public class CryptoUtil {

    private static SecretKeySpec generateKeySpec(String password) throws CryptoException {
        try{
            byte[] key = ("123124"+ password).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            return secretKeySpec;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        }

    }
    public static CipherOutputStream getOutputStream(OutputStream target, String password, int encryptMode) throws CryptoException
    {
        try{
            final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                    0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }; //example

            final SecretKey key = generateKeySpec(password);
            final IvParameterSpec IV = new IvParameterSpec(ivBytes);
            final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(encryptMode, key, IV);
            return new CipherOutputStream(target, cipher);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        }
    }


    public static CipherInputStream getInputStream(InputStream source, String password, int encryptMode) throws CryptoException {

        try
        {
            final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                    0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }; //example

            final SecretKey key = generateKeySpec(password);
            final IvParameterSpec IV = new IvParameterSpec(ivBytes);
            final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(encryptMode, key, IV);
            return new CipherInputStream(source, cipher);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        }


    }

    public static void main(String[] args) throws CryptoException
    {
        CipherOutputStream cos = null;
        CipherInputStream cis = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        try {
            bos= new ByteArrayOutputStream();


            cos = getOutputStream(bos,"password", Cipher.ENCRYPT_MODE);

            ObjectOutputStream oos = new ObjectOutputStream(cos);
            oos.writeObject(new FacilitatorController.Configuration());
            oos.close();
            cos.close();

            byte[] data = bos.toByteArray();
            bis = new ByteArrayInputStream(data);
            cis = getInputStream(bis,"password",Cipher.DECRYPT_MODE);
            ObjectInputStream ois = new ObjectInputStream(cis);
            Object o = ois.readObject();
            System.out.println(o);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CryptoException(e);
        }
        finally {
            StreamHelper.safelyClose(cis);
            cis = null;
            StreamHelper.safelyClose(cos);
            cos = null;
            StreamHelper.safelyClose(bos);
            bos = null;
            StreamHelper.safelyClose(bis);
            bis = null;

        }

    }

    public static class CryptoException extends Exception
    {
        public CryptoException(Exception e)
        {
            super(e);
        }
    }
}