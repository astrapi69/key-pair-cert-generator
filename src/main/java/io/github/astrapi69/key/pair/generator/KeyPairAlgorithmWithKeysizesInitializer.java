package io.github.astrapi69.key.pair.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import io.github.astrapi69.collection.list.SortedUniqueList;
import io.github.astrapi69.key.pair.generator.extension.CsvExtensions;
import io.github.astrapi69.key.pair.generator.model.KeyPairEntry;
import io.github.astrapi69.lang.ClassExtensions;
import io.github.astrapi69.swing.dialog.DialogExtensions;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public class KeyPairAlgorithmWithKeysizesInitializer implements Runnable
{

	final ApplicationModelBean applicationModelBean;
	@Getter
	boolean finished = false;

	public KeyPairAlgorithmWithKeysizesInitializer(final ApplicationModelBean applicationModelBean)
	{
		this.applicationModelBean = applicationModelBean;
	}

	@Override
	public void run()
	{
		String csvFilePath;
		InputStream is;
		csvFilePath = "key_pair_algorithm_with_key_size.csv";
		is = ClassExtensions.getResourceAsStream(csvFilePath);
		try
		{
			List<KeyPairEntry> certificateAlgorithmEntries = CsvExtensions
				.readKeyPairEntriesFromCsv(is);

			Map<String, SortedUniqueList<Integer>> uniqueListMap = KeyPairEntry
				.newAlgorithmToKeySizeMap(certificateAlgorithmEntries);
			this.applicationModelBean.setKeypairAlgorithmToKeySizes(uniqueListMap);
			finished = true;
			log.log(new LogRecord(Level.INFO, "key sizes initialized"));
		}
		catch (IOException exception)
		{
			log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
			String title = "Creation of key pair algorithms map failed";
			String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
				+ "<p> The creation of key pair algorithms map failed" + "<p>"
				+ exception.getMessage();

			DialogExtensions.showExceptionDialog(exception, WizardApplicationFrame.getInstance());
			throw new RuntimeException(title + "::" + htmlMessage, exception);
		}
	}
}
