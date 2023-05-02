import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-global-tf-idf-view',
  templateUrl: './global-tf-idf-view.component.html',
  styleUrls: ['./global-tf-idf-view.component.scss']
})
export class GlobalTfIdfViewComponent {
  @Input() globalTfIdf_Image: String = '';
}
