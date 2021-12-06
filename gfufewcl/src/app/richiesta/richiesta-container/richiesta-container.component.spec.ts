import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiestaContainerComponent } from './richiesta-container.component';

describe('RichiestaContainerComponent', () => {
  let component: RichiestaContainerComponent;
  let fixture: ComponentFixture<RichiestaContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiestaContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiestaContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
