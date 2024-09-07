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
package io.github.astrapi69.key.pair.generator.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.astrapi69.collection.list.SortedUniqueList;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CertificateAlgorithmEntry
{

	/**
	 * The algorithm used for the key pair
	 */
	@NonNull
	String keyPairAlgorithm;

	/**
	 * The signature algorithm used to sign the certificate
	 */
	@NonNull
	String signatureAlgorithm;


	/**
	 * Creates a map of key-pair algorithms to sorted unique lists of corresponding signature
	 * algorithms
	 *
	 * @param entries
	 *            the list of {@link CertificateAlgorithmEntry} entries
	 * @return a map where the key is the key-pair algorithm and the value is a
	 *         {@link SortedUniqueList} of corresponding signature algorithms
	 */
	public static Map<String, SortedUniqueList<String>> newSignatureAlgorithmMap(
		List<CertificateAlgorithmEntry> entries)
	{
		Map<String, SortedUniqueList<String>> algorithmMap = new LinkedHashMap<>();

		for (CertificateAlgorithmEntry entry : entries)
		{
			String keyPairAlgorithm = entry.getKeyPairAlgorithm();
			String signatureAlgorithm = entry.getSignatureAlgorithm();

			algorithmMap.computeIfAbsent(keyPairAlgorithm, k -> new SortedUniqueList<>())
				.add(signatureAlgorithm);
		}
		return algorithmMap;
	}
}
