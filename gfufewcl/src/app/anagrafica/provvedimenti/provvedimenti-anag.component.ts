/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { forkJoin, Subject } from 'rxjs';
import { distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { LocalDataSource } from 'ng2-smart-table';

import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { DR, Legge, LeggeProvvDR, Provvedimento, TipoDR, VincoloPopolazione } from 'src/app/shared/model';

import { NgbAccordion, NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-provvedimenti-anag',
  templateUrl: './provvedimenti-anag.component.html',
  styleUrls: ['./provvedimenti-anag.component.css']
})
export class ProvvedimentiAnagComponent implements OnInit, OnDestroy {

  private unsubscribe$ = new Subject();

  viewReady = false;

  tipiDrList: TipoDR[];
  tipiDrTableList = [];
  drList: DR[];
  drTableList = [];
  drForm: FormGroup;

  leggiList: Legge[];
  leggiTableList = [];

  provvedimentiList: Provvedimento[];
  provvedimentiTableList = [];

  vincoliList: VincoloPopolazione[];
  vincoliTableList = [];

  leggeProvvDrList: LeggeProvvDR[];

  drTableSource = new LocalDataSource();
  leggiTableSource = new LocalDataSource();
  provvTableSource = new LocalDataSource();
  leggeProvvDrTableSource = new LocalDataSource();

  drTableColumns;
  leggiTableColumns;
  provvTableColumns;
  leggeProvvDrTableColumns;

  drTableSettings;
  leggiTableSettings;
  provvTableSettings;
  leggeProvvDrTableSettings;

  constructor(
    private sanitizer: DomSanitizer,
    private fb: FormBuilder,
    private messageService: MessageService,
    private anagraficaService: AnagraficaService
  ) { }

  ngOnInit() {
    this.fetchLists();
    this.buildDrForm();
  }

  // activeIds: string[] =[];
 // panels = [0,1,2,3]





