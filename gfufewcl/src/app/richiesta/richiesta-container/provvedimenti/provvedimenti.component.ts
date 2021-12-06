import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap, takeUntil, tap } from 'rxjs/operators';
import { LocalDataSource } from 'ng2-smart-table';

import { RichiestaService } from 'src/app/shared/services/richiesta.service';
import { MessageService } from 'src/app/shared/services/message.service';
import { LeggeProvvDR, Richiedente } from 'src/app/shared/model';

@Component({
  selector: 'app-provvedimenti',
  templateUrl: './provvedimenti.component.html',
  styleUrls: ['./provvedimenti.component.css']
})
export class ProvvedimentiComponent implements OnInit, OnDestroy {

  @Input() idRichiesta: number;
  @Input() richiedenti: Richiedente[];
  @Input() codProfilo: string;
  @Output() provvedimentiOut = new EventEmitter();

  private unsubscribe$ = new Subject();

  provvedimentoForm: FormGroup;

  lpdList: LeggeProvvDR[];
  lpdDeletable: LeggeProvvDR;
  lpdAddable: LeggeProvvDR;
  // showEliminaBtn: boolean;
  loading = false;

  provvedimentiTableSource = new LocalDataSource();
  tableData = [];

  provvedimentiTableColumns;
  provvedimentiTableSettings;

