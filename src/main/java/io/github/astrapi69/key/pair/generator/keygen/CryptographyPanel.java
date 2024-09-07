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

import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.*;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.SortedUniqueList;
import io.github.astrapi69.collection.pair.ValueBox;
import io.github.astrapi69.collection.set.SetExtensions;
import io.github.astrapi69.crypt.api.key.KeyFileFormat;
import io.github.astrapi69.crypt.data.factory.CertFactory;
import io.github.astrapi69.crypt.data.key.KeyInfoExtensions;
import io.github.astrapi69.crypt.data.key.KeySizeExtensions;
import io.github.astrapi69.crypt.data.key.writer.CertificateWriter;
import io.github.astrapi69.crypt.data.model.CertificateInfo;
import io.github.astrapi69.key.pair.generator.WizardApplicationFrame;
import io.github.astrapi69.key.pair.generator.wizard.CertificateWizardPanel;
import io.github.astrapi69.key.pair.generator.wizard.model.CertificateInfoModel;
import io.github.astrapi69.key.pair.generator.wizard.model.DistinguishedNameInfoModel;
import io.github.astrapi69.key.pair.generator.wizard.model.KeyInfoModel;
import io.github.astrapi69.key.pair.generator.wizard.model.ValidityModel;
import io.github.astrapi69.model.BaseModel;
import io.github.astrapi69.model.LambdaModel;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.random.number.RandomBigIntegerFactory;
import io.github.astrapi69.swing.base.BasePanel;
import io.github.astrapi69.swing.dialog.DialogExtensions;
import io.github.astrapi69.swing.listener.RequestFocusListener;
import io.github.astrapi69.swing.model.combobox.GenericComboBoxModel;
import io.github.astrapi69.swing.model.combobox.GenericMutableComboBoxModel;
import io.github.astrapi69.swing.model.component.JMComboBox;
import lombok.Getter;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;

/**
 * The class {@link CryptographyPanel} can generate private and public keys and save them to files.
 */
@Getter
@Log
public class CryptographyPanel extends BasePanel<GenerateKeysModelBean>
{

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The btn clear.
	 */
	private JButton btnClear;

	/**
	 * The btn generate.
	 */
	private JButton btnGenerate;

	/**
	 * The btn save private key.
	 */
	private JButton btnSavePrivateKey;

	private JButton btnSavePrivKeyWithPw;

	private JButton btnSaveCertificate;
	/**
	 * The btn save public key.
	 */
	private JButton btnSavePublicKey;

	/**
	 * The cmb key size.
	 */
	private JMComboBox<Integer, GenericComboBoxModel<Integer>> cmbKeySize;

	/**
	 * The lbl key size.
	 */
	private JLabel lblKeySize;

	/**
	 * The lbl private key.
	 */
	private JLabel lblPrivateKey;

	/**
	 * The lbl public key.
	 */
	private JLabel lblPublicKey;

	private JLabel lblAlgorithm;

	private JMComboBox<String, GenericMutableComboBoxModel<String>> cmbAlgorithm;

	/**
	 * The scp private key.
	 */
	private JScrollPane scpPrivateKey;

	/**
	 * The scp public key.
	 */
	private JScrollPane scpPublicKey;

	/**
	 * The txt private key.
	 */
	private JTextArea txtPrivateKey;

	/**
	 * The txt public key.
	 */
	private JTextArea txtPublicKey;

	/**
	 * Instantiates a new {@link CryptographyPanel}.
	 */
	public CryptographyPanel()
	{
		this(BaseModel.of(GenerateKeysModelBean.builder().build()));
	}

