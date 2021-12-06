import { Injectable, Inject } from '@angular/core';
import { MessagePage } from './message-page';

@Injectable()
export class MessageAdapter {

  constructor() {
  }
  message(code: string): string {
    switch (code) {
      case '1': {
        return MessagePage.MSG_WARN_TO_LOGICDELETE;
      }
    }
    return 'Errore imprevisto di sistema, contattare l\'amministratore';
  }
}
