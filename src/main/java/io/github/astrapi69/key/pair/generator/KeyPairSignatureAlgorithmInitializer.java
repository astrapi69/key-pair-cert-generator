package io.github.astrapi69.key.pair.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import io.github.astrapi69.collection.list.SortedUniqueList;
import io.github.astrapi69.key.pair.generator.model.CertificateAlgorithmEntry;
import io.github.astrapi69.key.pair.generator.extension.CsvExtensions;
import io.github.astrapi69.lang.ClassExtensions;
import io.github.astrapi69.swing.dialog.DialogExtensions;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public class KeyPairSignatureAlgorithmInitializer implements Runnable
{

	final ApplicationModelBean applicationModelBean;
	@Getter
	boolean finished = false;

	public KeyPairSignatureAlgorithmInitializer(final ApplicationModelBean applicationModelBean)
	{
		this.applicationModelBean = applicationModelBean;
	}

	@Override
	public void run()
	{
		String csvFilePath;
		InputStream is;
		csvFilePath = "valid_jdk_17_provider_bc_certificate_signature_algorithms.csv";
		is = ClassExtensions.getResourceAsStream(csvFilePath);
		try
		{
			List<CertificateAlgorithmEntry> certificateAlgorithmEntries = CsvExtensions
				.readCertificateAlgorithmEntryFromCsv(is);

			Map<String, SortedUniqueList<String>> uniqueListMap = CertificateAlgorithmEntry
				.newSignatureAlgorithmMap(certificateAlgorithmEntries);
			this.applicationModelBean.setKeypairSignatureAlgorithms(uniqueListMap);
			finished = true;
			log.log(new LogRecord(Level.INFO, "key pair signature algorithms initialized"));
		}

		catch (IOException exception)
		{
			log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
			String title = "Creation of key pair signature algorithms map failed";
			String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
				+ "<p> The creation of key pair signature algorithms map failed" + "<p>"
				+ exception.getMessage();

			DialogExtensions.showExceptionDialog(exception, WizardApplicationFrame.getInstance());
			throw new RuntimeException(title + "::" + htmlMessage, exception);
		}
	}
}