	public CryptographyPanel(final IModel<GenerateKeysModelBean> model)
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
	protected void onChangeKeySize(final ActionEvent actionEvent)
	{
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on clear.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onClear(final ActionEvent actionEvent)
	{
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on generate.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onGenerate(final ActionEvent actionEvent)
	{
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();

		scpPrivateKey = new JScrollPane();
		txtPrivateKey = new JTextArea();
		lblPrivateKey = new JLabel();
		lblKeySize = new JLabel();
		scpPublicKey = new JScrollPane();
		txtPublicKey = new JTextArea();
		lblPublicKey = new JLabel();
		btnGenerate = new JButton();
		btnClear = new JButton();
		btnSavePrivateKey = new JButton();
		btnSavePublicKey = new JButton();
		btnSavePrivKeyWithPw = new JButton();
		btnSaveCertificate = new JButton();

		// Initialize the algorithm JComboBox and label
		lblAlgorithm = new JLabel();

		cmbAlgorithm = newKeyAlgorithmCombobox(); // Add your algorithms here
		// Set the current algorithm from the model, if any
		cmbAlgorithm.setSelectedItem(getModelObject().getAlgorithm());

		// Add an ActionListener to update the model when the selection changes
		cmbAlgorithm.addActionListener(actionEvent -> onAlgorithmChange(actionEvent));

		lblAlgorithm.setText("Algorithm");
		try
		{
			Set<Integer> supportedKeySizes = KeySizeExtensions
				.getSupportedKeySizesForKeyPairGenerator(getModelObject().getAlgorithm());
			Integer[] array = SetExtensions.toArray(supportedKeySizes);
			cmbKeySize = newKeySizeCombobox(array);
			setKeySizeValue(getModelObject().getAlgorithm());
		}
		catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception)
		{
			log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
			String title = "Creation of key size of algorithm " + getModelObject().getAlgorithm()
				+ " failed";
			String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
				+ "<p> The creation of key size of algorithm " + getModelObject().getAlgorithm()
				+ " failed" + "<p>" + exception.getMessage();
			DialogExtensions.showExceptionDialog(exception, WizardApplicationFrame.getInstance());
			throw new RuntimeException(title + "::" + htmlMessage, exception);
		}

		txtPrivateKey.setEditable(false);
		txtPublicKey.setEditable(false);

		txtPrivateKey.setFont(new Font("monospaced", Font.PLAIN, 12));
		txtPublicKey.setFont(new Font("monospaced", Font.PLAIN, 12));

		cmbKeySize.addActionListener(actionEvent -> onChangeKeySize(actionEvent));
		btnGenerate.addActionListener(actionEvent -> onGenerate(actionEvent));
		btnClear.addActionListener(actionEvent -> onClear(actionEvent));
		btnSavePrivateKey.addActionListener(actionEvent -> onSavePrivateKey(actionEvent));
		btnSavePrivKeyWithPw
			.addActionListener(actionEvent -> onSavePrivateKeyWithPassword(actionEvent));
		btnSavePublicKey.addActionListener(actionEvent -> onSavePublicKey(actionEvent));
		btnSaveCertificate.addActionListener(actionEvent -> onSaveCertificate(actionEvent));

		txtPrivateKey.setColumns(20);
		txtPrivateKey.setRows(5);
		scpPrivateKey.setViewportView(txtPrivateKey);
		txtPrivateKey.getAccessibleContext().setAccessibleDescription("");

		btnGenerate.setText("Generate keys");

		lblPrivateKey.setText("Private key");

		lblKeySize.setText("Keysize");

		txtPublicKey.setColumns(20);
		txtPublicKey.setRows(5);
		scpPublicKey.setViewportView(txtPublicKey);

		lblPublicKey.setText("Public key");

		btnClear.setText("Clear keys");

		btnSavePrivateKey.setText("Save private key");
		btnSavePrivKeyWithPw.setText("Save private key with password");

		btnSavePublicKey.setText("Save public key");
		btnSaveCertificate.setText("Save certificate...");
		//
		btnSaveCertificate.setEnabled(false);
	}


	protected JMComboBox<Integer, GenericComboBoxModel<Integer>> newKeySizeCombobox(
		Integer[] cmbArray)
	{
		GenericComboBoxModel<Integer> comboBoxModel;
		ValueBox<Integer> valueBox;
		IModel<Integer> selectedItemModel;
		valueBox = ValueBox.<Integer> builder().value(getModelObject().getKeySize()).build();
		comboBoxModel = new GenericComboBoxModel<>(cmbArray);
		selectedItemModel = LambdaModel.of(valueBox::getValue, valueBox::setValue);
		return new JMComboBox<>(comboBoxModel, selectedItemModel);
	}

	protected JMComboBox<String, GenericMutableComboBoxModel<String>> newKeyAlgorithmCombobox()
	{

		ValueBox<String> keyAlgorithmValueBox;
		IModel<String> selectedKeyAlgorithmModel;
		GenericMutableComboBoxModel<String> cmbKeyAlgorithmModel;

		Set<String> keyAlgorithms = WizardApplicationFrame.getInstance().getModelObject()
			.getKeypairSignatureAlgorithms().keySet();
		if (keyAlgorithms.contains("DSTU4145"))
		{
			String[] array = SetExtensions.toArray(keyAlgorithms);
			String[] remove = { "DSTU4145" };
			keyAlgorithms = ArrayExtensions.asSet(ArrayExtensions.removeAll(array, remove));
		}

		String selectedKeyAlgorithm = getModelObject().getAlgorithm();

		keyAlgorithmValueBox = ValueBox.<String> builder().value(selectedKeyAlgorithm).build();
		selectedKeyAlgorithmModel = LambdaModel.of(keyAlgorithmValueBox::getValue,
			keyAlgorithmValueBox::setValue);

		cmbKeyAlgorithmModel = new GenericMutableComboBoxModel<>(keyAlgorithms,
			selectedKeyAlgorithm);

		return new JMComboBox<>(cmbKeyAlgorithmModel, selectedKeyAlgorithmModel);
	}

	protected void onAlgorithmChange(ActionEvent actionEvent)
	{
		String selectedItem = (String)cmbAlgorithm.getSelectedItem();
		getModelObject().setAlgorithm(selectedItem);
		System.out.println(selectedItem);
		try
		{
			setKeySizeValue(selectedItem);
		}
		catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		// After making the changes, do minimal revalidation or repaint
		cmbKeySize.revalidate();
		cmbKeySize.repaint();
	}

	private void setKeySizeValue(String selectedItem)
		throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
	{
		Map<String, SortedUniqueList<Integer>> keypairAlgorithmToKeySizes = WizardApplicationFrame
			.getInstance().getModelObject().getKeypairAlgorithmToKeySizes();
		boolean containsKeyPairAlgorithm = keypairAlgorithmToKeySizes.containsKey(selectedItem);
		Integer[] array;
		if (!containsKeyPairAlgorithm)
		{
			Set<Integer> supportedKeySizes = KeySizeExtensions
				.getSupportedKeySizesForKeyPairGenerator(selectedItem);
			array = supportedKeySizes.isEmpty()
				? ArrayFactory.newArray()
				: SetExtensions.toArray(supportedKeySizes);
		}
		else
		{
			SortedUniqueList<Integer> supportedKeySizes = keypairAlgorithmToKeySizes
				.get(selectedItem);
			array = supportedKeySizes.isEmpty()
				? ArrayFactory.newArray()
				: ListExtensions.toArray(supportedKeySizes);
		}
		cmbKeySize.setModel(new GenericComboBoxModel<>(array));
	}

	JDialog dialog;

	protected void onSaveCertificate(ActionEvent actionEvent)
	{
		GenerateKeysModelBean modelObject = getModelObject();
		PublicKey publicKey = modelObject.getPublicKey();
		PrivateKey privateKey = modelObject.getPrivateKey();
		KeyInfoModel privateKeyInfoModel = KeyInfoModel
			.toKeyInfoModel(KeyInfoExtensions.toKeyInfo(privateKey));
		KeyInfoModel publicKeyInfoModel = KeyInfoModel
			.toKeyInfoModel(KeyInfoExtensions.toKeyInfo(publicKey));
		ZonedDateTime now = ZonedDateTime.now();
		ValidityModel validityModel = ValidityModel.builder().notBefore(now)
			.notAfter(now.plusYears(1)).build();

		SortedUniqueList<String> stringSortedUniqueList = WizardApplicationFrame.getInstance()
			.getModelObject().getKeypairSignatureAlgorithms().get(modelObject.getAlgorithm());
		modelObject.setSignatureAlgorithm(stringSortedUniqueList.getFirst());

		final CertificateInfoModel certificateInfoModel = CertificateInfoModel.builder()
			.publicKeyInfo(publicKeyInfoModel).privateKeyInfo(privateKeyInfoModel)
			.serial(RandomBigIntegerFactory.randomBigInteger())
			.issuer(DistinguishedNameInfoModel.builder().build())
			.keyPairAlgorithm(modelObject.getAlgorithm())
			.signatureAlgorithm(modelObject.getSignatureAlgorithm())
			.subject(DistinguishedNameInfoModel.builder().build()).validityModel(validityModel)
			.build();

		CertificateWizardPanel wizardPanel = new CertificateWizardPanel(
			BaseModel.of(certificateInfoModel))
		{
			@Override
			public void onFinish()
			{
				dialog.dispose(); // Close the dialog when the wizard finishes
				final JFileChooser fileChooser = new JFileChooser();
				final int state = fileChooser.showSaveDialog(this);
				if (state == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						File selectedFile = fileChooser.getSelectedFile();
						CertificateInfoModel modelObject1 = getModelObject();
						CertificateInfo certificateInfo = CertificateInfoModel
							.toCertificateInfo(modelObject1);
						X509Certificate x509Certificate = CertFactory
							.newX509Certificate(certificateInfo);
						CertificateWriter.write(x509Certificate, selectedFile, KeyFileFormat.PEM);
					}
					catch (final Exception exception)
					{
						log.log(Level.SEVERE, exception.getLocalizedMessage(), exception);
						String title = "Creation of certificate failed";
						String htmlMessage = "<html><body width='350'>" + "<h2>" + title + "</h2>"
							+ "<p> Password or key file or both are not valid" + "<p>"
							+ exception.getMessage();

						DialogExtensions.showExceptionDialog(exception,
							WizardApplicationFrame.getInstance());
						throw new RuntimeException(title + "::" + htmlMessage, exception);
					}
				}
			}

			@Override
			public void onCancel()
			{
				dialog.dispose(); // Close the dialog when the wizard is canceled
			}
		};

		// Create a custom JOptionPane
		JOptionPane optionPane = new JOptionPane(wizardPanel, // The wizard panel as the main
																// content
			JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, // No default options, we'll
																	// control the buttons
			null, // No icon
			new Object[] { } // No buttons, handled by the wizard
		);

		// Create the dialog from the JOptionPane
		dialog = optionPane.createDialog("Create Certificate");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowFocusListener(new RequestFocusListener(wizardPanel.getWizardContentPanel()
			.getIssuerPanel().getNewCertificateAttributesPanel().getTxtCommonName()));

		dialog.setModal(true);
		// Show the dialog
		dialog.pack();
		dialog.setLocationRelativeTo(null); // Center the dialog
		dialog.setVisible(true);
	}

