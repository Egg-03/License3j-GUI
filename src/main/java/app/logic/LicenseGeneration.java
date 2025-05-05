package app.logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.tinylog.Logger;

import javax0.license3j.Feature;
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
	private boolean licenseToSign = false;
	private LicenseKeyPair keyPair;

	// generate a new license if there are no previously unsaved licenses
	public void newLicense() {
		if (!licenseToSave) {
			license = new License();
			licenseToSign = true;
			Logger.info("A new license is generated in memory");
		} else {
			Logger.warn("An unsaved license is detected in memory. Please save the license first.");
		}
	}

	// save license to file
	public void saveLicense(String licenseName, IOFormat format) throws IOException {
		if (license == null) {
			Logger.error("No license in memory. Please create or load a license");
			return;
		} 
		
		if(licenseToSign) {
			Logger.error("License needs to be signed before saving");
			return;
		}
		
		File licenseFolder = new File("Licenses");
		if(!licenseFolder.isDirectory()) {
			Logger.info("'Licenses' directory created in the working directory with status "+licenseFolder.mkdir());
		}
		try (LicenseWriter writer = new LicenseWriter(licenseFolder+File.separator+licenseName)) {
			writer.write(license, format);
			licenseToSave = false;
			Logger.info("License saved to "+licenseFolder.getCanonicalPath());
		}
	}

	// dump license to screen
	public String displayLicense() throws IOException {
		if (license == null) {
			Logger.error("No license in memory. Please create or load a license");
			return "";
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); LicenseWriter lw = new LicenseWriter(baos)) {
			lw.write(license, IOFormat.STRING);
			return baos.toString(StandardCharsets.UTF_8);
		}
	}

	// load an existing license
	public void loadLicense(File licenseFile, IOFormat format) throws IOException {
		if (licenseToSave) {
			Logger.warn("Unsaved license detected in memory. Please save the license first.");
			return;
		}

		try (LicenseReader reader = new LicenseReader(licenseFile)) {
			license = reader.read(format);
			licenseToSave = false;
			licenseToSign = false;
			Logger.info(licenseFile.getName()+" is loaded in memory.");
		}
	}

	// add features to a license
	public void addFeature(String feature) {
		if (license == null) {
			Logger.error("No license in memory. Feature cannot be added. Create or load a license.");
			return;
		}

		license.add(Feature.Create.from(feature));
		licenseToSave = true;
		licenseToSign = true;
		Logger.info("Feature: "+feature+" has been added to your license. Please sign the license again before saving.");
	}

	// will generate a private-key public-key pair and load it in memory
	private void generateKeys(String algorithm, int size) throws NoSuchAlgorithmException {
			keyPair = LicenseKeyPair.Create.from(algorithm, size);
			Logger.info("Private and Public Keys loaded in memory");
	}

	// will save the loaded keys to file
	// uses the generateKeys() method internally

	public void generate(String algorithm, String sizeString, IOFormat format, String privateKeyFile, String publicKeyFile) throws IOException, NoSuchAlgorithmException {
		if (publicKeyFile.isEmpty() || privateKeyFile.isEmpty() || publicKeyFile.isBlank() || privateKeyFile.isBlank()) {
			Logger.error("KeyPair needs the names of the keys.");
			return;
		}

		final int size;
		try {
			size = Integer.parseInt(sizeString);
		} catch (NumberFormatException e) {
			Logger.error(sizeString+" has to be a positive decimal integer value.");
			return;
		}

		generateKeys(algorithm, size);
		
		File keyFolder = new File("Keys");
		if(!keyFolder.isDirectory()) {
			Logger.info("'Key' folder created with success state "+keyFolder.mkdir());
		}
		try (KeyPairWriter writer = new KeyPairWriter(new File(keyFolder+File.separator+privateKeyFile), new File(keyFolder+File.separator+publicKeyFile))) {
			writer.write(keyPair, format);
			Logger.info("Keys saved to: "+keyFolder.getCanonicalPath());
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
	public void loadPrivateKey(File keyFile, IOFormat format) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		if (Boolean.TRUE.equals(isPrivateKeyLoaded())) {
			Logger.info("Private Key in memory will be overriden by a new key loaded from a file.");
		}

		if (!keyFile.exists()) {
			Logger.error("Private Key file does not exist.");
			return;
		}

		try (KeyPairReader kpread = new KeyPairReader(keyFile)) {
			keyPair = merge(keyPair, kpread.readPrivate(format));
			Logger.info("Private Key Loaded From: " + keyFile.getAbsolutePath());
		}
	}

	// load public key
	public void loadPublicKey(File keyFile, IOFormat format) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		if (Boolean.TRUE.equals(isPublicKeyLoaded())) {
			Logger.info("Public Key in memory will be overriden by a new key loaded from a file.");
		}

		if (!keyFile.exists()) {
			Logger.error("Public Key file does not exist.");
			return;
		}

		try (KeyPairReader kpread = new KeyPairReader(keyFile)) {
			keyPair = merge(keyPair, kpread.readPublic(format));
			Logger.info("Public Key Loaded From: " + keyFile.getAbsolutePath());

		}
	}

	// digest public key

	public String digestPublicKey() throws NoSuchAlgorithmException {

		if (keyPair == null) {
			Logger.error("No digestable public key loaded.");
			return "";
		}

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

		return javaCode.toString();

	}

	// sign license
	public void signLicense() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
		if (license == null) {
			Logger.error("No license detected in memory. Load or create a license.");
		} else {
			license.sign(keyPair.getPair().getPrivate(), "SHA-512");
			Logger.info("License Signed. Please save before closing the app");
			licenseToSave = true;
            licenseToSign = false;
		}
	}

	public void verifyLicense() {
		if (license == null) {
			Logger.error("No license loaded in memory to be verified.");
			return;
		}
		if (Boolean.FALSE.equals(isPublicKeyLoaded())) {
			Logger.error("No public key loaded in memory to be verified with.");
			return;
		}
		if (license.isOK(keyPair.getPair().getPublic())) {
			Logger.info("License is properly signed.");
		} else {
			Logger.warn("License is NOT properly signed.");
		}
	}

	// extra functions
	public Boolean isLicenseLoaded() {
		return license != null;
	}
	
	public Boolean licenseRequiresSaving() {
		return licenseToSave;
	}
	
	public Boolean licenseRequiresSigning() {
		return licenseToSign;
	}
	
	public Boolean isPrivateKeyLoaded() {
		return (keyPair != null && keyPair.getPair() != null && keyPair.getPair().getPrivate() != null);
	}
	
	public Boolean isPublicKeyLoaded() {
		return (keyPair != null && keyPair.getPair() != null && keyPair.getPair().getPublic() != null);
	}
}
