import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-document-frequency-view',
  templateUrl: './document-frequency-view.component.html',
  styleUrls: ['./document-frequency-view.component.scss']
})
export class DocumentFrequencyViewComponent {
  @Input() dfImage: String = '';
  @Output() dfUpdate = new EventEmitter;

  updateDF() {
    this.dfUpdate.emit();
  }
}