	protected void onInitializeGroupLayout()
	{
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(lblAlgorithm).addComponent(lblKeySize).addComponent(btnGenerate)
					.addComponent(btnClear))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(cmbAlgorithm).addComponent(cmbKeySize)))
			.addGroup(layout.createSequentialGroup().addComponent(lblPrivateKey)
				.addComponent(scpPrivateKey))
			.addGroup(layout.createSequentialGroup().addComponent(lblPublicKey)
				.addComponent(scpPublicKey))
			.addGroup(layout.createSequentialGroup().addComponent(btnSavePrivKeyWithPw)
				.addComponent(btnSavePrivateKey).addComponent(btnSaveCertificate)
				.addComponent(btnSavePublicKey)));

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lblAlgorithm).addComponent(cmbAlgorithm))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lblKeySize).addComponent(cmbKeySize))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(btnGenerate).addComponent(btnClear))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lblPrivateKey).addComponent(scpPrivateKey))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lblPublicKey).addComponent(scpPublicKey))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(btnSavePrivKeyWithPw).addComponent(btnSavePrivateKey)
				.addComponent(btnSaveCertificate).addComponent(btnSavePublicKey)));
	}

	@Override
	protected void onInitializeLayout()
	{
		super.onInitializeLayout();
		// onInitializeMigLayout();
		// onInitializeGroupLayout();
		// onInitializeGridBagLayout();
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
		add(lblAlgorithm, gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 0; // First row
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(cmbAlgorithm, gbc);

		// KeySize label and combobox
		gbc.gridx = 0; // First column
		gbc.gridy = 1; // Second row
		gbc.anchor = GridBagConstraints.WEST;
		add(lblKeySize, gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 1; // Second row
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(cmbKeySize, gbc);

		// Generate and Clear buttons
		gbc.gridx = 0; // First column
		gbc.gridy = 2; // Third row
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(btnGenerate, gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 2; // Third row
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(btnClear, gbc);

		// PrivateKey label and text area
		gbc.gridx = 0; // First column
		gbc.gridy = 3; // Fourth row
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(lblPrivateKey, gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 3; // Fourth row
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		add(scpPrivateKey, gbc);

		// PublicKey label and text area
		gbc.gridx = 0; // First column
		gbc.gridy = 4; // Fifth row
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(lblPublicKey, gbc);

		gbc.gridx = 1; // Second column
		gbc.gridy = 4; // Fifth row
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		add(scpPublicKey, gbc);

		// Save buttons
		gbc.gridx = 0; // First column
		gbc.gridy = 5; // Sixth row
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		add(btnSavePrivKeyWithPw, gbc);

		gbc.gridy = 6; // Seventh row
		add(btnSavePrivateKey, gbc);

		gbc.gridy = 7; // Eighth row
		add(btnSaveCertificate, gbc);

		gbc.gridy = 8; // Ninth row
		add(btnSavePublicKey, gbc);
	}

	protected void onInitializeMigLayout()
	{
		MigLayout migLayout = new MigLayout("fill", "[grow]", "[][][][][grow][]");
		// Set layout with MigLayout
		setLayout(migLayout);

		// Add Algorithm label and combobox
		add(lblAlgorithm, "split 2, gapright 10");
		add(cmbAlgorithm, "wrap, growx");

		// Add KeySize label and combobox
		add(lblKeySize, "split 2, gapright 10");
		add(cmbKeySize, "wrap, growx");

		// Add Generate and Clear buttons
		add(btnGenerate, "split 2, growx");
		add(btnClear, "wrap, growx");

		// Add PrivateKey label and text area
		add(lblPrivateKey, "split 2, gapright 10, top");
		add(scpPrivateKey, "span, grow, wrap");

		// Add PublicKey label and text area
		add(lblPublicKey, "split 2, gapright 10, top");
		add(scpPublicKey, "span, grow, wrap");

		// Add Save buttons
		add(btnSavePrivKeyWithPw, "split 4, growx");
		add(btnSavePrivateKey, "growx");
		add(btnSaveCertificate, "growx");
		add(btnSavePublicKey, "wrap, growx");
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
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on save private
	 * key with password.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	protected void onSavePrivateKeyWithPassword(final ActionEvent actionEvent)
	{
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
	}

}
