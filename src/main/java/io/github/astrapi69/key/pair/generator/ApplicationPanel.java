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

import java.awt.*;

import io.github.astrapi69.key.pair.generator.keygen.GenerateKeysModelBean;
import io.github.astrapi69.key.pair.generator.keygen.GenerateKeysPanel;
import io.github.astrapi69.model.BaseModel;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.swing.base.BasePanel;

public class ApplicationPanel extends BasePanel<ApplicationModelBean>
{
	GenerateKeysPanel component;

	public ApplicationPanel(final IModel<ApplicationModelBean> model)
	{
		super(model);
	}

	public ApplicationPanel()
	{
		this(BaseModel.of(ApplicationModelBean.builder().build()));
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();
		ApplicationModelBean modelObject = getModelObject();
		GenerateKeysModelBean generateKeysModelBean = GenerateKeysModelBean.builder().build();
		modelObject.setGenerateKeysModelBean(generateKeysModelBean);
		IModel<GenerateKeysModelBean> model = BaseModel.of(generateKeysModelBean);
		component = newGenerateKeysPanel(model);
	}

	@Override
	protected void onInitializeLayout()
	{
		super.onInitializeLayout();
		this.setLayout(new BorderLayout());
		add(component);
	}

	protected GenerateKeysPanel newGenerateKeysPanel(final IModel<GenerateKeysModelBean> model)
	{
		return new GenerateKeysPanel(model);
	}
}
