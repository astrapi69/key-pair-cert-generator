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

import javax.swing.JLabel;

import io.github.astrapi69.design.pattern.state.wizard.model.BaseWizardStateMachineModel;
import io.github.astrapi69.key.pair.generator.NewCertificateAttributesPanel;
import io.github.astrapi69.key.pair.generator.wizard.model.CertificateInfoModel;
import io.github.astrapi69.model.BaseModel;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.swing.base.BasePanel;
import net.miginfocom.swing.MigLayout;

public class SubjectPanel extends BasePanel<BaseWizardStateMachineModel<CertificateInfoModel>>
{

	private JLabel lblHeader;
	private NewCertificateAttributesPanel newCertificateAttributesPanel;

	public SubjectPanel(IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		super(model);
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();
		lblHeader = new JLabel("Subject");
		newCertificateAttributesPanel = new NewCertificateAttributesPanel(
			BaseModel.of(getModelObject().getModelObject().getSubject()));
	}

	@Override
	protected void onInitializeLayout()
	{
		super.onInitializeLayout();
		onInitializeMigLayout();
	}

	protected void onInitializeMigLayout()
	{
		MigLayout migLayout = new MigLayout("wrap 1", "[grow, fill]", "[][][grow]");

		setLayout(migLayout);
		add(lblHeader, "cell 0 0, alignx center, gapbottom 10");
		add(newCertificateAttributesPanel, "cell 0 1, grow");

	}

	@Override
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		if (value)
		{
			newCertificateAttributesPanel.getTxtCommonName().requestFocusInWindow();
		}
	}
}
