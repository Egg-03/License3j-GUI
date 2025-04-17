package logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javax0.license3j.Feature;
import javax0.license3j.HardwareBinder;
import javax0.license3j.License;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.KeyPairReader;
import javax0.license3j.io.KeyPairWriter;
import javax0.license3j.io.LicenseReader;
import javax0.license3j.io.LicenseWriter;

public class LicenseGeneration {

	private License license;
	private boolean licenseToSave = false;
	private LicenseKeyPair keyPair;
	
	// generate a new license if there are no previously unsaved licenses
	public void newLicense() {
		if(!licenseToSave) {
			license = new License();
		} else {
			// TODO give a warning that a license in memory needs to be saved first
		}
	}
	
	// save license to file
	public void saveLicense(String licenseName, IOFormat format) {
		if(license==null) {
			// TODO show a message that there is no license to save
			return;
		}
		
		try (LicenseWriter writer = new LicenseWriter(licenseName)) {
			writer.write(license, format);
			licenseToSave = false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	// dump license to screen
	public void dumpLicense() {
		if(license==null) {
			// TODO do something
			return;
		}
		
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream(); LicenseWriter lw = new LicenseWriter(baos)) {
			lw.write(license, IOFormat.STRING);
			System.out.println(new String(baos.toByteArray(), StandardCharsets.UTF_8));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// load an existing license
	public void loadLicense(File licenseFile, IOFormat format) {
		if(licenseToSave) {
			// TODO throw a warning message about unsaved licenses
			return;
		}
		
		try(LicenseReader reader = new LicenseReader(licenseFile.getName())) {
			license = reader.read(format);
			licenseToSave = false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	// add features to a license
	public void addFeature(String feature) {
		if (license == null) {
            // TODO show a message saying "Feature cannot be added when there is no license loaded. Use 'loadLicense' or 'newLicense'"
            return;
        }
		
        license.add(Feature.Create.from(feature));
        licenseToSave = true;
	}
	
	// will generate a private-key public-key pair and load it in memory
	private void generateKeys(String algorithm, int size) {
		try {
			keyPair = LicenseKeyPair.Create.from(algorithm, size);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Algorithm " + algorithm + " is not handled by the current version of this application.", e);
		}
	}
	
	// will save the loaded keys to file
	// uses the generateKeys() method internally
	
	public void generate(String algorithm, String sizeString, IOFormat format, String privateKeyFile, String publicKeyFile) {
		 if (publicKeyFile.isEmpty() || privateKeyFile.isEmpty()) {
	            //TODO keypair needs names of the public and private keys to be dumped
	            return;
	        }
		 
            final int size;
	        try {
	            size = Integer.parseInt(sizeString);
	        } catch (NumberFormatException e) {
	            //TODO Message: "Option size has to be a positive decimal integer value. " + sizeString + " does not qualify as such."
	            return;
	        }
	        
	        generateKeys(algorithm, size);
	        try (KeyPairWriter writer = new KeyPairWriter(new File(privateKeyFile), new File(publicKeyFile))) {
	            writer.write(keyPair, format);
	            final String privateKeyPath = new File(privateKeyFile).getAbsolutePath();
	            final String publicKeyPath = new File(publicKeyFile).getAbsolutePath();
	            System.out.println("Private key saved to " + privateKeyPath);
	            System.out.println("Public key saved to " + publicKeyPath);
	        } catch (IOException e) {
	        	// TODO 
	            e.printStackTrace();
	        }
	}
	
	private LicenseKeyPair merge(LicenseKeyPair oldKp, LicenseKeyPair newKp) {
        if (oldKp == null) {
            return newKp;
        }
        final String cipher = oldKp.cipher();
        if (newKp.getPair().getPublic() != null) {
            return LicenseKeyPair.Create.from(newKp.getPair().getPublic(), oldKp.getPair().getPrivate(), cipher);
        }
        if (newKp.getPair().getPrivate() != null) {
            return LicenseKeyPair.Create.from(oldKp.getPair().getPublic(), newKp.getPair().getPrivate(), cipher);
        }
        return oldKp;
    }
	
	// load private key
	public void loadPrivateKey(File keyFile, IOFormat format) {
		if(keyPair !=null && keyPair.getPair()!=null && keyPair.getPair().getPrivate()!=null) {
			// override the old key in memory with the new key from file
			// TODO show message about overriding
		}
		
		if(!keyFile.exists()) {
			return;
		}
		
		try(KeyPairReader kpread = new KeyPairReader(keyFile)) {
			keyPair = merge(keyPair, kpread.readPrivate(format));
			System.out.println("Private Key Loaded From: "+keyFile.getAbsolutePath());
			
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	// load public key
	
	public void loadPublicKey(File keyFile, IOFormat format) {
		if(keyPair !=null && keyPair.getPair()!=null && keyPair.getPair().getPrivate()!=null) {
			// override the old key in memory with the new key from file
			// TODO show message about overriding
		}
		
		if(!keyFile.exists()) {
			return;
		}
		
		try(KeyPairReader kpread = new KeyPairReader(keyFile)) {
			keyPair = merge(keyPair, kpread.readPublic(format));
			System.out.println("Private Key Loaded From: "+keyFile.getAbsolutePath());
			
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	// digest public key
	
	public void digestPublicKey() {
		
		if (keyPair == null) {
           // TODO show a message that no public key is loaded
            return;
        }
		
		try {
			byte[] publicKey = keyPair.getPublic();
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			byte[] calculatedDigest = digest.digest(publicKey);
			
			StringBuilder javaCode = new StringBuilder("--KEY DIGEST START\nbyte [] digest = new byte[] {\n");
            for (int i = 0; i < calculatedDigest.length; i++) {
                int intVal = (calculatedDigest[i]) & 0xff;
                javaCode.append(String.format("(byte)0x%02X, ", intVal));
                if (i % 8 == 0) {
                    javaCode.append("\n");
                }
            }
            javaCode.append("\n};\n---KEY DIGEST END\n");

            javaCode.append("--KEY START\nbyte [] key = new byte[] {\n");
            for (int i = 0; i < publicKey.length; i++) {
                int intVal = (publicKey[i]) & 0xff;
                javaCode.append(String.format("(byte)0x%02X, ", intVal));
                if (i % 8 == 0) {
                    javaCode.append("\n");
                }
            }
            javaCode.append("\n};\n---KEY END\n");
            
            System.out.println(javaCode.toString());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// sign license
	public void signLicense() {
		if (license == null) {
			// TODO show message to load or create a license
		} else {
			try {
				license.sign(keyPair.getPair().getPrivate(), "SHA-512");
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException| IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void verifyLicense() {
		if( license == null ){
			//TODO show message that there is no license to be verified
			return;
		}
		if( keyPair == null || keyPair.getPair() == null || keyPair.getPair().getPublic() == null ){
			//TODO There is no public key to verify the license with
			return;
		}
		if (license.isOK(keyPair.getPair().getPublic())) {
			System.out.println("License is properly signed.");
		} else {
			System.out.println("License is not properly signed.");
		}
	}
	
	// should not allow the app to exit if there is a license in memory waiting to be saved
	public Boolean allowExit() {
		return !licenseToSave;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, SocketException, UnknownHostException {
		LicenseGeneration lg = new LicenseGeneration();
		lg.newLicense();
		lg.addFeature("licid:STRING="+new HardwareBinder().getMachineIdString());
		lg.addFeature("name:STRING=Eggy");
		lg.dumpLicense();
		lg.generate("RSA", "2048", IOFormat.BINARY, "eggpr.key", "eggpl.key");
		lg.signLicense();
		lg.digestPublicKey();
		lg.verifyLicense();
		lg.saveLicense("TestLicense.bin", IOFormat.BINARY);
		System.out.println(lg.allowExit());

	}

}
