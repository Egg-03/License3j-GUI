package app.ui.primary;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.tinylog.Logger;

import app.logic.LicenseGeneration;
import javax0.license3j.io.IOFormat;

class AppSwingWorker {
	
	private AppSwingWorker() {
		throw new IllegalStateException("Utility Class");
	}
}

class NewLicense extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	
	protected NewLicense(LicenseGeneration lg) {
		this.lg=lg;
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.newLicense();
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			get();
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class LoadLicense extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	private final IOFormat licenseType;
	private final File licenseFile;
	
	protected LoadLicense(LicenseGeneration lg, IOFormat licenseType, File licenseFile) {
		this.lg=lg;
		this.licenseType=licenseType;
		this.licenseFile=licenseFile;
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.loadLicense(licenseFile, licenseType);
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			get();	
		} catch (ExecutionException e) {	
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class DisplayLicense extends SwingWorker<String, Void> {
	
	private final LicenseGeneration lg;
	
	protected DisplayLicense(LicenseGeneration lg) {
		this.lg=lg;	
	}

	@Override
	protected String doInBackground() throws Exception {
		return lg.displayLicense();
	}
	
	@Override
	protected void done() {		
		try {
			Logger.info(get());
		} catch (ExecutionException e) {		
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class SignLicense extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	
	protected SignLicense(LicenseGeneration lg) {
		this.lg=lg;	
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.signLicense();
		return null;
	}
	
	@Override
	protected void done() {		
		try {
			get();
		} catch (ExecutionException e) {		
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class VerifyLicense extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	
	protected VerifyLicense(LicenseGeneration lg) {
		this.lg=lg;	
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.verifyLicense();
		return null;
	}
	
	@Override
	protected void done() {		
		try {
			get();
		} catch (ExecutionException e) {		
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class SaveLicense extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	private final IOFormat licenseType;
	private final String licenseName;
	
	protected SaveLicense(LicenseGeneration lg, String licenseName, IOFormat licenseType) {
		this.lg=lg;
		this.licenseType=licenseType;
		if(licenseName.isBlank())
			this.licenseName="defaultLicense"+licenseType;
		else
			this.licenseName = licenseName;
		
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.saveLicense(licenseName, licenseType);
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			get();
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class AddFeatureToLicense extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	private final String featureName;
	private final String featureType;
	private final String featureContent;
	
	
	protected AddFeatureToLicense(LicenseGeneration lg, String featureName, String featureType, String featureContent) {
		this.lg=lg;
		this.featureName=featureName;
		this.featureType=featureType;
		this.featureContent=featureContent;
	}

	@Override
	protected Void doInBackground() throws Exception {
		if(featureName.isBlank() || featureContent.isBlank()) {
			return null;
		}
		lg.addFeature(featureName+":"+featureType+"="+featureContent);
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			get();
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class GenerateKeys extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	private final String algorithm;
	private final String size;
	private final IOFormat keyFormat;
	private final String privateKeyName;
	private final String publicKeyName;
	
	
	protected GenerateKeys(LicenseGeneration lg, String algorithm, String size, IOFormat keyFormat, String privateKeyName, String publicKeyName) {
		this.lg=lg;
		this.algorithm=algorithm;
		this.size=size;
		this.keyFormat = keyFormat;
		this.privateKeyName=privateKeyName;
		this.publicKeyName=publicKeyName;
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.generate(algorithm, size, keyFormat, privateKeyName, publicKeyName);
		return null;
	}
	
	@Override
	protected void done() {	
		try {
			get();
			
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class LoadPrivateKey extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	private final IOFormat keyFormat;
	private final File keyFile;
	
	protected LoadPrivateKey(LicenseGeneration lg, IOFormat keyFormat, File keyFile) {
		this.lg=lg;
		this.keyFormat=keyFormat;
		this.keyFile=keyFile;
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.loadPrivateKey(keyFile, keyFormat);
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			get();		
		} catch (ExecutionException e) {		
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class LoadPublicKey extends SwingWorker<Void, Void> {
	
	private final LicenseGeneration lg;
	private final IOFormat keyFormat;
	private final File keyFile;
	
	protected LoadPublicKey(LicenseGeneration lg, IOFormat keyFormat,  File keyFile) {
		this.lg=lg;
		this.keyFormat=keyFormat;	
		this.keyFile=keyFile;
	}

	@Override
	protected Void doInBackground() throws Exception {
		lg.loadPublicKey(keyFile, keyFormat);
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			get();		
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {		
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}

class DigestPublicKey extends SwingWorker<String, Void> {
	
	private final LicenseGeneration lg;
	
	
	protected DigestPublicKey(LicenseGeneration lg) {
		this.lg=lg;	
	}

	@Override
	protected String doInBackground() throws Exception {
		return lg.digestPublicKey();
	}
	
	@Override
	protected void done() {
		
		try {
			Logger.info(get());
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}
