import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentFrequencyViewComponent } from './document-frequency-view.component';

describe('DocumentFrequencyViewComponent', () => {
  let component: DocumentFrequencyViewComponent;
  let fixture: ComponentFixture<DocumentFrequencyViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentFrequencyViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocumentFrequencyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
