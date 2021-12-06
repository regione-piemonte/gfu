/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { ConfigService } from './config.service';
import { DR, Legge, Parere, Percentuale, VincoloPopolazione, Profilo, Provvedimento, Rendiconto, StatoFinanziamento, TipoDR, Utente, LeggeProvvDR, TettoMax, TipoFormaAssociativa } from '../model';
import { retryWhen, timeout } from 'rxjs/operators';
import { selectiveRetryStrategy } from './security.util';


@Injectable({
  providedIn: 'root'
})
export class AnagraficaService {

  constructor(private config: ConfigService, private http: HttpClient) { }

  getTipiFormaAssociativa(): Observable<TipoFormaAssociativa[]> {
    const list: TipoFormaAssociativa[] = [
      {
        idTipoFormaAssociativa: 4,
        descrizione: 'CONVENZIONE',
        codTipo: 'CONV',
        isValid: true
      },
      {
        idTipoFormaAssociativa: 5,
        descrizione: 'UNIONE DI COMUNI (GFU)',
        codTipo: 'UCGFU',
        isValid: true
      }
    ];
    return of(list);
  }

  getTipiFormaAssociativaFull(): Observable<TipoFormaAssociativa[]> {
    const list: TipoFormaAssociativa[] = [
      {
        idTipoFormaAssociativa: 1,
        descrizione: "COMUNITA' MONTANA",
        codTipo: 'CM',
        isValid: false
      },
      {
        idTipoFormaAssociativa: 2,
        descrizione: 'CONSORZIO',
        codTipo: 'CONS',
        isValid: false
      },
      {
        idTipoFormaAssociativa: 3,
        descrizione: 'UNIONE DI COMUNI (DEPRECATA)',
        codTipo: 'UCO',
        isValid: false
      },
      {
        idTipoFormaAssociativa: 4,
        descrizione: 'CONVENZIONE',
        codTipo: 'CONV',
        isValid: true
      },
      {
        idTipoFormaAssociativa: 5,
        descrizione: 'UNIONE DI COMUNI (GFU)',
        codTipo: 'UCGFU',
        isValid: true
      },
      {
        idTipoFormaAssociativa: 6,
        descrizione: 'UNIONE DI COMUNI (Catalogo Dati Piemonte)',
        codTipo: 'UCDP',
        isValid: true
      }
    ];
    return of(list);
  }

  /* ANAGRAFICA PROVVEDIMENTO API */

