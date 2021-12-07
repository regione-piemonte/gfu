/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Component, OnInit } from '@angular/core';
import { LocalDataSource } from 'ng2-smart-table';

import { MessagePage } from 'src/app/shared/utils/message-page';
import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { Profilo, Utente } from 'src/app/shared/model';

@Component({
  selector: 'app-utenti',
  templateUrl: './utenti.component.html',
  styleUrls: ['./utenti.component.css']
})
export class UtentiComponent implements OnInit {

  utentiList: Utente[] = [];
  profiliList: Profilo[] = [];

  profiliTableList = [];

  utentiTableColumns;
  utentiTableSettings;

  utentiTableSource = new LocalDataSource();

  constructor(private messageService: MessageService, private anagraficaService: AnagraficaService) { }

  ngOnInit() {
    this.anagraficaService.getProfili().subscribe(
      res => {
        this.profiliList = res;
        this.profiliList.forEach( profilo => {
          this.profiliTableList.push({
            value: profilo.idProfilo,
            title: profilo.descrizioneProfilo
          });
        });
        this.configTable();
        this.getUtenti();
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  configTable() {
    this.utentiTableColumns = {
      cognome: {
        title: 'Cognome',
        editable: true
      },
      nome: {
        title: 'Nome',
        editable: true
      },
      codiceFiscale: {
        title: 'Cod. fiscale',
        editable: true
      },
      email: {
        title: 'EMail',
        editable: true
      },
      profilo: {
        title: 'Profilo',
        editable: true,
        editor: {
          type: 'list',
          config: {
            list: this.profiliTableList
          }
        },
        valuePrepareFunction: (cell, row) => {
          return cell.descrizioneProfilo;
        },
      },
      dataInserimento: {
        title: 'DATA INSERIMENTO',
        editable: false,
        addable: false,
      },
      dataCancellazione: {
        title: 'DATA CANCELLAZIONE',
        editable: false,
        addable: false,
      },
    };

    this.utentiTableSettings = {
      columns: this.utentiTableColumns,
      mode: 'inline',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
      },
      add: {
        confirmCreate: true,
        addButtonContent: '<i class="fa fa-plus" aria-hidden="true" title="aggiungi"></i>' ,
        createButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>' ,
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      delete: {
        confirmDelete: true,
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      edit: {
        confirmSave: true,
        editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>' ,
        saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>' ,
        cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
      },
      pager: {
        display: true,
        perPage: 10
      },
      rowClassFunction: (row) => {
        if (row.data.dataCancellazione) {
          return 'hidden-actions';
        }
      }
    };
  }

  getUtenti() {
    this.anagraficaService.getUtenti().subscribe(
      res => {
        this.utentiList = res;
        this.utentiTableSource.load(this.utentiList);
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onCreateElement(event) {
    event.confirm.reject();

    const check = this.utentiList.some(element => element.codiceFiscale === event.newData.codiceFiscale);
    if (check) {
      this.messageService.showMessage('Errore', 'Utente già esistente.');
      return;
    }

    if (event.newData.cognome && event.newData.codiceFiscale && event.newData.profilo) {
      event.newData.profilo = this.profiliList.find(prof => prof.idProfilo === Number(event.newData.profilo));
      this.anagraficaService.saveUtente(event.newData).subscribe(
        res => {
          this.utentiList.push(res);
          this.utentiTableSource.load(this.utentiList);

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

    if (event.newData.cognome && event.newData.codiceFiscale && event.newData.profilo) {
      event.newData.profilo = this.profiliList.find(prof => prof.idProfilo === Number(event.newData.profilo));
      this.anagraficaService.saveUtente(event.newData).subscribe(
        res => {
          const index = this.utentiList.findIndex(utente => utente.idUtente === res.idUtente);
          this.utentiList[index] = res;
          this.utentiTableSource.load(this.utentiList);

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

    const mess = 'L\'utente sarà eliminato. Vuoi proseguire?';
    const modalRef = this.messageService.showConfirmation('Attenzione', mess);
    modalRef.result.then(confirm => {
      if (confirm) {
        this.anagraficaService.deleteUtente(event.data.idUtente).subscribe(
          res => {
            const index = this.utentiList.findIndex(utente => utente.idUtente === res.idUtente);
            // this.utentiList.splice(index, 1);
            this.utentiList[index] = res;
            this.utentiTableSource.load(this.utentiList);

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
