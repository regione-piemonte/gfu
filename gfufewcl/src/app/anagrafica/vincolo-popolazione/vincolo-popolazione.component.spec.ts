import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VincoloPopolazioneComponent } from './vincolo-popolazione.component';

describe('VincoloPopolazioneComponent', () => {
  let component: VincoloPopolazioneComponent;
  let fixture: ComponentFixture<VincoloPopolazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VincoloPopolazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VincoloPopolazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
