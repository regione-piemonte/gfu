import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvvedimentiAnagComponent } from './provvedimenti-anag.component';

describe('ProvvedimentiComponent', () => {
  let component: ProvvedimentiAnagComponent;
  let fixture: ComponentFixture<ProvvedimentiAnagComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvvedimentiAnagComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvvedimentiAnagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
