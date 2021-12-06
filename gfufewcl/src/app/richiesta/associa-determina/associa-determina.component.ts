/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap, takeUntil, tap } from 'rxjs/operators';
import { LocalDataSource, Ng2SmartTableComponent } from 'ng2-smart-table';

import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { RichiestaService } from 'src/app/shared/services/richiesta.service';
import { DeterminaToErogazioni, LeggeProvvDR, ProvvedimentoSearch, StatoFinanziamento } from 'src/app/shared/model';

@Component({
  selector: 'app-associa-determina',
  templateUrl: './associa-determina.component.html',
  styleUrls: ['./associa-determina.component.css']
})
export class AssociaDeterminaComponent implements OnInit, OnDestroy {

  resultTableRef: Ng2SmartTableComponent;
  @ViewChild('resultTable') set content(tableComponent: Ng2SmartTableComponent) {
    if (tableComponent) {
      setTimeout(() => {
        this.resultTableRef = tableComponent;
        this.resultTableRef.isAllSelected = true;
        const rowData = [];
        const rows = this.resultTableRef.grid.getRows();
        rows.forEach(row => {
          row.isSelected = true;
          rowData.push(row.getData());
        });
        this.selectRow(rowData);
      }, 0);
    }
  }

  private unsubscribe$ = new Subject();

  ricercaForm: FormGroup;
  determinaForm: FormGroup;
  lpdList: LeggeProvvDR[] = [];
  statiFinanziamento: StatoFinanziamento[] = [];

  searchFilters;
  searchResult: ProvvedimentoSearch[];
  resultTableSource = new LocalDataSource();

  determinaUrl: string;

  selectedElements: ProvvedimentoSearch[];
  loading = false;

  enableCercaDetBtn = false;
  enableAssociaBtn = false;

  resultTableColumns = {
    descLeggeProvvDr: {
      title: 'Legge - Provvedimento - D.R.',
      editable: false,
    },
    numProtocollo: {
      title: 'Prot. richiesta',
      editable: false,
    },
    dataProtocollo: {
      title: 'Data richiesta',
      editable: false,
    },
  };

