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
		if (!licenseToSave) {
			license = new License();
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

		try (LicenseWriter writer = new LicenseWriter(licenseName)) {
			writer.write(license, format);
			licenseToSave = false;
		}
	}

	// dump license to screen
	public String dumpLicense() throws IOException {
		if (license == null) {
			Logger.error("No license in memory. Please create or load a license");
			return "";
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); LicenseWriter lw = new LicenseWriter(baos)) {
			lw.write(license, IOFormat.STRING);
			return new String(baos.toByteArray(), StandardCharsets.UTF_8);
		}
	}

	// load an existing license
	public void loadLicense(File licenseFile, IOFormat format) throws IOException {
		if (licenseToSave) {
			Logger.warn("Unsaved license detected in memory. Please save the license first.");
			return;
		}

		try (LicenseReader reader = new LicenseReader(licenseFile.getName())) {
			license = reader.read(format);
			licenseToSave = false;

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
	}

	// will generate a private-key public-key pair and load it in memory
	private void generateKeys(String algorithm, int size) throws NoSuchAlgorithmException {
			keyPair = LicenseKeyPair.Create.from(algorithm, size);
	}

	// will save the loaded keys to file
	// uses the generateKeys() method internally

	public void generate(String algorithm, String sizeString, IOFormat format, String privateKeyFile, String publicKeyFile) throws IOException, NoSuchAlgorithmException {
		if (publicKeyFile.isEmpty() || privateKeyFile.isEmpty()) {
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
		try (KeyPairWriter writer = new KeyPairWriter(new File(privateKeyFile), new File(publicKeyFile))) {
			writer.write(keyPair, format);
			final String privateKeyPath = new File(privateKeyFile).getAbsolutePath();
			final String publicKeyPath = new File(publicKeyFile).getAbsolutePath();
			Logger.info("Private key saved to " + privateKeyPath);
			Logger.info("Public key saved to " + publicKeyPath);
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
		if (keyPair != null && keyPair.getPair() != null && keyPair.getPair().getPrivate() != null) {
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
		if (keyPair != null && keyPair.getPair() != null && keyPair.getPair().getPrivate() != null) {
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
		}
	}

	public void verifyLicense() {
		if (license == null) {
			Logger.error("No license loaded in memory to be verified.");
			return;
		}
		if (keyPair == null || keyPair.getPair() == null || keyPair.getPair().getPublic() == null) {
			Logger.error("No public key loaded in memory to be verified with.");
			return;
		}
		if (license.isOK(keyPair.getPair().getPublic())) {
			Logger.info("License is properly signed.");
		} else {
			Logger.warn("License is NOT properly signed.");
		}
	}

	// should not allow the app to exit if there is a license in memory waiting to
	// be saved
	public Boolean allowExit() {
		return !licenseToSave;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
		LicenseGeneration lg = new LicenseGeneration();
		lg.newLicense();
		lg.addFeature("licenseId:UUID=" + new HardwareBinder().getMachineId());
		lg.addFeature("licensedTo:STRING=Eggy");
		lg.addFeature("companyName:STRING=Wunkus");
		lg.addFeature("expiryDate:DATE=2028-04-17");
		Logger.info(lg.dumpLicense());
		lg.generate("RSA", "2048", IOFormat.BINARY, "eggpr.key", "eggpl.key");
		lg.signLicense();
		Logger.info(lg.digestPublicKey());
		lg.verifyLicense();
		lg.saveLicense("TestLicense.bin", IOFormat.BINARY);
		Logger.info(lg.allowExit());

	}

}
