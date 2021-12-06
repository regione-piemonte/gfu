import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TettoMaxComponent } from './tetto-max.component';

describe('TettoMaxComponent', () => {
  let component: TettoMaxComponent;
  let fixture: ComponentFixture<TettoMaxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TettoMaxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TettoMaxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
