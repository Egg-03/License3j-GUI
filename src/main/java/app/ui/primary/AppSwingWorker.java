package app.ui.primary;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.tinylog.Logger;

import app.logic.LicenseGeneration;
import javax0.license3j.io.IOFormat;

class AppSwingWorker {
	
	public AppSwingWorker() {
		throw new IllegalStateException("Utility Class");
	}
}

class NewLicense extends SwingWorker<Void, Void> {
	
	private LicenseGeneration lg;
	
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
	
	private LicenseGeneration lg;
	private IOFormat licenseType;
	private File licenseFile;
	
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
	
	private LicenseGeneration lg;
	
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
	
	private LicenseGeneration lg;
	
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
	
	private LicenseGeneration lg;
	
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
	
	private LicenseGeneration lg;
	private IOFormat licenseType;
	private String licenseName;
	
	protected SaveLicense(LicenseGeneration lg, String licenseName, IOFormat licenseType) {
		this.lg=lg;
		this.licenseType=licenseType;
		if(licenseName.isBlank() || licenseName.isEmpty())
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
	
	private LicenseGeneration lg;
	private String featureName;
	private String featureType;
	private String featureContent;
	
	
	protected AddFeatureToLicense(LicenseGeneration lg, String featureName, String featureType, String featureContent) {
		this.lg=lg;
		this.featureName=featureName;
		this.featureType=featureType;
		this.featureContent=featureContent;
	}

	@Override
	protected Void doInBackground() throws Exception {
		if(featureName.isBlank()|| featureName.isEmpty() ||  featureContent.isBlank() || featureContent.isEmpty()) {
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
	
	private LicenseGeneration lg;
	private String algorithm;
	private String size;
	private IOFormat keyFormat;
	private String privateKeyName;
	private String publicKeyName;
	
	
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
	
	private LicenseGeneration lg;
	private IOFormat keyFormat;
	private File keyFile;
	
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
	
	private LicenseGeneration lg;
	private IOFormat keyFormat;
	private File keyFile;
	
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
	
	private LicenseGeneration lg;
	
	
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

class LiveStatus extends SwingWorker<List<Boolean>, Void> {
	private LicenseGeneration lg;
	private JTable t;
	
	protected LiveStatus(LicenseGeneration lg, JTable table) {
		this.lg=lg;
		this.t=table;
	}

	@Override
	protected List<Boolean> doInBackground() throws Exception {
		return List.of(lg.isLicenseLoaded(), lg.licensePendingSaveStatus(), lg.isPrivateKeyLoaded(), lg.isPublicKeyLoaded());
	}
	
	@Override
	protected void done() {
		try {
			List<Boolean> statusList = get();
			for(int i=0; i<t.getRowCount(); i++) {
				t.getModel().setValueAt(statusList.get(i), i, 1);
			}
			
		} catch (ExecutionException e) {
			Logger.error(e);
		} catch (InterruptedException e) {
			Logger.error(e);
			Thread.currentThread().interrupt();
		}
	}
}
