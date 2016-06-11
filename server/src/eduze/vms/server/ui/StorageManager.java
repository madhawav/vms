package eduze.vms.server.ui;
import eduze.vms.foundation.ui.CryptoUtil;
import eduze.vms.foundation.ui.StreamHelper;
import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.ServerImpl;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Admin on 6/1/2016.
 */
public class StorageManager {
    private static StorageManager instance = null;

    public static StorageManager getInstance() {
        if(instance == null)
            instance = new StorageManager();
        return instance;
    }

    private StorageManager(){}

    private File storageFile = null;

    private String encryptionKey = "password";

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public void init(File file)
    {
        storageFile = file;
    }

    public DataNode readStorage() throws CryptoUtil.CryptoException, StorageException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        CipherInputStream cis = null;
        try{
            fis = new FileInputStream(storageFile);
            cis = CryptoUtil.getInputStream(fis, encryptionKey, Cipher.DECRYPT_MODE);
            ois = new ObjectInputStream(cis);
            DataNode result = (DataNode) ois.readObject();
            return result;
        }
        catch (IOException e)
        {
            throw new StorageException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new StorageException(e);
        } finally {
            StreamHelper.safelyClose(ois);
            StreamHelper.safelyClose(cis);
            StreamHelper.safelyClose(fis);

            ois=null;
            cis=null;
            fis=null;
        }
    }


    public void updatePairedFacilitators(Collection<Facilitator> facilitators) throws CryptoUtil.CryptoException, StorageException {
        DataNode dataNode = readStorage();

        ArrayList<Facilitator> pairedFacilitators = new ArrayList<>();
        pairedFacilitators.addAll(facilitators);

        dataNode.setPairedFacilitators(pairedFacilitators);

        updateStorage(dataNode);

    }
    public void updateStorage(DataNode dataNode) throws StorageException, CryptoUtil.CryptoException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        CipherOutputStream cos = null;
        try{
            fos = new FileOutputStream(storageFile,false);
            cos = CryptoUtil.getOutputStream(fos,encryptionKey,Cipher.ENCRYPT_MODE);
            oos = new ObjectOutputStream(cos);
            oos.writeObject(dataNode);
            oos.flush();
            cos.flush();
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new StorageException(e);
        } catch (IOException  e) {
            e.printStackTrace();
            throw new StorageException(e);
        }
        finally {
            StreamHelper.safelyClose(oos);
            StreamHelper.safelyClose(cos);
            StreamHelper.safelyClose(fos);
            oos = null;
            cos = null;
            fos = null;
        }
    }

    public void saveDefaultConfiguration(String serverName, int port) throws CryptoUtil.CryptoException, StorageException {
        DataNode dataNode = new DataNode();
        dataNode.setPairedFacilitators(new ArrayList<Facilitator>());
        ServerImpl.Configuration configuration = new ServerImpl.Configuration();
        configuration.setName(serverName);
        configuration.setPort(port);
        dataNode.setConfiguration(configuration);
        updateStorage(dataNode);
    }

    public static class DataNode implements java.io.Serializable
    {

        private Collection<Facilitator> pairedFacilitators = null;
        public Collection<Facilitator> getPairedFacilitators()
        {
            return pairedFacilitators;
        }

        public void setPairedFacilitators(Collection<Facilitator> pairedFacilitators) {
            this.pairedFacilitators = pairedFacilitators;
        }

        private ServerImpl.Configuration configuration = null;

        public ServerImpl.Configuration getConfiguration() {
            return configuration;
        }

        public void setConfiguration(ServerImpl.Configuration configuration) {
            this.configuration = configuration;
        }
    }

    public static class StorageException extends Exception
    {
        public StorageException(Exception e)
        {
            super(e);
        }
    }
}
