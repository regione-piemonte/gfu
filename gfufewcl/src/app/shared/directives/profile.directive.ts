/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Directive, ElementRef, Input, OnChanges, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[appProfile]'
})
export class ProfileDirective implements OnChanges {

  @Input() currentProfile: string;
  @Input() allowedProfiles: Array<string> = [];

  constructor(private el: ElementRef) { }

  /*
     * On changes hook
     * @param changes The changes
     */
    ngOnChanges(changes: SimpleChanges) {
      if (changes.currentProfile || changes.allowedProfiles) {
          if (this.currentProfile !== undefined &&
              this.allowedProfiles !== undefined) {
                  const array: Array<string> = this.allowedProfiles as Array<string>;
                  const current = this.currentProfile;
                  if (array.find(x => x === current) !== undefined) {
                      this.el.nativeElement.style.display = 'block';
                  } else {
                      this.el.nativeElement.style.display = 'none';
                  }
          }
      }
  }

}
