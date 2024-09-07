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
public class KeyPairEntry
{

	/**
	 * The algorithm used for the key pair.
	 */
	@NonNull
	String algorithm;

	/**
	 * The key size for the key pair.
	 */
	Integer keySize;


	/**
	 * Creates a map of key-pair algorithms to sorted unique lists of corresponding signature
	 * algorithms
	 *
	 * @param entries
	 *            the list of {@link CertificateAlgorithmEntry} entries
	 * @return a map where the key is the key-pair algorithm and the value is a
	 *         {@link SortedUniqueList} of corresponding signature algorithms
	 */
	public static Map<String, SortedUniqueList<Integer>> newAlgorithmToKeySizeMap(
		List<KeyPairEntry> entries)
	{
		Map<String, SortedUniqueList<Integer>> algorithmMap = new LinkedHashMap<>();

		for (KeyPairEntry entry : entries)
		{
			String keyPairAlgorithm = entry.getAlgorithm();
			Integer entryKeySize = entry.getKeySize();

			algorithmMap.computeIfAbsent(keyPairAlgorithm, k -> new SortedUniqueList<>())
				.add(entryKeySize);
		}
		return algorithmMap;
	}
}
