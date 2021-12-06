/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { retryWhen, timeout } from 'rxjs/operators';

import { ConfigService } from './config.service';
import { selectiveRetryStrategy } from './security.util';

import { Comune, Finanziamento, Richiedente, Richiesta,
  AssociazioneComuni, LeggeProvvDR, RichiestaSearchRequest,
  RichiestaSearchResponse, ProvvedimentoSearch, TettoMaxRichiesta,
  Erogazione, DeterminaToErogazioni, PraticaUrbGfu, PraticaUrb,
  PraticaUrbSintetica, Determina
} from '../model';

@Injectable({
  providedIn: 'root'
})
export class RichiestaService {

  constructor(private config: ConfigService, private http: HttpClient) { }

  /* COMUNI API */

  searchComuni(input: string): Observable<Comune[]> {
    if (input.length < 3) {
      return of([]);
    }
    return this.http.get<Comune[]>(this.config.apiRootPath + '/comuni?descComune=' + input.toUpperCase() + '&isValid=true')
    .pipe(
      timeout(3000),
      retryWhen(
        selectiveRetryStrategy({
          scalingDuration: 10,
          excludedStatusCodes: [302, 0],
          maxRetryAttempts: 2
        })
      )
    );
  }

  searchAssociazioni(input: string): Observable<AssociazioneComuni[]> {
    if (input.length < 3) {
      return of([]);
    }
    return this.http.get<AssociazioneComuni[]>(this.config.apiRootPath + '/associazioni?descAssociazione=' + input.toUpperCase() + '&isValid=true')
    .pipe(
      timeout(3000),
      retryWhen(
        selectiveRetryStrategy({
          scalingDuration: 10,
          excludedStatusCodes: [302, 0],
          maxRetryAttempts: 2
        })
      )
    );
  }

  getAssociazioneById(idAssociazione: number): Observable<AssociazioneComuni> {
    return this.http.get<AssociazioneComuni>(this.config.apiRootPath + '/associazioni/' + idAssociazione);
  }

  saveAssociazione(associazione: AssociazioneComuni): Observable<AssociazioneComuni> {
    return this.http.post<AssociazioneComuni>(this.config.apiRootPath + '/associazioni', associazione);
  }

  /* RICHIESTA API */

  getRichieste(): Observable<Richiesta[]> {
    return this.http.get<Richiesta[]>(this.config.apiRootPath + '/richieste');
  }

  getRichiestaById(idRichiesta: number): Observable<Richiesta> {
    return this.http.get<Richiesta>(this.config.apiRootPath + '/richieste/' + idRichiesta);
  }

  saveRichiesta(richiesta: Richiesta): Observable<Richiesta> {
    if (richiesta.idRichiesta) {
      return this.http.put<Richiesta>(this.config.apiRootPath + '/richieste/' + richiesta.idRichiesta, richiesta);
    } else {
      return this.http.post<Richiesta>(this.config.apiRootPath + '/richieste', richiesta);
    }
  }

  searchRichieste(filters: RichiestaSearchRequest): Observable<RichiestaSearchResponse[]> {
    return this.http.post<RichiestaSearchResponse[]>(this.config.apiRootPath + '/richieste/richiestefilter', filters);
  }

  deleteRichiesta(idRichiesta: number): Observable<any> {
    return this.http.delete(this.config.apiRootPath + '/richieste/' + idRichiesta);
  }

  searchRichiesteProvvedimentiFinanziamenti(filters: any): Observable<ProvvedimentoSearch[]> {
    let endpoint = this.config.apiRootPath + '/richieste/provvedimenti/finanziamenti?idStatoFinanziamento=' + filters.idStatoFinanziamento;
    if (filters.idLeggeProvvDr) {
      endpoint = endpoint + '&idLeggeProvvDr=' + filters.idLeggeProvvDr;
    }
    if (filters.dataProtocolloDa) {
      endpoint = endpoint + '&dataProtRichiestaDa=' + filters.dataProtocolloDa;
    }
    if (filters.dataProtocolloA) {
      endpoint = endpoint + '&dataProtRichiestaA=' + filters.dataProtocolloA;
    }

    return this.http.get<ProvvedimentoSearch[]>(endpoint);
  }

