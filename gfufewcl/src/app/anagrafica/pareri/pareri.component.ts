/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { LocalDataSource } from 'ng2-smart-table';

import { MessagePage } from 'src/app/shared/utils/message-page';
import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { Parere } from 'src/app/shared/model';

@Component({
  selector: 'app-pareri',
  templateUrl: './pareri.component.html',
  styleUrls: ['./pareri.component.css']
})
export class PareriComponent implements OnInit {

  pareriList: Parere[];
  pareriTableSource = new LocalDataSource();

  pareriTableColumns = {
    descrizione: {
      title: 'DESCRIZIONE PARERE',
    },
    isValid: {
      title: 'ATTIVO',
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

  pareriTableSettings = {
    columns: this.pareriTableColumns,
    mode: 'inline',
    actions: {

      columnTitle: 'AZIONI',
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
      perPage: 10
    }
  };

  constructor(
    private sanitizer: DomSanitizer,
    private messageService: MessageService,
    private anagraficaService: AnagraficaService
  ) { }

  ngOnInit() {
    this.anagraficaService.getPareri().subscribe(
      res => {
        this.pareriList = res;
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

  loadTableData() {
    this.pareriTableSource.load(this.pareriList);
    // this.pareriTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
  }

  onCreateElement(event) {
    event.confirm.reject();

    const check = this.pareriList.some(element => element.descrizione === event.newData.descrizione);
    if (check) {
      this.messageService.showMessage('Errore', 'Parere già esistente.');
      return;
    }

    if (event.newData.descrizione) {
      event.newData.isValid = true;
      this.anagraficaService.saveParere(event.newData).subscribe(
        res => {
          this.pareriList.push(res);
          this.loadTableData();

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
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_REQUIRED_INPUT);
    }
  }

  onEditElement(event) {
    event.confirm.reject();


    const check = this.pareriList.some(element => element.descrizione === event.newData.descrizione);
    if (check) {
      this.messageService.showMessage('Errore', 'Parere già esistente.');
      return;
    }

    if (event.newData.descrizione) {
      this.anagraficaService.saveParere(event.newData).subscribe(
        res => {
          const index = this.pareriList.findIndex(parere => parere.idParere === res.idParere);
          this.pareriList[index] = res;
          this.pareriTableSource.load(this.pareriList);

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
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_REQUIRED_INPUT);
    }
  }

  onDeleteElement(event) {

    event.confirm.reject();

    const modalRef = this.messageService.showConfirmation('Attenzione', MessagePage.MSG_WARN_TO_LOGICDELETE);
    modalRef.result.then(confirm => {
      if (confirm) {
        event.data.isValid = false;
        this.anagraficaService.saveParere(event.data).subscribe(
          res => {
            const index = this.pareriList.findIndex(parere => parere.idParere === res.idParere);
            this.pareriList[index] = res;
            this.pareriTableSource.load(this.pareriList);
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
}