  resultTableSettings = {
    columns: this.resultTableColumns,
    mode: 'external',
    selectMode: 'multi',
    actions: {
      columnTitle: 'Azioni',
      position: 'right',
      add: false,
      delete: false,
    },
    edit: {
      editButtonContent: '<i class="fa fa-file-text-o" aria-hidden="true"></i>',
    },
    // pager: {
    //   display: true,
    //   perPage: 7
    // }
    pager: {
      display: false,
    }
  };

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private messageService: MessageService,
    private anagraficaService: AnagraficaService,
    private richiestaService: RichiestaService,
  ) { }

  ngOnInit() {
    this.anagraficaService.getStatiFinanziamento().subscribe(
      res => {
        const acconto: StatoFinanziamento = res.find(stato => stato.codStatoFinanziamento === 'PERFACC');
        const saldo: StatoFinanziamento = res.find(stato => stato.codStatoFinanziamento === 'PERFSAL');
        this.statiFinanziamento.push(acconto);
        this.statiFinanziamento.push(saldo);
        this.buildForms();
      },
      err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  buildForms() {
    this.ricercaForm = this.fb.group({
      dataFrom: null,
      dataTo: null,
      lpd: null,
      statoFinanziamento: [null, Validators.required],
    });
    this.determinaForm = this.fb.group({
      numDetermina: [null, Validators.required],
      dataDetermina: [null, Validators.required],
    });
    this.determinaForm.disable();

    this.determinaForm.valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.enableAssociaBtn = false;
      this.determinaUrl = null;

      if ( this.determinaForm.get('numDetermina').value && this.determinaForm.get('dataDetermina').value ) {
        this.enableCercaDetBtn = true;
      } else {
        this.enableCercaDetBtn = false;
      }
    });
  }

  search() {
    this.searchResult = null;
    this.searchFilters = null;
    this.determinaUrl = null;
    this.enableAssociaBtn = false;
    if (this.ricercaForm.valid) {
      let dataFrom = null;
      let dataTo = null;
      const idLpd = typeof this.ricercaForm.get('lpd').value === 'string'
      ? this.ricercaForm.get('lpd').value
      : this.ricercaForm.get('lpd').value && this.ricercaForm.get('lpd').value.idLeggeProvvDr;

      if (this.ricercaForm.get('dataFrom').value) {
        const dataFromSplit = this.ricercaForm.get('dataFrom').value.split('-');
        dataFrom = dataFromSplit[2] + '/' + dataFromSplit[1] + '/' + dataFromSplit[0];
      }
      if (this.ricercaForm.get('dataTo').value) {
        const dataToSplit = this.ricercaForm.get('dataTo').value.split('-');
        dataTo = dataToSplit[2] + '/' + dataToSplit[1] + '/' + dataToSplit[0];
      }
      this.searchFilters = {
        idStatoFinanziamento: this.ricercaForm.get('statoFinanziamento').value,
        idLeggeProvvDr: idLpd,
        dataProtocolloDa: dataFrom,
        dataProtocolloA: dataTo
      };

      this.richiestaService.searchRichiesteProvvedimentiFinanziamenti(this.searchFilters).subscribe(
        res => {
          if (res) {
            this.searchResult = res;
            this.resultTableSource.load(this.searchResult);
          } else {
            this.searchResult = null;
            this.searchFilters = null;
            this.messageService.showMessage('Attenzione', 'La ricerca non ha prodotto alcun risultato.');
          }
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti.');
    }
  }

  clearForm() {
    this.ricercaForm.reset();
    this.determinaForm.reset();
    this.determinaForm.disable();
    this.searchResult = null;
    this.selectedElements = null;
    this.enableAssociaBtn = false;
    this.determinaUrl = null;
  }

  viewDetail(event) {
    // todo: land on Provv tab
    this.router.navigate(['../edit/' + event.data.idRichiesta], { relativeTo: this.activatedRoute });
  }

  selectRow(selections) {
    // this.enableAssociaBtn = false;
    // this.determinaUrl = null;
    if (selections && selections.length > 0) {
      this.selectedElements = selections;
      // this.enableAssociaBtn = true;
      this.determinaForm.enable();
    } else {
      this.selectedElements = null;
      this.determinaForm.reset();
      this.determinaForm.disable();
      this.enableAssociaBtn = false;
      this.determinaUrl = null;
    }
  }

  onCercaDetermina() {
    if (this.determinaForm.valid) {
      const numDet = Number(this.determinaForm.get('numDetermina').value);
      if ( isNaN(numDet) ) {
        this.messageService.showMessage('Errore', 'Il campo "Num. determina" deve avere un valore numerico.');
        return;
      }

      const dataSplit = this.determinaForm.get('dataDetermina').value.split('-');
      const dataDet = dataSplit[2] + '/' + dataSplit[1] + '/' + dataSplit[0];

      const filters = {
        numDetermina: numDet,
        dataDetermina: dataDet,
      };

      this.richiestaService.searchDetermina(filters).subscribe(
        res => {
          if (!res || !res.numDetermina) {
            this.messageService.showMessage('Errore', 'La determina non è stata trovata.');
            this.determinaUrl = null;
            this.enableAssociaBtn = false;
            return;
          }

          this.determinaUrl = res.url;
          this.enableAssociaBtn = true;
        },
        err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
            if (err.error.code === '49') {
              this.enableAssociaBtn = true;
            }
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti.');
    }
  }

  onConsultaAllegato() {
    window.open(this.determinaUrl, '_blank');
  }

  associaDetermina() {
    if (this.determinaForm.valid) {
      const numDet = Number(this.determinaForm.get('numDetermina').value);
      if ( isNaN(numDet) ) {
        this.messageService.showMessage('Errore', 'In campo "NUM DETERMINA" deve avere un valore numerico.');
        return;
      }

      const dataSplit = this.determinaForm.get('dataDetermina').value.split('-');
      const dataDet = dataSplit[2] + '/' + dataSplit[1] + '/' + dataSplit[0];

      const idFinanziamenti = [];
      this.selectedElements.forEach( element => idFinanziamenti.push({ idFinanziamento: element.idFinanziamento}) );

      const requestBody: DeterminaToErogazioni = {
        numDetermina: numDet,
        dataDetermina: dataDet,
        finanziamentiDaAssociare: idFinanziamenti
      };
      this.richiestaService.associaDetermina(requestBody).subscribe(
        res => {
          this.messageService.showMessage('Info', 'Determina associata correttamente.');
          // this.search();
          this.determinaForm.reset();
          this.determinaForm.disable();
          this.enableCercaDetBtn = false;
          this.enableAssociaBtn = false;
          this.determinaUrl = null;
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti.');
    }
  }

  exportCSV() {
    if (!this.searchResult) {
      return;
    }

    this.richiestaService.exportSearchResultInCSV(this.searchFilters).subscribe(
      response => {
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' }); // type: 'text/csv' ?
        const url = window.URL.createObjectURL(blob);
        const fileName = 'Elenco_Risultati' + '.xls';

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

  // print() {}

  // trigger LPD search
  searchLPD = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.loading = true),
      switchMap( input => {
        return this.richiestaService.searchLPD(input, true)
        .pipe(
          tap(list => this.lpdList = list),
          map(list => list.slice(0, 10))
        );
      }),
      tap(() => this.loading = false),
  )

  // format autocomplete options
  formatLPD = (ldp: LeggeProvvDR): string => ldp.descLeggeProvvDr;

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