  fetchLists() {
    const getTipiDr = this.anagraficaService.getTipiDr();
    const getDr = this.anagraficaService.getDr();
    const getLeggi = this.anagraficaService.getLeggi();
    const getProvv = this.anagraficaService.getProvvedimenti();
    const getVincoli = this.anagraficaService.getVincoliPopolazione();
    const getLPD = this.anagraficaService.getLeggeProvvDr();

    forkJoin([getTipiDr, getDr, getLeggi, getProvv, getVincoli, getLPD]).subscribe(
      res => {
        this.tipiDrList = res[0];
        this.drList = res[1];
        this.leggiList = res[2];
        this.provvedimentiList = res[3];
        this.vincoliList = res[4];
        this.leggeProvvDrList = res[5];

        this.buildTableEditLists();
        this.configTables();
        this.viewReady = true;
        this.loadTableData();
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  buildTableEditLists() {
    this.tipiDrTableList = [];
    this.tipiDrList.forEach( tipoDr => {
      this.tipiDrTableList.push(
        {
          value: tipoDr.idTipoDr,
          title: tipoDr.descTipoDr
        }
      );
    });

    this.drTableList = [];
    this.drList.forEach( dr => {
      this.drTableList.push(
        {
          value: dr.idDr,
          title: dr.descDr
        }
      );
    });

    this.leggiTableList = [];
    this.leggiList.forEach( legge => {
      this.leggiTableList.push(
        {
          value: legge.idLegge,
          title: legge.descrizione
        }
      );
    });

    this.provvedimentiTableList = [];
    this.provvedimentiList.forEach( p => {
      this.provvedimentiTableList.push(
        {
          value: p.idProvvedimento,
          title: p.descProvvedimento
        }
      );
    });

    this.vincoliTableList = [];
    this.vincoliList.forEach( vincolo => {
      this.vincoliTableList.push(
        {
          value: vincolo.idVincoloPopolazione,
          title: vincolo.descrizione
        }
      );
    });
  }

  configTables() {
    /* --- TABLE COLUMNS --- */
    this.drTableColumns = {
      idTipoDr: {
        title: 'Tipo Direttiva Regionale',
        valuePrepareFunction: (cell, row) => {
          return this.valuePrepareTipoDr(cell);
        },
        filterFunction: (cell?: any, search?: string) => {
          if (search.length > 0) {
            return this.filterTipoDr(cell, search);
          }
        },
        editor: {
          type: 'list',
          config: {
            list: this.tipiDrTableList
          }
        }
      },
      numeroDr: {
        title: 'Numero'
      },
      dataDr: {
        title: 'Data'
      },
      descDr: {
        title: 'Descrizione',
      },
      isValid: {
        title: 'Attivo',
        type: 'html',
        editable: false,
        addable: false,
        filter: false,
        valuePrepareFunction: (cell, row) => {
          if (cell) {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled checked>');
          } else {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled>');
          }
        }
      }
    };

    this.leggiTableColumns = {
      descrizione: {
        title: 'Descrizione',
      },
      isValid: {
        title: 'Attivo',
        type: 'html',
        editable: false,
        addable: false,
        filter: false,
        valuePrepareFunction: (cell, row) => {
          if (cell) {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled checked>');
          } else {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled>');
          }
        }
      }
    };

    this.provvTableColumns = {
      descProvvedimento: {
        title: 'Descrizione',
      },
      isValid: {
        title: 'Attivo',
        type: 'html',
        editable: false,
        addable: false,
        filter: false,
        valuePrepareFunction: (cell, row) => {
          if (cell) {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled checked>');
          } else {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled>');
          }
        }
      }
    };

    this.leggeProvvDrTableColumns = {
      idLegge: {
        title: 'Leggi',
        valuePrepareFunction: (cell, row) => {
          return this.valuePrepareLegge(cell);
        },
        filterFunction: (cell?: any, search?: string) => {
          if (search.length > 0) {
            return this.filterLegge(cell, search);
          }
        },
        editor: {
          type: 'list',
          config: {
            list: this.leggiTableList
          }
        }
      },
      idProvvedimento: {
        title: 'Provvedimento',
        valuePrepareFunction: (cell, row) => {
          return this.valuePrepareProvvedimento(cell);
        },
        filterFunction: (cell?: any, search?: string) => {
          if (search.length > 0) {
            return this.filterProvvedimento(cell, search);
          }
        },
        editor: {
          type: 'list',
          config: {
            list: this.provvedimentiTableList
          }
        }
      },
      idDr: {
        title: 'D.R.',
        valuePrepareFunction: (cell, row) => {
          return this.valuePrepareDr(cell);
        },
        filterFunction: (cell?: any, search?: string) => {
          if (search.length > 0) {
            return this.filterDr(cell, search);
          }
        },
        editor: {
          type: 'list',
          config: {
            list: this.drTableList
          }
        }
      },
      idVincoloPopolazione: {
        title: 'Popolazione',
        valuePrepareFunction: (cell, row) => {
          return this.valuePrepareVincolo(cell);
        },
        filterFunction: (cell?: any, search?: string) => {
          if (search.length > 0) {
            return this.filterVincolo(cell, search);
          }
        },
        editor: {
          type: 'list',
          config: {
            list: this.vincoliTableList
          }
        }
      },
      isValid: {
        title: 'Attivo',
        type: 'html',
        editable: false,
        addable: false,
        filter: false,
        valuePrepareFunction: (cell, row) => {
          if (cell) {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled checked>');
          } else {
            return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" disabled>');
          }
        }
      }
    };

    /* --- TABLE SETTINGS --- */
    this.drTableSettings = {
      columns: this.drTableColumns,
      mode: 'inline',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
        add: false
      },
      delete: {
        confirmDelete: true,
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      edit: {
        confirmSave: true,
        editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>',
        saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      pager: {
        display: true,
        perPage: 4
      }
    };

    this.leggiTableSettings = {
      columns: this.leggiTableColumns,
      mode: 'inline',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
      },
      add: {
        confirmCreate: true,
        addButtonContent: '<i class="fa fa-plus" aria-hidden="true" title="aggiungi"></i>',
        createButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      delete: {
        confirmDelete: true,
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      edit: {
        confirmSave: true,
        editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>',
        saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      pager: {
        display: true,
        perPage: 4
      }
    };

    this.provvTableSettings = {
      columns: this.provvTableColumns,
      mode: 'inline',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
      },
      add: {
        confirmCreate: true,
        addButtonContent: '<i class="fa fa-plus" aria-hidden="true" title="aggiungi"></i>',
        createButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      delete: {
        confirmDelete: true,
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      edit: {
        confirmSave: true,
        editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>',
        saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      pager: {
        display: true,
        perPage: 4
      }
    };

    this.leggeProvvDrTableSettings = {
      columns: this.leggeProvvDrTableColumns,
      mode: 'inline',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
      },
      add: {
        confirmCreate: true,
        addButtonContent: '<i class="fa fa-plus" aria-hidden="true" title="aggiungi"></i>',
        createButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      delete: {
        confirmDelete: true,
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      edit: {
        confirmSave: true,
        editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>',
        saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      pager: {
        display: true,
        perPage: 4
      }
    };
  }

  valuePrepareTipoDr(cellValue) {
    const listItem = this.tipiDrTableList.find(item => item.value === cellValue);
    return listItem ? listItem.title : null;
  }

  filterTipoDr(cellValue, searchString) {
    const tipoDr = this.drList.find(element => element.idTipoDr === cellValue);
    return tipoDr.descTipoDr ? tipoDr.descTipoDr.toLowerCase().includes(searchString.toLowerCase()) : false;
  }

  valuePrepareLegge(cellValue) {
    const listItem = this.leggiTableList.find(item => item.value === cellValue);
    return listItem ? listItem.title : null;
  }

  filterLegge(cellValue, searchString) {
    const lpd = this.leggeProvvDrList.find(element => element.idLegge === cellValue);
    return lpd.descLegge ? lpd.descLegge.toLowerCase().includes(searchString.toLowerCase()) : false;
  }

  valuePrepareProvvedimento(cellValue) {
    const listItem = this.provvedimentiTableList.find(item => item.value === cellValue);
    return listItem ? listItem.title : null;
  }

  filterProvvedimento(cellValue, searchString) {
    const lpd = this.leggeProvvDrList.find(element => element.idProvvedimento === cellValue);
    return lpd.descProvvedimento ? lpd.descProvvedimento.toLowerCase().includes(searchString.toLowerCase()) : false;
  }

  valuePrepareDr(cellValue) {
    const listItem = this.drTableList.find(item => item.value === cellValue);
    return listItem ? listItem.title : null;
  }

  filterDr(cellValue, searchString) {
    const lpd = this.leggeProvvDrList.find(element => element.idDr === cellValue);
    return lpd.descDr ? lpd.descDr.toLowerCase().includes(searchString.toLowerCase()) : null;
  }

  valuePrepareVincolo(cellValue) {
    const listItem = this.vincoliTableList.find(item => item.value === cellValue);
    return listItem ? listItem.title : null;
  }

  filterVincolo(cellValue, searchString) {
    const lpd = this.leggeProvvDrList.find(element => element.idVincoloPopolazione === cellValue);
    return lpd.descVincoloPopolazione ? lpd.descVincoloPopolazione.toLowerCase().includes(searchString.toLowerCase()) : false;
  }

  loadTableData() {
    this.drTableSource.load(this.drList);
    // this.drTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
    this.leggiTableSource.load(this.leggiList);
    // this.leggiTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
    this.provvTableSource.load(this.provvedimentiList);
    // this.provvTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
    this.leggeProvvDrTableSource.load(this.leggeProvvDrList);
    // this.leggeProvvDrTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
  }

  refreshLPD() {
    this.anagraficaService.getLeggeProvvDr().subscribe(
      res => {
        this.leggeProvvDrList = res;
        this.leggeProvvDrTableSource.load(res);
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  buildDrForm() {
    this.drForm = this.fb.group({
      tipoDr: '',
      numeroDr: '',
      dataDr: '',
      descrizioneDr: ''
    });

    this.drForm.get('tipoDr').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.buildDrDescription();
    });

    this.drForm.get('numeroDr').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.buildDrDescription();
    });

    this.drForm.get('dataDr').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.buildDrDescription();
    });
  }

  buildDrDescription() {
    let descrizione = this.drForm.get('tipoDr').value.descTipoDr + ' ' + this.drForm.get('numeroDr').value;
    if (this.drForm.get('dataDr').value) {
      const date = this.formatDate(this.drForm.get('dataDr').value);
      descrizione = descrizione + ' ' + date;
    }
    this.drForm.get('descrizioneDr').setValue( descrizione.replace(/\s+/g, ' ').trim() );
  }

  formatDate(inputDate) {
    const splitDate = inputDate.split('-');
    const formattedDate = splitDate[2] + '/' + splitDate[1] + '/' + splitDate[0];
    return formattedDate;
  }

  addDr() {
    const check = this.drList.some(dr => dr.descDr === this.drForm.get('descrizioneDr').value);
    if (check) {
      this.messageService.showMessage('Errore', 'In elenco è già presente una direttiva con la stessa descrizione.');
      return;
    }

    const newDr: DR = {
      idTipoDr: this.drForm.get('tipoDr').value.idTipoDr,
      descTipoDr: this.drForm.get('tipoDr').value.descrizione,
      numeroDr: this.drForm.get('numeroDr').value,
      dataDr: this.formatDate(this.drForm.get('dataDr').value),
      descDr: this.drForm.get('descrizioneDr').value,
      isValid: true
    };

    this.anagraficaService.saveDr(newDr).subscribe(
      res => {
        this.drList.unshift(res);
        this.drTableSource.load(this.drList);
        // this.drTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
        this.buildTableEditLists();
        this.configTables();
        this.refreshLPD();
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onEditDr(event) {
    event.confirm.reject();

    const check = this.drList.some(dr => dr.descDr === this.drForm.get('descrizioneDr').value);
    if (check) {
      this.messageService.showMessage('Errore', 'In elenco è già presente una direttiva con la stessa descrizione.');
      return;
    }

    if (event.newData.descDr) {
      this.anagraficaService.saveDr(event.newData).subscribe(
        res => {
          const index = this.drList.findIndex(dr => dr.idDr === res.idDr);
          this.drList[index] = res;
          this.drTableSource.load(this.drList);
          this.buildTableEditLists();
          this.configTables();
          this.refreshLPD();

          // close the edit row (still open at this point) by clicking the X icon:
          const a: any = document.getElementsByClassName('ng2-smart-action-edit-cancel')[0];
          a.click();

        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti. Modifiche non salvate.');
    }
  }

  onDeleteDr(event) {
    event.confirm.reject();

    const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler rendere l\'elemento non più attivo?');
    modalRef.result.then(confirm => {
      if (confirm) {
        event.data.isValid = false;
        this.anagraficaService.saveDr(event.data).subscribe(
          res => {
            const index = this.drList.findIndex(dr => dr.idDr === res.idDr);
            this.drList[index] = res;
            this.drTableSource.load(this.drList);
            this.buildTableEditLists();
            this.configTables();
            this.refreshLPD();
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

  onCreateLegge(event) {
    event.confirm.reject();

    const check = this.leggiList.some(element => element.descrizione === event.newData.descrizione);
    if (check) {
      this.messageService.showMessage('Errore', 'Legge già esistente.');
      return;
    }

    if (event.newData.descrizione) {
      event.newData.isValid = true;
      this.anagraficaService.saveLegge(event.newData).subscribe(
        res => {
          this.leggiList.unshift(res);
          this.leggiTableSource.load(this.leggiList);
          // this.leggiTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
          this.buildTableEditLists();
          this.configTables();
          this.refreshLPD();

          // close the add row (still open at this point) by clicking the X icon:
          const a: any = document.getElementsByClassName('ng2-smart-action-add-cancel')[0];
          a.click();

        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti. Legge non aggiunta.');
    }
  }

  onEditLegge(event) {
    event.confirm.reject();

    const check = this.leggiList.some(element => element.descrizione === event.newData.descrizione);
    if (check) {
      this.messageService.showMessage('Errore', 'Legge già esistente.');
      return;
    }

    if (event.newData.descrizione) {
      this.anagraficaService.saveLegge(event.newData).subscribe(
        res => {
          const index = this.leggiList.findIndex(legge => legge.idLegge === res.idLegge);
          this.leggiList[index] = res;
          this.leggiTableSource.load(this.leggiList);
          this.buildTableEditLists();
          this.configTables();
          this.refreshLPD();

          // close the edit row (still open at this point) by clicking the X icon:
          const a: any = document.getElementsByClassName('ng2-smart-action-edit-cancel')[0];
          a.click();

        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti. Modifiche non salvate.');
    }
  }

  onDeleteLegge(event) {
    event.confirm.reject();

    const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler rendere l\'elemento non più attivo?');
    modalRef.result.then(confirm => {
      if (confirm) {
        event.data.isValid = false;
        this.anagraficaService.saveLegge(event.data).subscribe(
          res => {
            const index = this.leggiList.findIndex(legge => legge.idLegge === res.idLegge);
            this.leggiList[index] = res;
            this.leggiTableSource.load(this.leggiList);
            this.buildTableEditLists();
            this.configTables();
            this.refreshLPD();
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

  onCreateProvvedimento(event) {
    event.confirm.reject();

    const check = this.provvedimentiList.some(element => element.descProvvedimento === event.newData.descProvvedimento);
    if (check) {
      this.messageService.showMessage('Errore', 'Provvedimento già esistente.');
      return;
    }

    if (event.newData.descProvvedimento) {
      event.newData.isValid = true;
      this.anagraficaService.saveProvvedimento(event.newData).subscribe(
        res => {
          this.provvedimentiList.unshift(res);
          this.provvTableSource.load(this.provvedimentiList);
          // this.provvTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
          this.buildTableEditLists();
          this.configTables();
          this.refreshLPD();

          // close the add row (still open at this point) by clicking the X icon:
          const a: any = document.getElementsByClassName('ng2-smart-action-add-cancel')[0];
          a.click();

        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti. Provvedimento non aggiunto.');
    }
  }

  onEditProvvedimento(event) {
    event.confirm.reject();

    const check = this.provvedimentiList.some(element => element.descProvvedimento === event.newData.descProvvedimento);
    if (check) {
      this.messageService.showMessage('Errore', 'Provvedimento già esistente.');
      return;
    }

    if (event.newData.descProvvedimento) {
      this.anagraficaService.saveProvvedimento(event.newData).subscribe(
        res => {
          const index = this.provvedimentiList.findIndex(provv => provv.idProvvedimento === res.idProvvedimento);
          this.provvedimentiList[index] = res;
          this.provvTableSource.load(this.provvedimentiList);
          this.buildTableEditLists();
          this.configTables();
          this.refreshLPD();

          // close the edit row (still open at this point) by clicking the X icon:
          const a: any = document.getElementsByClassName('ng2-smart-action-edit-cancel')[0];
          a.click();

        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti. Modifiche non salvate.');
    }
  }

  onDeleteProvvedimento(event) {
    event.confirm.reject();

    const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler rendere l\'elemento non più attivo?');
    modalRef.result.then(confirm => {
      if (confirm) {
        event.data.isValid = false;
        this.anagraficaService.saveProvvedimento(event.data).subscribe(
          res => {
            const index = this.provvedimentiList.findIndex(provv => provv.idProvvedimento === res.idProvvedimento);
            this.provvedimentiList[index] = res;
            this.provvTableSource.load(this.provvedimentiList);
            this.buildTableEditLists();
            this.configTables();
            this.refreshLPD();
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

  onSaveLPD(event, editMode = false) {
    event.confirm.reject();

    const check = this.leggeProvvDrList.some( element =>
      element.idLegge === Number(event.newData.idLegge) &&
      element.idProvvedimento === Number(event.newData.idProvvedimento) &&
      element.idDr === Number(event.newData.idDr) &&
      element.idVincoloPopolazione === Number(event.newData.idVincoloPopolazione)
    );
    if (check) {
      // this.messageService.showMessage('Errore', 'Elemento già esistente.');

      // close the edit row (still open at this point) by clicking the X icon:
      const a: any = document.getElementsByClassName('ng2-smart-action-edit-cancel')[0];
      a.click();
      return;
    }

    if (event.newData.idLegge && event.newData.idProvvedimento && event.newData.idDr) {

      const legge = this.leggiList.find(l => l.idLegge === Number(event.newData.idLegge));
      const provvedimento = this.provvedimentiList.find(p => p.idProvvedimento === Number(event.newData.idProvvedimento));
      const dr = this.drList.find(d => d.idDr === Number(event.newData.idDr));

      const newLPD: LeggeProvvDR = {
        idLeggeProvvDr: editMode ? event.newData.idLeggeProvvDr : null,
        descLeggeProvvDr: legge.descrizione + ' - ' + provvedimento.descProvvedimento + ' - ' + dr.descDr,
        idLegge: legge.idLegge,
        descLegge: legge.descrizione,
        idProvvedimento: provvedimento.idProvvedimento,
        descProvvedimento: provvedimento.descProvvedimento,
        idDr: dr.idDr,
        descDr: dr.descDr,
        isValid: editMode ? event.newData.isValid : true
      };

      if (event.newData.idVincoloPopolazione) {
        const vincolo = this.vincoliList.find(v => v.idVincoloPopolazione === Number(event.newData.idVincoloPopolazione));
        newLPD.idVincoloPopolazione = vincolo.idVincoloPopolazione;
        newLPD.descVincoloPopolazione = vincolo.descrizione;
      }

      if (editMode) {
        this.editLPD(newLPD);
      } else {
        this.createLPD(newLPD);
      }

    } else {
      const mess = editMode ? 'Modifiche non salvate.' : 'Elemento non aggiunto.';
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti. ' + mess);
    }
  }

  createLPD(newLPD) {
    this.anagraficaService.saveLeggeProvvDr(newLPD).subscribe(
      res => {
        this.leggeProvvDrList.unshift(res);
        this.leggeProvvDrTableSource.load(this.leggeProvvDrList);
        // this.leggeProvvDrTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);

        // close the add row (still open at this point) by clicking the X icon:
        const a: any = document.getElementsByClassName('ng2-smart-action-add-cancel')[0];
        a.click();

      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  editLPD(editedLPD) {
    this.anagraficaService.saveLeggeProvvDr(editedLPD).subscribe(
      res => {
        const index = this.leggeProvvDrList.findIndex(el => el.idLeggeProvvDr === res.idLeggeProvvDr);
        this.leggeProvvDrList[index] = res;
        this.leggeProvvDrTableSource.load(this.leggeProvvDrList);

        // close the edit row (still open at this point) by clicking the X icon:
        const a: any = document.getElementsByClassName('ng2-smart-action-edit-cancel')[0];
        a.click();

      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onDeleteLPD(event) {
    event.confirm.reject();

    const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler rendere l\'elemento non più attivo?');
    modalRef.result.then(confirm => {
      if (confirm) {
        event.data.isValid = false;
        this.anagraficaService.saveLeggeProvvDr(event.data).subscribe(
          res => {
            const index = this.leggeProvvDrList.findIndex(el => el.idLeggeProvvDr === res.idLeggeProvvDr);
            this.leggeProvvDrList[index] = res;
            this.leggeProvvDrTableSource.load(this.leggeProvvDrList);
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

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
