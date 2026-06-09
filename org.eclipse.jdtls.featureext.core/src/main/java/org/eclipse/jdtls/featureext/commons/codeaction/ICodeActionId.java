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

/**
 * Represents an id that identifies the type of code action.
 * 
 * This class is a shared code between https://github.com/eclipse-lsp4jakarta/lsp4jakarta
 * and https://github.com/eclipse-lsp4mp/lsp4mp repositories
 * and intentionally kept unchanged for migration purposes into the common project feature-ext.
 */
public interface ICodeActionId {

	/**
	 * Returns the id of this code action as a string.
	 *
	 * @return the id of this code action as a string
	 */
	String getId();

}
