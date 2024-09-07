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
package io.github.astrapi69.key.pair.generator.keygen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import io.github.astrapi69.key.pair.generator.WizardApplicationFrame;
import io.github.astrapi69.crypt.data.factory.KeyPairFactory;
import io.github.astrapi69.crypt.data.factory.KeyPairGeneratorFactory;
import io.github.astrapi69.crypt.data.key.PrivateKeyExtensions;
import io.github.astrapi69.crypt.data.key.PublicKeyExtensions;
import io.github.astrapi69.crypt.data.key.writer.EncryptedPrivateKeyWriter;
import io.github.astrapi69.crypt.data.key.writer.PrivateKeyWriter;
import io.github.astrapi69.crypt.data.key.writer.PublicKeyWriter;
import io.github.astrapi69.model.BaseModel;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.swing.base.BasePanel;
import io.github.astrapi69.swing.dialog.DialogExtensions;
import lombok.Getter;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;

@Getter
@Log
public class GenerateKeysPanel extends BasePanel<GenerateKeysModelBean>
{

	private static final long serialVersionUID = 1L;

	private CryptographyPanel cryptographyPanel;

	public GenerateKeysPanel()
	{
		this(BaseModel.of(GenerateKeysModelBean.builder().build()));
	}

	public GenerateKeysPanel(final IModel<GenerateKeysModelBean> model)
	{
		super(model);
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on change key
	 * size.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	@SuppressWarnings("unchecked")
	protected void onChangeKeySize(final ActionEvent actionEvent)
	{
		final JComboBox<String> cb = (JComboBox<String>)actionEvent.getSource();
		final Integer selected = (Integer)cb.getSelectedItem();
		getModelObject().setKeySize(selected);
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on clear.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onClear(final ActionEvent actionEvent)
	{
		getCryptographyPanel().getCmbAlgorithm().setSelectedItem("RSA");
		getCryptographyPanel().getCmbKeySize().setSelectedItem(2048);
		getCryptographyPanel().getTxtPrivateKey().setText("");
		getCryptographyPanel().getTxtPublicKey().setText("");
		getCryptographyPanel().getBtnSaveCertificate().setEnabled(false);
		getModelObject().setDecryptor(null);
		getModelObject().setEncryptor(null);
		getModelObject().setAlgorithm("RSA");
		getModelObject().setKeySize(2048);
		getModelObject().setPrivateKey(null);
		getModelObject().setPublicKey(null);
	}

	// callbacks
	/**
	 * Callback method that can be overwritten to provide specific action for the on generate.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onGenerate(final ActionEvent actionEvent)
	{
		final String selectedAlgorithm = getCryptographyPanel().getCmbAlgorithm().getPropertyModel()
			.getObject();
		Integer selectedKeySize = (Integer)getCryptographyPanel().getCmbKeySize().getSelectedItem();
		getCryptographyPanel().getTxtPrivateKey().setText("Generating private key...");
		getCryptographyPanel().getTxtPublicKey().setText("Generating public key...");
		Map<String, String> algorithmToKeySize = new HashMap<>();
		algorithmToKeySize.put("ECGOST3410", "GostR3410-2001-CryptoPro-A");
		algorithmToKeySize.put("ECGOST3410-2012", "GostR3410-2001-CryptoPro-A");
		try
		{
			final KeyPair keyPair;
			Security.addProvider(new BouncyCastleProvider());
			if (selectedKeySize != null)
			{
				keyPair = KeyPairFactory.newKeyPair(selectedAlgorithm, selectedKeySize);
			}
			else
			{
				KeyPairGenerator keyPairGenerator = KeyPairGeneratorFactory
					.newKeyPairGenerator(selectedAlgorithm);
				if (algorithmToKeySize.containsKey(selectedAlgorithm))
				{
					String stdName = algorithmToKeySize.get(selectedAlgorithm);
					keyPairGenerator.initialize(new ECGenParameterSpec(stdName));
				}
				keyPair = keyPairGenerator.generateKeyPair();
			}
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			getModelObject().setPrivateKey(privateKey);
			getModelObject().setPublicKey(publicKey);

			final String privateKeyFormat = PrivateKeyExtensions
				.toPemFormat(getModelObject().getPrivateKey());

			final String publicKeyFormat = PublicKeyExtensions
				.toPemFormat(getModelObject().getPublicKey());

			getCryptographyPanel().getTxtPrivateKey().setText("");
			getCryptographyPanel().getTxtPublicKey().setText("");
			getCryptographyPanel().getTxtPrivateKey().setText(privateKeyFormat);
			getCryptographyPanel().getTxtPublicKey().setText(publicKeyFormat);
			getCryptographyPanel().getBtnSaveCertificate().setEnabled(true);
		}
		catch (final NoSuchAlgorithmException | NoSuchProviderException
			| InvalidAlgorithmParameterException | IOException exception)
		{
			log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
			String title = "Creation of key pair failed";
			String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
				+ "<p> The creation of key pair failed" + "<p>" + exception.getMessage();

			DialogExtensions.showExceptionDialog(exception, WizardApplicationFrame.getInstance());
			throw new RuntimeException(title + "::" + htmlMessage, exception);

		}

	}

	public boolean isNumeric(String s)
	{
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();

		cryptographyPanel = new CryptographyPanel(getModel())
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onChangeKeySize(final ActionEvent actionEvent)
			{
				GenerateKeysPanel.this.onChangeKeySize(actionEvent);
			}

			@Override
			protected void onClear(final ActionEvent actionEvent)
			{
				GenerateKeysPanel.this.onClear(actionEvent);
			}

			@Override
			protected void onGenerate(final ActionEvent actionEvent)
			{
				GenerateKeysPanel.this.onGenerate(actionEvent);
			}

			@Override
			protected void onSavePrivateKey(final ActionEvent actionEvent)
			{
				GenerateKeysPanel.this.onSavePrivateKey(actionEvent);
			}

			@Override
			protected void onSavePrivateKeyWithPassword(final ActionEvent actionEvent)
			{
				GenerateKeysPanel.this.onSavePrivateKeyWithPassword(actionEvent);
			}

			@Override
			protected void onSavePublicKey(final ActionEvent actionEvent)
			{
				GenerateKeysPanel.this.onSavePublicKey(actionEvent);
			}
		};

	}


	@Override
	protected void onInitializeLayout()
	{
		super.onInitializeLayout();
		// onInitializeMigLayout();
		onInitializeGridBagLayout();
	}

	protected void onInitializeGridBagLayout()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Padding around each component

		// Algorithm label and combobox
		gbc.gridx = 0; // First column
		gbc.gridy = 0; // First row
		gbc.anchor = GridBagConstraints.WEST;
		add(getCryptographyPanel().getLblAlgorithm(), gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 0; // First row
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(getCryptographyPanel().getCmbAlgorithm(), gbc);

		// KeySize label and combobox
		gbc.gridx = 0; // First column
		gbc.gridy = 1; // Second row
		gbc.anchor = GridBagConstraints.WEST;
		add(getCryptographyPanel().getLblKeySize(), gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 1; // Second row
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(getCryptographyPanel().getCmbKeySize(), gbc);

		// PrivateKey label and text area
		gbc.gridx = 0; // First column
		gbc.gridy = 2; // Third row
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(getCryptographyPanel().getLblPrivateKey(), gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 2; // Third row
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		add(getCryptographyPanel().getScpPrivateKey(), gbc);

		// PublicKey label and text area
		gbc.gridx = 0; // First column
		gbc.gridy = 3; // Fourth row
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(getCryptographyPanel().getLblPublicKey(), gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 3; // Fourth row
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		add(getCryptographyPanel().getScpPublicKey(), gbc);

		// Generate and Clear buttons
		gbc.gridx = 0; // First column
		gbc.gridy = 4; // Fifth row
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(getCryptographyPanel().getBtnGenerate(), gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 4; // Fifth row
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(getCryptographyPanel().getBtnClear(), gbc);

		// Save buttons (all on one row)
		gbc.gridx = 0; // First column
		gbc.gridy = 5; // Sixth row
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		JPanel saveButtonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints saveGbc = new GridBagConstraints();
		saveGbc.insets = new Insets(5, 5, 5, 5);

		saveGbc.gridx = 0;
		saveButtonPanel.add(getCryptographyPanel().getBtnSavePrivKeyWithPw(), saveGbc);

		saveGbc.gridx = 1;
		saveButtonPanel.add(getCryptographyPanel().getBtnSavePrivateKey(), saveGbc);

		saveGbc.gridx = 2;
		saveButtonPanel.add(getCryptographyPanel().getBtnSaveCertificate(), saveGbc);

		saveGbc.gridx = 3;
		saveButtonPanel.add(getCryptographyPanel().getBtnSavePublicKey(), saveGbc);

		add(saveButtonPanel, gbc);
	}

	/**
	 * Initialize layout.
	 */
	protected void onInitializeMigLayout()
	{
		MigLayout migLayout = new MigLayout("fill", // Layout constraints: fill the available space
			"[grow]", // Columns: single column that grows with the window
			"[grow][grow]" // Rows: two rows that grow with the window
		);
		setLayout(migLayout);

		// Add the panels with constraints
		add(cryptographyPanel, "grow, wrap"); // Make it grow and wrap to the next row
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on save private
	 * key.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onSavePrivateKey(final ActionEvent actionEvent)
	{
		final JFileChooser fileChooser = new JFileChooser();
		final int state = fileChooser.showSaveDialog(this);
		if (state == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				PrivateKeyWriter.writeInPemFormat(getModelObject().getPrivateKey(),
					fileChooser.getSelectedFile());
			}
			catch (final Exception exception)
			{
				log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
				String title = "Save of private key failed";
				String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
					+ "<p> Save of key private key failed" + "<p>" + exception.getMessage();
				DialogExtensions.showExceptionDialog(exception,
					WizardApplicationFrame.getInstance());
				throw new RuntimeException(title + "::" + htmlMessage, exception);

			}
		}
	}


	protected void onSavePrivateKeyWithPassword(final ActionEvent actionEvent)
	{
		final Object[] options = { "Set password", "Cancel" };
		final PasswordPanel panel = new PasswordPanel();

		final int result = JOptionPane.showOptionDialog(null, panel, "Enter a password",
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
		final String password = String.copyValueOf(panel.getTxtPassword().getPassword());
		final String repeatPassword = String
			.copyValueOf(panel.getTxtRepeatPassword().getPassword());
		if (result == 0 && 0 < password.length())
		{
			if (password.equals(repeatPassword))
			{

				final JFileChooser fileChooser = new JFileChooser();
				final int state = fileChooser.showSaveDialog(this);
				if (state == JFileChooser.APPROVE_OPTION)
				{
					PrivateKey privateKey;
					try
					{
						privateKey = getModelObject().getPrivateKey();
						File selectedFile = fileChooser.getSelectedFile();
						EncryptedPrivateKeyWriter.encryptPrivateKeyWithPassword(privateKey,
							selectedFile, password);
					}
					catch (final Exception exception)
					{
						log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
						String title = "Save of encrypted private key failed";
						String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
							+ "<p> Save of encrypted key private key failed" + "<p>"
							+ exception.getMessage();
						DialogExtensions.showExceptionDialog(exception,
							WizardApplicationFrame.getInstance());
						throw new RuntimeException(title + "::" + htmlMessage, exception);
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Entered passwords are not the same.");
			}
		}
	}


	/**
	 * Callback method that can be overwritten to provide specific action for the on save public
	 * key.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onSavePublicKey(final ActionEvent actionEvent)
	{
		final JFileChooser fileChooser = new JFileChooser();
		final int state = fileChooser.showSaveDialog(this);
		if (state == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				PublicKeyWriter.write(getModelObject().getPublicKey(),
					fileChooser.getSelectedFile());
			}
			catch (final Exception exception)
			{
				log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
				String title = "Save of public key failed";
				String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
					+ "<p> Save of key public key failed" + "<p>" + exception.getMessage();
				DialogExtensions.showExceptionDialog(exception,
					WizardApplicationFrame.getInstance());
				throw new RuntimeException(title + "::" + htmlMessage, exception);
			}
		}
	}
}
