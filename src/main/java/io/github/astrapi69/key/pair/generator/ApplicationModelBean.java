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
package io.github.astrapi69.key.pair.generator;

import java.io.Serializable;
import java.util.Map;

import io.github.astrapi69.collection.list.SortedUniqueList;
import io.github.astrapi69.key.pair.generator.keygen.GenerateKeysModelBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link ApplicationModelBean} holds application-specific data
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationModelBean implements Serializable
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	boolean showSplash;
	boolean signedIn;
	/** The flag that indicated that the application model changed */
	boolean dirty;
	Long lastId;
	GenerateKeysModelBean generateKeysModelBean;
	Map<String, SortedUniqueList<String>> keypairSignatureAlgorithms;
	Map<String, SortedUniqueList<Integer>> keypairAlgorithmToKeySizes;
}
