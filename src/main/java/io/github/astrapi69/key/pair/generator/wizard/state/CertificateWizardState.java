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
package io.github.astrapi69.key.pair.generator.wizard.state;

import io.github.astrapi69.key.pair.generator.WizardApplicationFrame;
import io.github.astrapi69.design.pattern.state.wizard.BaseWizardState;
import io.github.astrapi69.design.pattern.state.wizard.model.BaseWizardStateMachineModel;
import io.github.astrapi69.design.pattern.state.wizard.model.WizardStateInfo;
import io.github.astrapi69.swing.dialog.DialogExtensions;
import io.github.astrapi69.key.pair.generator.wizard.model.CertificateInfoModel;

public enum CertificateWizardState
	implements BaseWizardState<BaseWizardStateMachineModel<CertificateInfoModel>>
{
	ISSUER {

		@Override
		public String getName()
		{
			return name();
		}

		@Override
		public void goNext(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			CertificateInfoModel modelObject = stateMachine.getModelObject();
			String commonName = modelObject.getIssuer().getCommonName();
			if (commonName != null && !commonName.trim().isEmpty())
			{
				stateMachine.setCurrentState(SUBJECT);
			}
			else
			{
				DialogExtensions.showInformationDialog(WizardApplicationFrame.getInstance(),
					"Empty common name",
					"The common name of the issuer is empty.<br> Please set the common name of the issuer.");
			}
		}

		@Override
		public void goPrevious(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			// No previous state
		}

		@Override
		public boolean hasPrevious()
		{
			return false;
		}

		@Override
		public boolean isFirst()
		{
			return true;
		}

		@Override
		public void cancel(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public void finish(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public WizardStateInfo getWizardStateInfo()
		{
			return null;
		}

		@Override
		public void setWizardStateInfo(WizardStateInfo wizardStateInfo)
		{

		}

	},
	SUBJECT {

		@Override
		public String getName()
		{
			return name();
		}

		@Override
		public void goNext(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			CertificateInfoModel modelObject = stateMachine.getModelObject();
			String commonName = modelObject.getSubject().getCommonName();
			if (commonName != null && !commonName.trim().isEmpty())
			{
				stateMachine.setCurrentState(DATES);
			}
			else
			{
				DialogExtensions.showInformationDialog(WizardApplicationFrame.getInstance(),
					"Empty common name",
					"The common name of the subject is empty.<br> Please set the common name of the subject.");
			}
		}

		@Override
		public void goPrevious(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			stateMachine.setCurrentState(ISSUER);
		}

		@Override
		public void cancel(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public void finish(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public WizardStateInfo getWizardStateInfo()
		{
			return null;
		}

		@Override
		public void setWizardStateInfo(WizardStateInfo wizardStateInfo)
		{

		}


	},
	DATES {
		WizardStateInfo wizardStateInfo;
		{
			wizardStateInfo = WizardStateInfo.builder().next(true).last(false).name(name()).build();
		}

		@Override
		public void goNext(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			CertificateInfoModel modelObject = stateMachine.getModelObject();
			if (modelObject.getVersion() == 3)
			{
				stateMachine.setCurrentState(EXTENSIONS);
			}
		}

		@Override
		public void goPrevious(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			stateMachine.setCurrentState(SUBJECT);
		}

		@Override
		public void cancel(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public void finish(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public WizardStateInfo getWizardStateInfo()
		{
			return wizardStateInfo;
		}

		@Override
		public void setWizardStateInfo(WizardStateInfo wizardStateInfo)
		{
			this.wizardStateInfo = wizardStateInfo;
		}

	},
	EXTENSIONS {

		@Override
		public String getName()
		{
			return name();
		}

		@Override
		public void goNext(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			// No next state
		}

		@Override
		public void goPrevious(BaseWizardStateMachineModel<CertificateInfoModel> stateMachine)
		{
			stateMachine.setCurrentState(DATES);
		}

		@Override
		public boolean hasNext()
		{
			return false;
		}

		@Override
		public boolean isLast()
		{
			return true;
		}

		@Override
		public void cancel(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public void finish(
			BaseWizardStateMachineModel<CertificateInfoModel> x509CertificateV3InfoBaseWizardStateMachineModel)
		{

		}

		@Override
		public WizardStateInfo getWizardStateInfo()
		{
			return null;
		}

		@Override
		public void setWizardStateInfo(WizardStateInfo wizardStateInfo)
		{

		}

	}
}
