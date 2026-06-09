/*******************************************************************************
* Copyright (c) 2026 IBM Corporation and others.
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v. 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0.
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     IBM Corporation, Archana Iyer - initial API and implementation
*******************************************************************************/
package org.eclipse.jdtls.featureext.commons.codeaction;

import java.util.Objects;

/**
 * Represents the data that all code actions have.
 * 
 * This class is a shared code between https://github.com/eclipse-lsp4jakarta/lsp4jakarta
 * and https://github.com/eclipse-lsp4mp/lsp4mp repositories
 * and intentionally kept unchanged for migration purposes into the common project feature-ext.
 */
public class CodeActionData {

	private String id;

	/**
	 * No argument constructor
	 */
	public CodeActionData() {
		this(null);
	}

	/**
	 * Constructor with ICodeActionId
	 * @param id
	 */
	public CodeActionData(ICodeActionId id) {
		setCodeActionId(id);
	}

	/**
	 * Returns the id of this code action as a string.
	 *
	 * Each type of code action has an id that represents it, so that it's easy to
	 * associate a given code action to the code that generated it.
	 *
	 * @return the id of this code action
	 */
	public String getCodeActionId() {
		return id;
	}

	/**
	 * Sets the id of this code action.
	 *
	 * @param id the new value for the id of this code action
	 */
	public void setCodeActionId(ICodeActionId id) {
		if (id != null) {
			this.id = id.getId();
		} else {
			this.id = null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CodeActionData)) {
			return false;
		}
		CodeActionData other = (CodeActionData) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CodeActionData [id=" + id + "]";
	}

}
