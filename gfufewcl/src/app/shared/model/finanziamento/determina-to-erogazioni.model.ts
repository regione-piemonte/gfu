/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface DeterminaToErogazioni {
  numDetermina: number;
  dataDetermina: string;
  finanziamentiDaAssociare: FinanziamentiDaAssociare[];
}

interface FinanziamentiDaAssociare {
  idFinanziamento: number;
}
