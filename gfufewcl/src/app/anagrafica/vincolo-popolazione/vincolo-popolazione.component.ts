/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { LocalDataSource } from 'ng2-smart-table';

import { MessagePage } from 'src/app/shared/utils/message-page';
import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { VincoloPopolazione } from 'src/app/shared/model';


@Component({
  selector: 'app-vincolo-popolazione',
  templateUrl: './vincolo-popolazione.component.html',
  styleUrls: ['./vincolo-popolazione.component.css']
})
export class VincoloPopolazioneComponent implements OnInit {

  vincoloPopolazioneList: VincoloPopolazione[];
  vincoloPopolazioneTableSource = new LocalDataSource();

  vincoloPopolazioneTableColumns = {
    segno: {
      title: 'Segno',
      type: 'html',
      editor: {
        type: 'list',
        config: {
          selectText: 'Show all',
          list: [
            { value: '>', title: '>' },
            { value: '<', title: '<' },
          ]
        },
      }
    },
    popolazione: {
      title: 'Abitanti',
      type: 'number'
    },
    isValid: {
      title: 'Attivo',
      editable: false,
      addable: false,
      type: 'html',
      valuePrepareFunction: (cell, row) => {
        if (cell) {
          return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox"  checked  onclick="return false" >');
        } else {
          return this.sanitizer.bypassSecurityTrustHtml('<input type="checkbox" onclick="return false"  >');
        }
      },
      editor: {
        type: 'checkbox',
        config: {
          true: true,
          false: false,
        },
      },
    }
  };

  vincoloPopolazioneTableSettings = {
    columns: this.vincoloPopolazioneTableColumns,
    mode: 'inline',
    actions: {
      columnTitle: 'Azioni',
      position: 'right',
    },
    add: {
      confirmCreate: true,
      addButtonContent: '<i class="fa fa-plus" aria-hidden="true" title="aggiungi"></i>',
      createButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
      cancelButtonContent: '<i class="fas fa-undos" aria-hidden="true"></i>',
    },
    delete: {
      confirmDelete: true,
      deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
    },
    edit: {
      confirmSave: true,
      editButtonContent: '<i class="fa fa-pencil" aria-hidden="true" title="modifica"></i>',
      saveButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i>',
      cancelButtonContent: '<i class="fas fa-undos" aria-hidden="true"></i>',
    },
    pager: {
      display: true,
      perPage: 10
    }
  };

  constructor(
    private sanitizer: DomSanitizer,
    private anagraficaService: AnagraficaService,
    private messageService: MessageService,
  ) { }

  ngOnInit() {
    this.anagraficaService.getVincoliPopolazione().subscribe(
      res => {
        this.vincoloPopolazioneList = res;
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
    this.vincoloPopolazioneTableSource = new LocalDataSource(this.vincoloPopolazioneList);
    // this.vincoloPopolazioneTableSource.setSort([{ field: 'isValid', direction: 'desc' }]);
  }

  onCreateElement(event) {
    event.confirm.reject();

    const check = this.vincoloPopolazioneList.some(element => (element.popolazione === event.newData.popolazione &&
                                                                element.segno === event.newData.segno));
    if (check) {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_DUPLICATE_RECORD);
      return;
    }

    if (event.newData.popolazione) {
      event.newData.isValid = true;
      this.anagraficaService.saveVincoloPopolazione(event.newData).subscribe(
        res => {
          this.vincoloPopolazioneList.push(res);
          this.loadTableData();
          const a: any = document.getElementsByClassName('ng2-smart-action-add-cancel')[0];
          a.click();

        }, err => {
          console.log('Si è verificato un errore durante la richiesta.');
        }
      );
    } else {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_REQUIRED_INPUT);
    }
  }

  onEditElement(event) {
    event.confirm.reject();

    const check = this.vincoloPopolazioneList.some(element => element.popolazione === event.newData.popolazione);
    if (check) {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_RECORD_ALREADY_EXIST);
      return;
    }

    if (event.newData.popolazione) {
      this.anagraficaService.saveVincoloPopolazione(event.newData).subscribe(
        res => {
          const index = this.vincoloPopolazioneList.findIndex(vincoloPopolazione => vincoloPopolazione.idVincoloPopolazione === res.idVincoloPopolazione);
          this.vincoloPopolazioneList[index] = res;
          this.vincoloPopolazioneTableSource.load(this.vincoloPopolazioneList);
          const a: any = document.getElementsByClassName('ng2-smart-action-edit-cancel')[0];
          a.click();

        }, err => {
          console.log('Si è verificato un errore durante la richiesta.');
        }
      );
    } else {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_REQUIRED_INPUT);
    }
  }

  onDeleteElement(event) {
    event.confirm.reject();
    const check = event.data.isValid;
    if (check) {
      const modalRef = this.messageService.showConfirmation('Attenzione', MessagePage.MSG_WARN_TO_LOGICDELETE);
      modalRef.result.then(confirm => {
        if (confirm) {
          event.data.isValid = false;
          this.anagraficaService.saveVincoloPopolazione(event.data).subscribe(
            res => {
              const index = this.vincoloPopolazioneList.findIndex(vincoloPopolazione => vincoloPopolazione.idVincoloPopolazione === res.idVincoloPopolazione);
              this.vincoloPopolazioneList[index] = res;
              this.vincoloPopolazioneTableSource.load(this.vincoloPopolazioneList);
            }, err => {
              console.log('Si è verificato un errore durante la richiesta.');
            }
          );
        }
      });
    } else {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN__RECORD_ALREADY_DEL);
      return;
    }
  }

}
