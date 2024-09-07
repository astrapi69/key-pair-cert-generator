/**
 * The MIT License
 *
 * Copyright (C) 2024 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.key.pair.generator.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import io.github.astrapi69.key.pair.generator.model.CertificateAlgorithmEntry;
import io.github.astrapi69.key.pair.generator.model.KeyPairEntry;


/**
 * The {@code CsvExtensions} class provides utility methods to read data from CSV files, map the
 * data to objects, and generate maps of data from lists of objects. This class supports reading CSV
 * files using Apache Commons CSV and creating data structures like lists and maps from the parsed
 * CSV data
 */
public class CsvExtensions
{

	/**
	 * Reads entries from a CSV input stream, maps them using a provided function, and returns a
	 * list of the mapped entries
	 *
	 * @param <T>
	 *            the generic type of the mapped entries
	 * @param csvInputStream
	 *            the input stream containing the CSV data
	 * @param recordMapper
	 *            a function to map each CSV record to an object of type T
	 * @return a list of mapped entries of type T
	 * @throws IOException
	 *             if an I/O error occurs during reading
	 */
	public static <T> List<T> readEntriesFromCsv(InputStream csvInputStream,
		Function<CSVRecord, T> recordMapper) throws IOException
	{
		List<T> entries = new ArrayList<>();

		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
			.build();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream));
			CSVParser csvParser = new CSVParser(reader, csvFormat))
		{
			for (CSVRecord csvRecord : csvParser)
			{
				T entry = recordMapper.apply(csvRecord);
				entries.add(entry);
			}
		}
		return entries;
	}

	/**
	 * Reads {@link CertificateAlgorithmEntry} objects from a CSV input stream
	 *
	 * @param csvFile
	 *            the input stream containing the CSV data
	 * @return a list of {@link CertificateAlgorithmEntry} objects
	 * @throws IOException
	 *             if an I/O error occurs during reading
	 */
	public static List<CertificateAlgorithmEntry> readCertificateAlgorithmEntryFromCsv(
		InputStream csvFile) throws IOException
	{
		return readEntriesFromCsv(csvFile, csvRecord -> {
			String algorithm = csvRecord.get("keypair-algorithm");
			String signatureAlgorithm = csvRecord.get("signature-algorithm");

			return CertificateAlgorithmEntry.builder().keyPairAlgorithm(algorithm)
				.signatureAlgorithm(signatureAlgorithm).build();
		});
	}

	/**
	 * Reads key-pair entries from a CSV file and returns a list of {@link KeyPairEntry}
	 *
	 * @param csvFile
	 *            the CSV file to read from
	 * @return a list of {@link KeyPairEntry}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static List<KeyPairEntry> readKeyPairEntriesFromCsv(InputStream csvFile)
		throws IOException
	{
		return readEntriesFromCsv(csvFile, csvRecord -> {
			String algorithm = csvRecord.get("algorithm");
			String keySizeString = csvRecord.get("keysize");
			Integer keySize = keySizeString.isEmpty() ? null : Integer.valueOf(keySizeString);

			return KeyPairEntry.builder().algorithm(algorithm).keySize(keySize).build();
		});
	}

}