  exportSearchResultInCSV(filters: RichiestaSearchRequest) {
    return this.http.post(this.config.apiRootPath + '/richieste/exp', filters, {responseType: 'blob'});
  }

  /* RICHIEDENTE API */

  getRichiedentiLight(idRichiesta: number): Observable<Richiedente[]> {
    return this.http.get<Richiedente[]>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti');
  }

  getRichiedentiFull(idRichiesta: number): Observable<Richiedente[]> {
    return this.http.get<Richiedente[]>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti/provvedimenti');
  }

  saveRichiedenti(idRichiesta: number, richiedentiList: Richiedente[]): Observable<Richiedente[]> {
    return this.http.post<Richiedente[]>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti', richiedentiList);
  }

  deleteRichiedente(idRichiesta: number, idRichiedente: number): Observable<any> {
    return this.http.delete(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti/' + idRichiedente);
  }

  linkAssociazioneToRichiesta(idRichiesta: number, idAssociazione: number): Observable<any> {
    return this.http.put(this.config.apiRootPath + '/richieste/' + idRichiesta + '/' + idAssociazione, null);
  }

  // todo
  // unlinkAssociazioneFromRichiesta(idRichiesta: number): Observable<any> {
  //   return of(true);
  // }

  /* PROVVEDIMENTO API */

  searchLPD(input: string, validFilter?: boolean): Observable<LeggeProvvDR[]> {
    if (input.length < 3) {
      return of([]);
    }
    let endPoint = this.config.apiRootPath + '/leggi/provvedimenti/dr?descLeggeProvvDr=' + input;
    if (validFilter) {
      endPoint += '&isValid=true';
    }

    return this.http.get<LeggeProvvDR[]>(endPoint)
    .pipe(
      timeout(3000),
      retryWhen(
        selectiveRetryStrategy({
          scalingDuration: 10,
          excludedStatusCodes: [302, 0],
          maxRetryAttempts: 2
        })
      )
    );
  }

  postProvvToRichiedenti(idRichiesta: number, idLeggeProvvDr: number): Observable<Richiedente[]> {
    return this.http.post<Richiedente[]>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti/provvedimenti/' + idLeggeProvvDr, null);
  }

  putProvvToRichiedenti(idRichiesta: number, richiedente: Richiedente): Observable<any> {
    return this.http.put(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti/provvedimenti', richiedente);
  }

  deleteProvvToRichiedenti(idRichiesta: number, idLeggeProvvDr: number): Observable<any> {
    return this.http.delete(this.config.apiRootPath + '/richieste/' + idRichiesta + '/richiedenti/provvedimenti/' + idLeggeProvvDr);
  }

  getProvvedimentiByIdRichiesta(idRichiesta: number): Observable<LeggeProvvDR[]> {
    return this.http.get<LeggeProvvDR[]>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/provvedimenti');
  }

  /* FINANZIAMENTO API */

  getFinanziamentoById(idFinanziamento: number): Observable<Finanziamento> {
    return this.http.get<Finanziamento>(this.config.apiRootPath + '/finanziamenti/' + idFinanziamento);
  }

  getFinanziamentoByLpd(idRichiesta: number, idLpd: number): Observable<Finanziamento> {
    return this.http.get<Finanziamento>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/provvedimenti/' + idLpd + '/finanziamenti');
  }

  saveFinanziamento(idRichiesta: number, finanziamento: Finanziamento, idProvvedimento?: number): Observable<Finanziamento> {
    if (finanziamento.idFinanziamento) {
      return this.http.put<Finanziamento>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/finanziamenti/' + finanziamento.idFinanziamento, finanziamento);
    } else {
      return this.http.post<Finanziamento>(this.config.apiRootPath + '/richieste/' + idRichiesta + '/provvedimenti/' + idProvvedimento + '/finanziamenti', finanziamento);
    }
  }

  cambioStato(idFinanziamento: number, idNuovoStato: number): Observable<Finanziamento> {
    return this.http.put<Finanziamento>(this.config.apiRootPath + '/finanziamenti/' + idFinanziamento + '/stato/' + idNuovoStato, null);
  }

  getTettoMaxRichiesta(idRichiesta: number, idLpd: number): Observable<TettoMaxRichiesta> {
    return this.http.get<TettoMaxRichiesta>(`${this.config.apiRootPath}/richieste/${idRichiesta}/richiedenti/${idLpd}/tettomax`);
  }

  getErogazioniByFinanziamento(idFinanziamento: number): Observable<Erogazione[]> {
    return this.http.get<Erogazione[]>(this.config.apiRootPath + '/finanziamenti/' + idFinanziamento + '/erogazioni');
  }

  saveErogazione(idFinanziamento: number, erogazione: Erogazione): Observable<Finanziamento> {
    const url = `${this.config.apiRootPath}/finanziamenti/${idFinanziamento}/erogazioni`;
    return this.http.post<Finanziamento>(url, erogazione);
  }

  updateErogazione(idFinanziamento: number, erogazione: Erogazione): Observable<Finanziamento> {
    const url = `${this.config.apiRootPath}/finanziamenti/${idFinanziamento}/erogazioni/${erogazione.idErogazione}`;
    return this.http.put<Finanziamento>(url, erogazione);
  }

  /* FINANZIAMENTO-PRAURB API */

  getPraUrbWithinFinanziamento(idFinanziamento: number): Observable<Finanziamento> {
    return this.http.get<Finanziamento>(`${this.config.apiRootPath}/finanziamenti/${idFinanziamento}/pratiche`);
  }

  getPraUrbDetails(idFinanziamento: number, numPratica: string): Observable<PraticaUrb> {
    return this.http.get<PraticaUrb>(`${this.config.apiRootPath}/finanziamenti/${idFinanziamento}/pratiche/${numPratica}`);
  }

  postPraUrb(idFinanziamento: number, pratica: PraticaUrbGfu): Observable<Finanziamento> {
    return this.http.post<Finanziamento>(`${this.config.apiRootPath}/finanziamenti/${idFinanziamento}/pratiche`, pratica);
  }

  deletePraUrb(idFinanziamento: number): Observable<Finanziamento> {
    return this.http.delete<Finanziamento>(`${this.config.apiRootPath}/finanziamenti/${idFinanziamento}/pratiche`);
  }

  searchPraUrbByFilters(filters: any): Observable<PraticaUrbSintetica[]> {
    let endpoint = `${this.config.apiRootPath}/finanziamenti/pratiche?istatComune=${filters.istatComune}`;
    if (filters.dataProvvedimentoDa) {
      endpoint = endpoint + `&dataProvvedimentoDa=${filters.dataProvvedimentoDa}`;
    }
    if (filters.dataProvvedimentoA) {
      endpoint = endpoint + `&dataProvvedimentoA=${filters.dataProvvedimentoA}`;
    }

    return this.http.get<PraticaUrbSintetica[]>(endpoint);
  }

  /* ASSOCIA-DETERMINA API */

  associaDetermina(requestBody: DeterminaToErogazioni) {
    return this.http.put(`${this.config.apiRootPath}/finanziamenti/erogazioni/determine`, requestBody);
  }

  searchDetermina(filters): Observable<Determina> {
    return this.http.get<Determina>(`${this.config.apiRootPath}/determine?numDetermina=${filters.numDetermina}&dataDetermina=${filters.dataDetermina}`);
  }
}
