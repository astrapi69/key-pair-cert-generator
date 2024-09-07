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

import io.github.astrapi69.design.pattern.state.wizard.model.BaseWizardStateMachineModel;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.swing.wizard.BaseWizardContentPanel;
import io.github.astrapi69.key.pair.generator.wizard.model.CertificateInfoModel;
import io.github.astrapi69.key.pair.generator.wizard.state.CertificateWizardState;
import lombok.Getter;

@Getter
public class CertificateWizardContentPanel extends BaseWizardContentPanel<CertificateInfoModel>
{

	private static final long serialVersionUID = 1L;

	IssuerPanel issuerPanel;
	SubjectPanel subjectPanel;
	DatesPanel datesPanel;
	ExtensionsPanel extensionsPanel;

	public CertificateWizardContentPanel(
		IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		super(model);
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();
		IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model = getModel();
		add(issuerPanel = newIssuerPanel(model), CertificateWizardState.ISSUER.getName());
		add(subjectPanel = newSubjectPanel(model), CertificateWizardState.SUBJECT.getName());
		add(datesPanel = newDatesPanel(model), CertificateWizardState.DATES.getName());
		add(extensionsPanel = newExtensionsPanel(model),
			CertificateWizardState.EXTENSIONS.getName());
	}

	protected ExtensionsPanel newExtensionsPanel(
		IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		return new ExtensionsPanel(model);
	}


	protected DatesPanel newDatesPanel(
		IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		return new DatesPanel(model);
	}

	protected IssuerPanel newIssuerPanel(
		IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		return new IssuerPanel(model);
	}

	protected SubjectPanel newSubjectPanel(
		IModel<BaseWizardStateMachineModel<CertificateInfoModel>> model)
	{
		return new SubjectPanel(model);
	}

}
