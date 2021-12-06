import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { forkJoin, Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { LocalDataSource } from 'ng2-smart-table';

import { APP_SETTINGS } from 'src/app/shared/settings/app-settings';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { RichiestaService } from 'src/app/shared/services/richiesta.service';
import { MessageService } from 'src/app/shared/services/message.service';
import {
  Comune,
  LeggeProvvDR,
  Parere,
  Rendiconto,
  RichiestaSearchRequest,
  RichiestaSearchResponse,
  StatoFinanziamento,
  TipoFormaAssociativa,
  VincoloPopolazione,
} from 'src/app/shared/model';

@Component({
  selector: 'app-richiesta-finanziamento-search',
  templateUrl: './richiesta-finanziamento-search.component.html',
  styleUrls: ['./richiesta-finanziamento-search.component.css']
})
export class RichiestaFinanziamentoSearchComponent implements OnInit {

  codProfilo: string;

  tipiFormaAssociativa: TipoFormaAssociativa[] = [];
  vincoliPopolazione: VincoloPopolazione[] = [];
  statiFinanziamento: StatoFinanziamento[] = [];
  pareri: Parere[] = [];
  rendiconti: Rendiconto[] = [];

  ricercaForm: FormGroup;

  searchFilters: RichiestaSearchRequest;
  searchResult: RichiestaSearchResponse[];
  richiesteTableSource = new LocalDataSource();

  comuniLoading = false;
  lpdLoading = false;
  associazioniLoading = false;

  richiesteTableColumns;
  richiesteTableSettings;

  searchComune = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.comuniLoading = true),
      switchMap( input => {
        return this.richiestaService.searchComuni(input)
        .pipe(
          map(comuni => comuni.slice(0, 10))
        );
      }),
      tap(() => this.comuniLoading = false),
    )

  comuneFieldFormatter = (comune: Comune) => comune.descComune;

  searchLPD = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.lpdLoading = true),
      switchMap( input => {
        return this.richiestaService.searchLPD(input)
        .pipe(
          map(list => list.slice(0, 10))
        );
      }),
      tap(() => this.lpdLoading = false),
    )

  lpdFieldFormatter = (ldp: LeggeProvvDR): string => ldp.descLeggeProvvDr;

  searchAssociazione = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.associazioniLoading = true),
      switchMap( input => {
        return this.richiestaService.searchAssociazioni(input)
        .pipe(
          map(associazioni => {
            const associazioniString = [];
            if (associazioni) {
              associazioni.forEach( associazione => associazioniString.push(associazione.descAssociazione) );
            }
            return associazioniString.slice(0, 10);
          })
        );
      }),
      tap(() => this.associazioniLoading = false),
    )

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private messageService: MessageService,
    private richiestaService: RichiestaService,
    private anagraficaService: AnagraficaService
  ) {
    this.codProfilo = APP_SETTINGS.codProfilo;
  }

  ngOnInit() {
    this.buildForm();
    this.configTable();

    const getTipiFormaAssociativa = this.anagraficaService.getTipiFormaAssociativaFull();
    const getVincoliPopolazione = this.anagraficaService.getVincoliPopolazione();
    const getStatiFinanziamento = this.anagraficaService.getStatiFinanziamento();
    const getPareri = this.anagraficaService.getPareri();
    const getRendiconti = this.anagraficaService.getRendiconti();

    forkJoin([getTipiFormaAssociativa, getVincoliPopolazione, getStatiFinanziamento, getPareri, getRendiconti]).subscribe(
      res => {
        this.tipiFormaAssociativa = res[0];
        this.vincoliPopolazione = res[1];
        this.statiFinanziamento = res[2];
        this.pareri = res[3];
        this.rendiconti = res[4];
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  buildForm() {
    this.ricercaForm = this.fb.group({
      numProtocollo: null,
      dataFrom: null,
      dataTo: null,
      provincia: null,
      comune: null,
      tipoFormaAssociativa: null,
      denomFormaAssociativa: null,
      lpd: null,
      popolazione: null,
      statoFinanziamento: null,
      numDetermina: null,
      dataDetermina: null,
      parere: null,
      rendiconto: null,
      attoRinuncia: null,
      flgPraticaAssociata: null,
      numPraUrb: null,
      numAttoApprovazione: null,
      dataAttoApprovazione: null
    });
  }

  configTable() {
    this.richiesteTableColumns = {
      numProtocollo: {
        title: 'Protocollo',
        editable: false,
      },
      dataProtocollo: {
        title: 'Data',
        editable: false,
        compareFunction: (direction: any, a: any, b: any) => {
          const splitA = a.split('/');
          const splitB = b.split('/');
          const millisA = new Date(splitA[2], splitA[1] - 1, splitA[0]).getTime();
          const millisB = new Date(splitB[2], splitB[1] - 1, splitB[0]).getTime();
          if (millisA < millisB) {
            return -1 * direction;
          }
          if (millisA > millisB) {
            return direction;
          }
          return 0;
        },
      },
      descRichiedente: {
        title: 'Richiedente',
        editable: false,
      },
      descTipoRichiedente: {
        title: 'Tipo richiedente',
        editable: false,
        // valuePrepareFunction: (cell, row) => {
        //   if ( !!row.descRichiedente && !row.descTipoRichiedente ) {
        //     return 'COMUNE';
        //   } else {
        //     return row.descTipoRichiedente;
        //   }
        // },
      },
      numPraticaPraurb: {
        title: 'NUMERO PRATICA',
        editable: false,
      },
    };

    this.richiesteTableSettings = {
      columns: this.richiesteTableColumns,
      mode: 'external',
      selectMode: 'single',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
        add: false,
        delete: this.codProfilo !== 'GUEST',
      },
      edit: {
        editButtonContent: '<i class="fa fa-file-text-o" aria-hidden="true"></i>',
      },
      delete: {
        confirmDelete: true,
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      pager: {
        display: true,
        perPage: 12
      }
    };
  }

  search() {
    let dataFrom = null;
    let dataTo = null;
    let dataDet = null;
    let dataAtto = null;

    if (this.ricercaForm.get('dataFrom').value) {
      const dataFromSplit = this.ricercaForm.get('dataFrom').value.split('-');
      dataFrom = dataFromSplit[2] + '/' + dataFromSplit[1] + '/' + dataFromSplit[0];
    }
    if (this.ricercaForm.get('dataTo').value) {
      const dataToSplit = this.ricercaForm.get('dataTo').value.split('-');
      dataTo = dataToSplit[2] + '/' + dataToSplit[1] + '/' + dataToSplit[0];
    }
    if (this.ricercaForm.get('dataDetermina').value) {
      const dataDetSplit = this.ricercaForm.get('dataDetermina').value.split('-');
      dataDet = dataDetSplit[2] + '/' + dataDetSplit[1] + '/' + dataDetSplit[0];
    }
    if (this.ricercaForm.get('dataAttoApprovazione').value) {
      const dataAttoSplit = this.ricercaForm.get('dataAttoApprovazione').value.split('-');
      dataAtto = dataAttoSplit[2] + '/' + dataAttoSplit[1] + '/' + dataAttoSplit[0];
    }

    this.searchFilters = {
      numProtocollo: this.ricercaForm.get('numProtocollo').value,
      dataProtocolloDa: dataFrom,
      dataProtocolloA: dataTo,
      siglaProv: this.ricercaForm.get('provincia').value,
      istatComune: this.ricercaForm.get('comune').value ? this.ricercaForm.get('comune').value.istatComune : null,
      idTipoFormaAssociativa: this.ricercaForm.get('tipoFormaAssociativa').value,
      denominazioneAssociazione: this.ricercaForm.get('denomFormaAssociativa').value,
      idLeggeProvvDr: this.ricercaForm.get('lpd').value ? this.ricercaForm.get('lpd').value.idLeggeProvvDr : null,
      idVincoloPopolazione: this.ricercaForm.get('popolazione').value,
      idStatoFinanziamento: this.ricercaForm.get('statoFinanziamento').value,
      numDetermina: this.ricercaForm.get('numDetermina').value,
      dataDetermina: dataDet,
      idParere: this.ricercaForm.get('parere').value,
      idRendiconto: this.ricercaForm.get('rendiconto').value,
      attoRinuncia: this.ricercaForm.get('attoRinuncia').value,
      flagPraticaUrbanisticaAssociata: this.ricercaForm.get('flgPraticaAssociata').value,
      numPraticaUrb: this.ricercaForm.get('numPraUrb').value,
      numAttoApprovazioneUrb: this.ricercaForm.get('numAttoApprovazione').value,
      dataAttoApprovazioneUrb: dataAtto,
    };

    this.richiestaService.searchRichieste(this.searchFilters).subscribe(
      res => {
        if (res) {
          this.searchResult = res;
          this.assignRichiedente();
          this.richiesteTableSource.load(this.searchResult);
          // this.richiesteTableSource.setSort([{ field: 'dataProtocollo', direction: 'asc' }]);
          // this.richiesteTableSource.setSort([{ field: 'numProtocollo', direction: 'asc' }]);
        } else {
          this.searchResult = null;
          this.searchFilters = null;
          this.messageService.showMessage('Attenzione', 'La ricerca non ha prodotto alcun risultato.');
        }
      }, err => {
        !!err && !!err.error && !!err.error.message
        ? this.messageService.showMessage('Attenzione', err.error.message)
        : this.messageService.showGenericError();
      }
    );
  }

  assignRichiedente() {
    this.searchResult.forEach(el => {
      if (el.descRichiedente && !el.descTipoRichiedente) {
        el.descTipoRichiedente = 'COMUNE';
      }
    });
  }

  clearForm() {
    this.ricercaForm.reset();
    this.searchResult = null;
  }

  viewDetail(event) {
    // console.log('Edit event.data: ', event.data);
    this.router.navigate(['../edit/' + event.data.idRichiesta], { relativeTo: this.activatedRoute });
  }

  deleteRichiesta(event) {
    const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler eliminare la richiesta selezionata?');
    modalRef.result.then(confirm => {
      if (confirm) {
        this.richiestaService.deleteRichiesta(event.data.idRichiesta).subscribe(
          res => {
            const index = this.searchResult.findIndex(item => item.idRichiesta === event.data.idRichiesta);
            this.searchResult.splice(index, 1);
            this.richiesteTableSource.load(this.searchResult);
          }, err => {
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );
      }
    });
  }

  exportCSV() {
    if (!this.searchResult) {
      return;
    }

    this.richiestaService.exportSearchResultInCSV(this.searchFilters).subscribe(
      response => {
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' }); // type: 'text/csv' ?
        const url = window.URL.createObjectURL(blob);
        const fileName = 'Elenco_Richieste' + '.xls';

        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove();

      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  print() {
    console.log('Stampa: funzionalità non ancora implementata...');
  }

}