  getTipiDr(): Observable<TipoDR[]> {
    return this.http.get<TipoDR[]>(this.config.apiRootPath + '/dr/tipi')
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

  getDr(): Observable<DR[]> {
    return this.http.get<DR[]>(this.config.apiRootPath + '/dr')
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

  saveDr(dr: DR): Observable<DR> {
    if (dr.idDr) {
      return this.http.put<DR>(this.config.apiRootPath + '/dr', dr)
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
    } else {
      return this.http.post<DR>(this.config.apiRootPath + '/dr', dr)
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
  }

  getLeggi(): Observable<Legge[]> {
    return this.http.get<Legge[]>(this.config.apiRootPath + '/leggi')
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

  saveLegge(legge: Legge): Observable<Legge> {
    if (legge.idLegge) {
      return this.http.put<Legge>(this.config.apiRootPath + '/leggi', legge)
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
    } else {
      return this.http.post<Legge>(this.config.apiRootPath + '/leggi', legge)
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
  }

  getProvvedimenti(): Observable<Provvedimento[]> {
    return this.http.get<Provvedimento[]>(this.config.apiRootPath + '/provvedimenti')
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

  saveProvvedimento(provv: Provvedimento): Observable<Provvedimento> {
    if (provv.idProvvedimento) {
      return this.http.put<Provvedimento>(this.config.apiRootPath + '/provvedimenti', provv)
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
    } else {
      return this.http.post<Provvedimento>(this.config.apiRootPath + '/provvedimenti', provv)
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
  }

  getVincoliPopolazione(): Observable<VincoloPopolazione[]> {
    return this.http.get<VincoloPopolazione[]>(this.config.apiRootPath + '/popolazione')
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

  saveVincoloPopolazione(vincoloPopolazione: VincoloPopolazione): Observable<VincoloPopolazione> {
    if (vincoloPopolazione.idVincoloPopolazione) {
      return this.http.put<VincoloPopolazione>(this.config.apiRootPath + '/popolazione', vincoloPopolazione)
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
    } else {
      return this.http.post<VincoloPopolazione>(this.config.apiRootPath + '/popolazione', vincoloPopolazione)
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
  }

  getLeggeProvvDr(): Observable<LeggeProvvDR[]> {
    return this.http.get<LeggeProvvDR[]>(this.config.apiRootPath + '/leggi/provvedimenti/dr')
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

  getLpdById(idLeggeProvvDr: number): Observable<LeggeProvvDR> {
    return this.http.get<LeggeProvvDR>(this.config.apiRootPath + '/leggi/provvedimenti/dr/' + idLeggeProvvDr)
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

  saveLeggeProvvDr(lpd: LeggeProvvDR): Observable<LeggeProvvDR> {
    if (lpd.idLeggeProvvDr) {
      return this.http.put<LeggeProvvDR>(this.config.apiRootPath + '/leggi/provvedimenti/dr', lpd)
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
    } else {
      return this.http.post<LeggeProvvDR>(this.config.apiRootPath + '/leggi/provvedimenti/dr', lpd)
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
  }

  /* ANAGRAFICA FINANZIAMENTO API */

  getPercentuali(validFilter?: boolean): Observable<Percentuale[]> {
    let endPoint = this.config.apiRootPath + '/percentuali';
    if (validFilter) {
      endPoint += '?isValid=true';
    }
    return this.http.get<Percentuale[]>(endPoint)
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

  savePercentuale(percentuale: Percentuale): Observable<Percentuale> {
    if (percentuale.idPercentuale) {
      return this.http.put<Percentuale>(this.config.apiRootPath + '/percentuali', percentuale)
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
    } else {
      return this.http.post<Percentuale>(this.config.apiRootPath + '/percentuali', percentuale)
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
  }

  getPareri(validFilter?: boolean): Observable<Parere[]> {
    let endPoint = this.config.apiRootPath + '/pareri';
    if (validFilter) {
      endPoint += '?isValid=true';
    }
    return this.http.get<Parere[]>(endPoint)
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

  saveParere(parere: Parere): Observable<Parere> {
    if (parere.idParere) {
      return this.http.put<Parere>(this.config.apiRootPath + '/pareri', parere)
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
    } else {
      return this.http.post<Parere>(this.config.apiRootPath + '/pareri', parere)
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
  }

  getRendiconti(validFilter?: boolean): Observable<Rendiconto[]> {
    let endPoint = this.config.apiRootPath + '/rendiconti';
    if (validFilter) {
      endPoint += '?isValid=true';
    }
    return this.http.get<Rendiconto[]>(endPoint)
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

  saveRendiconto(rendiconto: Rendiconto): Observable<Rendiconto> {
    if (rendiconto.idRendiconto) {
      return this.http.put<Rendiconto>(this.config.apiRootPath + '/rendiconti', rendiconto)
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
    } else {
      return this.http.post<Rendiconto>(this.config.apiRootPath + '/rendiconti', rendiconto)
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
  }

  saveTettoMax(tettoMax: TettoMax): Observable<TettoMax> {
    if (tettoMax.idTettoMax) {
      return this.http.put<TettoMax>(this.config.apiRootPath + '/tettomax', tettoMax)
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
    } else {
      return this.http.post<TettoMax>(this.config.apiRootPath + '/tettomax', tettoMax)
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
  }

  // todo
  getStatiFinanziamento(): Observable<StatoFinanziamento[]> {

    const stati: StatoFinanziamento[] = [
      {
        idStatoFinanziamento: 1,
        descrizione: 'IN CORSO',
        codStatoFinanziamento: 'INCORSO'
      },
      {
        idStatoFinanziamento: 2,
        descrizione: 'SOSPESO',
        codStatoFinanziamento: 'SOSP'
      },
      {
        idStatoFinanziamento: 3,
        descrizione: 'PERFEZIONATO PER ACCONTO',
        codStatoFinanziamento: 'PERFACC'
      },
      {
        idStatoFinanziamento: 4,
        descrizione: 'ACCONTO EROGATO',
        codStatoFinanziamento: 'ACCEROG'
      },
      {
        idStatoFinanziamento: 5,
        descrizione: 'PERFEZIONATO PER SALDO',
        codStatoFinanziamento: 'PERFSAL'
      },
      {
        idStatoFinanziamento: 6,
        descrizione: 'CONCLUSO',
        codStatoFinanziamento: 'CONCL'
      },
      {
        idStatoFinanziamento: 7,
        descrizione: 'RESPINTO',
        codStatoFinanziamento: 'RESP'
      },
      {
        idStatoFinanziamento: 8,
        descrizione: 'REVOCATO',
        codStatoFinanziamento: 'REVOC'
      },
      {
        idStatoFinanziamento: 9,
        descrizione: 'ANNULLATO PER RINUNCIA',
        codStatoFinanziamento: 'ANNXRIN'
      },
      {
        idStatoFinanziamento: 10,
        descrizione: 'VALIDATO',
        codStatoFinanziamento: 'VALID'
      },
    ];
    return of(stati);
  }

  getAllTettoMax(): Observable<TettoMax[]> {
    return this.http.get<TettoMax[]>(this.config.apiRootPath + '/tettomax')
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

  /* ANAGRAFICA UTENTI API*/

  // todo
  getProfili(): Observable<Profilo[]> {
    const profili: Profilo[] = [
      {
        idProfilo: 1,
        codProfilo: 'GUEST',
        descrizioneProfilo: 'Consultazione'
      },
      {
        idProfilo: 2,
        codProfilo: 'ADMIN',
        descrizioneProfilo: 'Amministratore'
      },
      {
        idProfilo: 3,
        codProfilo: 'SUPERADMIN',
        descrizioneProfilo: 'Super Amministratore'
      }
    ];
    return of(profili);
  }

  getUtenti(): Observable<Utente[]> {
    return this.http.get<Utente[]>(this.config.apiRootPath + '/utentianag')
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

  saveUtente(utente: Utente): Observable<Utente> {
    if (utente.idUtente) {
      return this.http.put<Utente>(this.config.apiRootPath + '/utentianag', utente);
    } else {
      return this.http.post<Utente>(this.config.apiRootPath + '/utentianag', utente);
    }
  }

  deleteUtente(idUtente: number): Observable<Utente> {
    return this.http.delete<Utente>(this.config.apiRootPath + '/utentianag/' + idUtente);
  }



}
