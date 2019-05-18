package by.bsuir.oop.paint.action;

import org.apache.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;

public class Signer {

    private static final String KEY_ALGORITM = "DSA";
    private static final String SIGNATURE_ALGORITM = "SHA1withDSA";
    private static final String PROVIDER = "SUN";
    private static final String PUBLIC_KEY_PATH = "pub.txt";
    private static boolean isSigned;
    private static Signer instance;
    public int signedFileContentSize;
    private PrivateKey privateKey;
    private PublicKey  publicKey;
    private static final Logger LOGGER = Logger.getLogger(Signer.class);

    private Signer(boolean _isSigned) {
        isSigned = _isSigned;
        if (_isSigned){
            try (FileInputStream keyfis = new FileInputStream(PUBLIC_KEY_PATH);
                ObjectInputStream ois = new ObjectInputStream(keyfis)) {
                publicKey = (PublicKey) ois.readObject();
            } catch (IOException e){
                LOGGER.error(e);
            } catch (ClassNotFoundException e){
                LOGGER.error("Invalid public key: " + e);
            }
        }else {
            try {
                Files.deleteIfExists(Paths.get(PUBLIC_KEY_PATH));
            } catch (IOException e) {
                LOGGER.error(e);
            }
            try(FileOutputStream keyfos = new FileOutputStream(PUBLIC_KEY_PATH);
                ObjectOutputStream oos = new ObjectOutputStream(keyfos)) {
                createKeys();
                oos.writeObject(publicKey);
            }catch (IOException e){
                LOGGER.error(e);
            }catch (NoSuchAlgorithmException | NoSuchProviderException e){

            }
        }
    }

    public static Signer getInstance(boolean _isSigned){
        if (instance == null || isSigned != _isSigned){
            instance = new Signer(_isSigned);
        }
        return instance;
    }

    /**
     * Процедура генерирования закрытого и открытого ключей
     */
    private void createKeys() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITM, PROVIDER);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", PROVIDER);
        keyPairGenerator.initialize(1024, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey  = keyPair.getPublic();
    }

    private static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public void sign(String fpath){
        if (Files.isDirectory(Paths.get(fpath))){
            File dir = new File(fpath);
            String[] files = dir.list();
            if (files != null){
                for (String file : files){
                    sign(fpath + "\\" + file);
                }
            }
        }else try (FileInputStream fis = new FileInputStream(fpath);
             BufferedInputStream bufis = new BufferedInputStream(fis)){
            int fsize = fis.available();
            Signature dsa = Signature.getInstance(SIGNATURE_ALGORITM, PROVIDER);
            dsa.initSign(privateKey);
            byte[] buffer = new byte[fsize];
            bufis.read(buffer, 0, fsize);
            dsa.update(buffer, 0, fsize);
            Files.write(Paths.get(fpath), dsa.sign(), StandardOpenOption.APPEND);
            Files.write(Paths.get(fpath), intToByteArray(fsize), StandardOpenOption.APPEND);
        } catch (NoSuchProviderException | NoSuchAlgorithmException e){

        } catch (IOException | SignatureException | InvalidKeyException e){
            LOGGER.error(e);
        }
    }

    public boolean isOriginal(String fpath){
        try (RandomAccessFile raf = new RandomAccessFile(fpath, "r")){
            final int BYTES4FSIZE = 4;
            long temp;
            if ((temp = Files.size(Paths.get(fpath)) - BYTES4FSIZE) < 1){
                throw new SignatureException(fpath);
            }
            raf.seek(temp);
            int fsize = raf.readInt();
            if (fsize > Files.size(Paths.get(fpath))){
                throw new SignatureException(fpath);
            }
            raf.seek(0);
            byte[] buf = new byte[fsize];
            raf.read(buf, 0, fsize);
            if ((temp = raf.length() - fsize - BYTES4FSIZE) < 1){
                throw new SignatureException(fpath);
            }
            byte[] sign = new byte[(int)temp];
            raf.read(sign, 0, sign.length);
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITM, PROVIDER);
            sig.initVerify(publicKey);
            sig.update(buf, 0, fsize);
            if (sig.verify(sign)){
                signedFileContentSize = fsize;
                return true;
            } else {
                signedFileContentSize = 0;
                return false;
            }
        } catch (SignatureException | IOException | InvalidKeyException e){
            LOGGER.error(e);
            return false;
        } catch (NoSuchProviderException | NoSuchAlgorithmException e){
            return false;
        }
    }

    public static void main(String[] args){
        Signer signer = Signer.getInstance(false);
        signer.sign("F:\\modules\\shapes");
    }
}
