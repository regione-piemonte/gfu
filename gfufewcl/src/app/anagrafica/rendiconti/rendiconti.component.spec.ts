import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RendicontiComponent } from './rendiconti.component';

describe('RendicontiComponent', () => {
  let component: RendicontiComponent;
  let fixture: ComponentFixture<RendicontiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RendicontiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RendicontiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
