/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export class SrvError {
  code: string;
  errorMessage: string;
  fields: string;
  message: string;
  sessionExpired ?: boolean;
}