  searchLPD = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.loading = true),
      switchMap( input => {
        return this.richiestaService.searchLPD(input)
        .pipe(
          tap(list => this.lpdList = list),
          map(list => {
            const lpdString = [];
            if (list) {
              list.forEach( lpd => lpdString.push(lpd.descLeggeProvvDr) );
            }
            return lpdString.slice(0, 10);
          })
        );
      }),
      tap(() => this.loading = false),
    )

  constructor(
    private fb: FormBuilder,
    private sanitizer: DomSanitizer,
    private messageService: MessageService,
    private richiestaService: RichiestaService,
  ) { }

  ngOnInit() {
    this.buildForm();
    this.configTable();
    this.loadData();
  }

  loadData() {
    const checkData = this.richiedenti.some(r => !!r.provvedimentiToRichiedente);
    if (checkData) {
      this.richiestaService.getRichiedentiFull(this.idRichiesta).subscribe(
        res => {
          this.richiedenti = res;
          this.buildTableData();
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    }
  }

  buildForm() {
    this.provvedimentoForm = this.fb.group({
      lpd: [null, Validators.required]
    });

    this.provvedimentoForm.get('lpd').valueChanges.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( desc => {
      if (!this.lpdList || (this.lpdList && this.lpdList.length === 0) || !desc) {
        this.lpdAddable = undefined;
        this.lpdDeletable = undefined;
        // this.showEliminaBtn = false;
      }
      if (this.lpdList && desc && desc.length > 2) {
        // this.lpdSelectedInForm = this.lpdList.find(lpd => lpd.descLeggeProvvDr === desc);
        const selection = this.lpdList.find(lpd => lpd.descLeggeProvvDr === desc);
        if (selection) {
          const isPresent = this.tableData.some(element => element.idLeggeProvvDr === selection.idLeggeProvvDr);
          if (isPresent) {
            // this.showEliminaBtn = true;
            this.lpdDeletable = selection;
          } else {
            // this.showEliminaBtn = false;
            this.lpdAddable = selection;
          }
        } else {
          this.lpdAddable = undefined;
          this.lpdDeletable = undefined;
        }
      }
    });

    if (this.codProfilo === 'GUEST') {
      this.provvedimentoForm.disable();
    }
  }

  configTable() {
    this.provvedimentiTableColumns = {
      descComune: {
        title: 'Comune',
        sort: false,
        editable: false
      },
      popolazione: {
        title: 'Num. abitanti',
        sort: false,
        editable: false
      },
      descLeggeProvvDr: {
        title: 'Legge - Provvedimento - D.R.',
        sort: false,
        editable: false
      },
      descVincoloPopolazione: {
        title: 'Vincolo popolazione',
        sort: false,
        editable: false
      },
      documentazione: {
        title: 'Documentazione',
        type: 'html',
        sort: false,
        filter: false,
        valuePrepareFunction: (cell, row) => {
          if (cell) {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled checked>');
          } else {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled>');
          }
        },
        editor: {
          type: 'list',
          config: {
            list: [
              {
                value: true,
                title: 'Sì'
              },
              {
                value: false,
                title: 'No'
              }
            ]
          }
        }
      },
      rinuncia: {
        title: 'Rinuncia',
        type: 'html',
        sort: false,
        filter: false,
        valuePrepareFunction: (cell, row) => {
          if (cell) {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled checked>');
          } else {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled>');
          }
        },
        editor: {
          type: 'list',
          config: {
            list: [
              {
                value: true,
                title: 'Sì'
              },
              {
                value: false,
                title: 'No'
              }
            ]
          }
        }
      }
    };

    this.provvedimentiTableSettings = {
      columns: this.provvedimentiTableColumns,
      mode: 'inline',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
        add: false,
        edit: this.codProfilo !== 'GUEST',
        delete: false
      },
      edit: {
        confirmSave: true,
        editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>',
        saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      pager: {
        display: true,
        perPage: 5
      }
    };
  }

  buildTableData() {
    this.tableData = [];
    this.richiedenti.forEach( richiedente => {
      richiedente.provvedimentiToRichiedente.forEach( element => {
        this.tableData.push({
          idRichiedente: richiedente.idRichiedente,
          istatComune: richiedente.istatComune,
          descComune: richiedente.descComune,
          popolazione: richiedente.popolazione,
          idLeggeProvvDr: element.idLeggeProvvDr,
          descLeggeProvvDr: element.descLeggeProvvDr,
          descVincoloPopolazione: element.descVincoloPopolazione,
          documentazione: element.flagDocumentazione,
          rinuncia: element.flagRinuncia
        });
      });
    });
    this.provvedimentiTableSource.load(this.tableData);
    this.provvedimentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);
    this.provvedimentiOut.emit(this.richiedenti);
  }

  onAggiungi() {
    if (this.provvedimentoForm.valid) {

      if (!this.lpdAddable.isValid) {
        this.messageService.showMessage('Errore', 'Non è possibile legare la richiesta al provvedimento scelto in quanto non risulta attivo.');
        return;
      }

      const duplicated = this.tableData.some(element => element.idLeggeProvvDr === this.lpdAddable.idLeggeProvvDr);
      if (duplicated) {
        this.messageService.showMessage('Errore', 'Il provvedimento è già presente nella lista.');
        return;
      }

      this.richiestaService.postProvvToRichiedenti(this.idRichiesta, this.lpdAddable.idLeggeProvvDr).subscribe(
        resp => {
          this.richiedenti.forEach( richiedente => {
            const response = resp.find(r => r.idRichiedente === richiedente.idRichiedente);
            richiedente.provvedimentiToRichiedente = response.provvedimentiToRichiedente;
          });

          this.richiedenti.forEach( richiedente => {
            this.tableData.push({
              idRichiedente: richiedente.idRichiedente,
              istatComune: richiedente.istatComune,
              descComune: richiedente.descComune,
              popolazione: richiedente.popolazione,
              idLeggeProvvDr: this.lpdAddable.idLeggeProvvDr,
              descLeggeProvvDr: this.lpdAddable.descLeggeProvvDr,
              descVincoloPopolazione: this.lpdAddable.descVincoloPopolazione,
              documentazione: false,
              rinuncia: false
            });
          });

          this.provvedimentiTableSource.load(this.tableData);
          this.provvedimentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);
          this.provvedimentoForm.get('lpd').reset();

          this.provvedimentiOut.emit(this.richiedenti);

        }, error => {
          if (error.error && error.error.message) {
            this.messageService.showMessage('Errore', error.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    }
  }

  onEdit(event) {
    event.confirm.reject();

    const request: Richiedente = {
      idRichiedente: event.newData.idRichiedente,
      istatComune: event.newData.istatComune,
      descComune: event.newData.descComune,
      popolazione: event.newData.popolazione,
      provvedimentiToRichiedente: [
        {
          idLeggeProvvDr: event.newData.idLeggeProvvDr,
          flagDocumentazione: event.newData.documentazione === 'true' || event.newData.documentazione === true,
          flagRinuncia: event.newData.rinuncia === 'true' || event.newData.rinuncia === true
        }
      ]
    };

    this.richiestaService.putProvvToRichiedenti(this.idRichiesta, request).subscribe(
      res => {
        const richiedenteIndex = this.richiedenti.findIndex(r => r.idRichiedente === request.idRichiedente);
        const ptrIndex = this.richiedenti[richiedenteIndex].provvedimentiToRichiedente.findIndex(ptr => ptr.idLeggeProvvDr === request.provvedimentiToRichiedente[0].idLeggeProvvDr);
        this.richiedenti[richiedenteIndex].provvedimentiToRichiedente[ptrIndex] = request.provvedimentiToRichiedente[0];

        const index = this.tableData.findIndex(item => item.idRichiedente === request.idRichiedente && item.idLeggeProvvDr === request.provvedimentiToRichiedente[0].idLeggeProvvDr);
        this.tableData[index].documentazione = request.provvedimentiToRichiedente[0].flagDocumentazione;
        this.tableData[index].rinuncia = request.provvedimentiToRichiedente[0].flagRinuncia;

        this.provvedimentiTableSource.load(this.tableData);

        this.provvedimentiOut.emit(this.richiedenti);
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onElimina() {
    const modalRef = this.messageService.showConfirmation('Attenzione', 'Il provvedimento verrà eliminato per tutti i richiedenti. Sei sicuro di voler procedere?');
    modalRef.result.then(confirm => {
      if (confirm) {
        this.richiestaService.deleteProvvToRichiedenti(this.idRichiesta, this.lpdDeletable.idLeggeProvvDr).subscribe(
          response => {
            this.removeProvvedimentoFromTable(this.lpdDeletable);
            this.provvedimentiTableSource.load(this.tableData);
            this.provvedimentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);
            this.provvedimentoForm.get('lpd').reset();
            this.richiestaService.getRichiedentiFull(this.idRichiesta).subscribe(
              resp => {
                if (resp && resp.length > 0) {
                  this.richiedenti = resp;
                  this.provvedimentiOut.emit(this.richiedenti);
                } else {
                  this.richiestaService.getRichiedentiLight(this.idRichiesta).subscribe(
                    lightRes => {
                      if ( lightRes && lightRes.length > 0 ) {
                        this.richiedenti = lightRes;
                        this.provvedimentiOut.emit(this.richiedenti);
                      }
                    },
                    lightErr => {
                      if (lightErr.error && lightErr.error.message) {
                        this.messageService.showMessage('Errore', lightErr.error.message);
                      } else {
                        this.messageService.showGenericError();
                      }
                    }
                  );
                }
              }, error => {
                if (error.error && error.error.message) {
                  this.messageService.showMessage('Errore', error.error.message);
                } else {
                  this.messageService.showGenericError();
                }
              }
            );
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

  removeProvvedimentoFromTable(lpd: LeggeProvvDR) {
    this.tableData = this.tableData.filter( element => {
      if (element.idLeggeProvvDr !== lpd.idLeggeProvvDr) {
        return element;
      }
    });
    this.lpdDeletable = undefined;
  }

  // onRowSelect(payload: {data: LeggeProvvDR}): void {
  //   this.showEliminaBtn = true;
  //   this.lpdDeletable = payload.data;
  // }

  // onRowDeselect(event: any): void {
  //   this.showEliminaBtn = false;
  //   this.lpdDeletable = undefined;
  // }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
