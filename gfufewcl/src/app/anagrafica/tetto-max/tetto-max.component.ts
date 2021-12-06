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
import { TettoMax} from 'src/app/shared/model';

@Component({
  selector: 'app-tetto-max',
  templateUrl: './tetto-max.component.html',
  styleUrls: ['./tetto-max.component.css']
})
export class TettoMaxComponent implements OnInit {

  tettoMaxList: TettoMax[];
  tettoMaxTableSource = new LocalDataSource();

  tettoMaxTableColumns = {
    importo: {
      title: 'Importo',
      type: 'number',
    },
    valuta: {
      title: 'Valuta',
      type: 'string',
      editable: false,
      modify: false,
      addable: false
    },
    dataInizio: {
      title: 'Data Inizio',
      editable: false,
      modify: false,
      addable: false
    },
    dataFine: {
      title: 'Data Fine',
      editable: false,
      modify: false,
      addable: false
    }
  };
  tettoMaxTableSettings = {
    columns: this.tettoMaxTableColumns,
    mode: 'inline',

    actions: {
      columnTitle: 'Azioni',
      position: 'right',
    },
    add: {
      confirmCreate: true,
      addButtonContent: '<i class="fa fa-plus" aria-hidden="true" title="aggiungi"></i>',
      createButtonContent: '<i class="fa fa-floppy-o" aria-hidden="true" title="salva"></i> ',
      cancelButtonContent: '<i class="fas fa-undo" aria-hidden="true" title="cancella"></i>',
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


  constructor(private sanitizer: DomSanitizer,
    private anagraficaService: AnagraficaService,
    private messageService: MessageService) { }


  ngOnInit() {
    this.anagraficaService.getAllTettoMax().subscribe(
      res => {
        this.tettoMaxList = res;
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
    this.tettoMaxTableSource = new LocalDataSource(this.tettoMaxList);
    //this.tettoMaxTableSource.setSort([{ field: 'dataInizio', direction: 'desc' }]);
  }

  onCreateElement(event) {
    console.log('Si');
    //event.confirm.reject();

    console.log('Si', event.newData.tettoMax);
    if (event.newData.importo) {
 /*     const check = this.tettoMaxList.some(element => element.importo === null);
    if (check) {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_REQUIRED_INPUT);
      return;
    }*/
      this.anagraficaService.saveTettoMax(event.newData).subscribe(
        res => {
          this.tettoMaxList.push(res);
          this.loadTableData();
          const a: any = document.getElementsByClassName('ng2-smart-action-add-cancel')[0];
          a.click();

        }, err => {
          console.log('Si è verificato un errore durante la richiesta.');
        }
      );
    }/* else {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN_REQUIRED_INPUT);
    }*/

  }

  onEditElement(event) {
    event.confirm.reject();

    //const check = this.tettoMaxList.some(element => element.importo === event.newData.importo);

    if (event.newData.importo) {
      this.anagraficaService.saveTettoMax(event.newData).subscribe(
        res => {
          const index = this.tettoMaxList.findIndex(tettoMax => tettoMax.idTettoMax === res.idTettoMax);
          this.tettoMaxList[index] = res;
          this.tettoMaxTableSource.load(this.tettoMaxList);
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
    /*
    event.confirm.reject();
    const check = event.data.isValid;
    if (check) {
      const modalRef = this.messageService.showConfirmation('Attenzione', MessagePage.MSG_WARN_TO_LOGICDELETE);
      modalRef.result.then(confirm => {
        if (confirm) {
          event.data.isValid = false;
          this.anagraficaService.saveTettoMax(event.data).subscribe(
            res => {
              const index = this.tettoMaxList.findIndex(tettoMax => tettoMax.idTettoMax === res.idTettoMax);
              this.tettoMaxList[index] = res;
              this.tettoMaxTableSource.load(this.tettoMaxList);
            }, err => {
              console.log('Si è verificato un errore durante la richiesta.');
            }
          );
        }
      });
    } else {
      this.messageService.showMessage('Errore', MessagePage.MSG_WARN__RECORD_ALREADY_DEL);
      return;
    }*/
  }

}
