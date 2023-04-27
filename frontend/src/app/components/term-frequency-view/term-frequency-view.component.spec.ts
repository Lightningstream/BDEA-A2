import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermFrequencyViewComponent } from './term-frequency-view.component';

describe('TermFrequencyViewComponent', () => {
  let component: TermFrequencyViewComponent;
  let fixture: ComponentFixture<TermFrequencyViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TermFrequencyViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TermFrequencyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
