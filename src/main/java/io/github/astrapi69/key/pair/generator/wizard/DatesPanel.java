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
package io.github.astrapi69.key.pair.generator.wizard;

import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;

import io.github.astrapi69.key.pair.generator.WizardApplicationFrame;
import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.SortedUniqueList;
import io.github.astrapi69.collection.pair.ValueBox;
import io.github.astrapi69.design.pattern.state.wizard.model.BaseWizardStateMachineModel;
import io.github.astrapi69.design.pattern.state.wizard.model.NavigationEventState;
import io.github.astrapi69.model.LambdaModel;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.random.number.RandomBigIntegerFactory;
import io.github.astrapi69.swing.base.BasePanel;
import io.github.astrapi69.swing.model.combobox.GenericComboBoxModel;
import io.github.astrapi69.swing.model.component.JMBigIntegerTextField;
import io.github.astrapi69.swing.model.component.JMComboBox;
import io.github.astrapi69.key.pair.generator.eventbus.ApplicationEventBus;
import io.github.astrapi69.key.pair.generator.wizard.model.CertificateInfoModel;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

@Getter
public class DatesPanel extends BasePanel<BaseWizardStateMachineModel<CertificateInfoModel>>
{

	private JLabel lblHeader;
	private JLabel lblNotAfter;
	private JLabel lblNotBefore;
	private JLabel lblSerialNumber;
	private JLabel lblSignatureAlgorithm;
	private JLabel lblVersion;
	private CalendarPanel txtNotAfter;
	private CalendarPanel txtNotBefore;
	private JMBigIntegerTextField txtSerialNumber;
	private JMComboBox<Integer, GenericComboBoxModel<Integer>> cmbVersion;
	private JButton btnGenerateSerialNumber;
	private JMComboBox<String, GenericComboBoxModel<String>> cmbSignatureAlgorithm;

	public DatesPanel(IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		super(model);
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();

		lblHeader = new JLabel("Certificate Data");
		lblNotAfter = new JLabel("Not After:");
		lblNotBefore = new JLabel("Not Before:");
		lblSerialNumber = new JLabel("Serial Number:");
		lblSignatureAlgorithm = new JLabel("Signature Algorithm:");
		lblVersion = new JLabel("Version:");

		txtNotBefore = new CalendarPanel(new DatePicker());
		txtNotAfter = new CalendarPanel(new DatePicker());
		txtSerialNumber = new JMBigIntegerTextField();
		btnGenerateSerialNumber = new JButton();

		CertificateInfoModel modelObject = getModelObject().getModelObject();

		cmbSignatureAlgorithm = newSignatureAlgorithmCombobox();
		cmbSignatureAlgorithm.addActionListener(this::onChangeSignatureAlgorithm);

		GenericComboBoxModel<Integer> comboBoxModel;
		ValueBox<Integer> valueBox;
		IModel<Integer> selectedItemModel;
		valueBox = ValueBox.<Integer> builder().value(3).build();
		Integer[] cmbArray = ArrayFactory.newArray(1, 3);
		comboBoxModel = new GenericComboBoxModel<>(cmbArray);
		selectedItemModel = LambdaModel.of(valueBox::getValue, valueBox::setValue);
		cmbVersion = new JMComboBox<>(comboBoxModel, selectedItemModel);

		cmbVersion.addActionListener(this::onChangeVersion);

		txtSerialNumber
			.setPropertyModel(LambdaModel.of(modelObject::getSerial, modelObject::setSerial));

		txtNotAfter.setSelectedDate(modelObject.getValidityModel().getNotAfter().toLocalDate());
		txtNotBefore.setSelectedDate(modelObject.getValidityModel().getNotBefore().toLocalDate());

		btnGenerateSerialNumber.setText("Generate");
		btnGenerateSerialNumber.addActionListener(this::onGenerateSerialNumber);
	}

