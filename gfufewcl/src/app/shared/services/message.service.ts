/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from '../components/modal/modal.component';
import { MessagePage } from '../utils/message-page';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private modalService: NgbModal) { }

  showMessage(title: string, content: string) {
    const modalRef = this.modalService.open(ModalComponent, {
      centered: true,
    });
    modalRef.componentInstance.data = {
      mode: 'info',
      title,
      content,
    };
  }

  showConfirmation(title: string, content: string) {
    const modalRef = this.modalService.open(ModalComponent, {
      centered: true,
    });
    modalRef.componentInstance.data = {
      mode: 'confirm',
      title,
      content,
    };
    return modalRef;
  }

  showGenericError() {
    const modalRef = this.modalService.open(ModalComponent, {
      centered: true,
    });
    modalRef.componentInstance.data = {
      mode: 'info',
      title: 'Errore',
      content: MessagePage.MSG_SYSTEM_ERR,
    };
  }
}