	protected JMComboBox<String, GenericComboBoxModel<String>> newSignatureAlgorithmCombobox()
	{

		GenericComboBoxModel<String> comboBoxModel;
		ValueBox<String> signatureAlgorithmValueBox;
		IModel<String> selectedSignatureAlgorithmModel;
		CertificateInfoModel modelObject = getModelObject().getModelObject();
		String selectedSignatureAlgorithm;
		String keyPairAlgorithm = modelObject.getKeyPairAlgorithm();

		SortedUniqueList<String> stringSortedUniqueList = WizardApplicationFrame.getInstance()
			.getModelObject().getKeypairSignatureAlgorithms().get(keyPairAlgorithm);
		selectedSignatureAlgorithm = stringSortedUniqueList.getFirst();
		List<String> signatureAlgorithms = stringSortedUniqueList;

		signatureAlgorithmValueBox = ValueBox.<String> builder().value(selectedSignatureAlgorithm)
			.build();
		selectedSignatureAlgorithmModel = LambdaModel.of(signatureAlgorithmValueBox::getValue,
			signatureAlgorithmValueBox::setValue);

		comboBoxModel = new GenericComboBoxModel<>(signatureAlgorithms);

		return new JMComboBox<>(comboBoxModel, selectedSignatureAlgorithmModel);
	}

	protected void onChangeSignatureAlgorithm(ActionEvent actionEvent)
	{
		Object item = cmbSignatureAlgorithm.getSelectedItem();
		String selectedSignatureAlgorithm = (String)item;
		CertificateInfoModel modelObject = getModelObject().getModelObject();
		modelObject.setSignatureAlgorithm(selectedSignatureAlgorithm);
	}

	/**
	 * Callback method that can be overwritten to provide specific action for the on change key
	 * size.
	 *
	 * @param actionEvent
	 *            the action event
	 */
	@SuppressWarnings("unchecked")
	protected void onChangeVersion(final ActionEvent actionEvent)
	{
		Object item = cmbVersion.getSelectedItem();
		Integer selectedVersion = (Integer)item;
		CertificateInfoModel modelObject = getModelObject().getModelObject();
		modelObject.setVersion(selectedVersion);
		if (selectedVersion.equals(1))
		{
			getModelObject().getCurrentState().getWizardStateInfo().setNext(false);
			updateWizardButtons();
		}
		else if (selectedVersion.equals(3))
		{
			getModelObject().getCurrentState().getWizardStateInfo().setNext(true);
			updateWizardButtons();
		}
	}

	protected void updateWizardButtons()
	{
		ApplicationEventBus.post(NavigationEventState.UPDATE);
	}

	protected void onGenerateSerialNumber(java.awt.event.ActionEvent evt)
	{
		BigInteger serialNumber = RandomBigIntegerFactory.randomSerialNumber();
		CertificateInfoModel modelObject = getModelObject().getModelObject();
		modelObject.setSerial(serialNumber);
		getTxtSerialNumber().setText(serialNumber.toString());
	}

	@Override
	protected void onInitializeLayout()
	{
		super.onInitializeLayout();
		setLayout(new MigLayout("", "[right][grow]", "[][][][][][]"));

		add(lblHeader, "span, align center, wrap 10");
		add(lblVersion, "cell 0 1, alignx trailing");
		add(cmbVersion, "cell 1 1, growx");

		add(lblSerialNumber, "cell 0 2, alignx trailing");
		add(txtSerialNumber, "cell 1 2, growx, split 2");
		add(btnGenerateSerialNumber, "cell 1 2");

		add(lblNotBefore, "cell 0 3, alignx trailing");
		add(txtNotBefore, "cell 1 3, growx");

		add(lblNotAfter, "cell 0 4, alignx trailing");
		add(txtNotAfter, "cell 1 4, growx");

		add(lblSignatureAlgorithm, "cell 0 5, alignx trailing");
		add(cmbSignatureAlgorithm, "cell 1 5, growx");
	}


	@Override
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		if (value)
		{
			cmbVersion.requestFocusInWindow();
		}
	}
}
