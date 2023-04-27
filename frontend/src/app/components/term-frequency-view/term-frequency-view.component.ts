import { Component, Input } from '@angular/core';
import { TextFile } from 'src/app/interfaces/text-file';

@Component({
  selector: 'app-term-frequency-view',
  templateUrl: './term-frequency-view.component.html',
  styleUrls: ['./term-frequency-view.component.scss']
})
export class TermFrequencyViewComponent {
  @Input() tfImage: String = '';
  @Input() selectedFile: TextFile | undefined = undefined;

  getTitle() {
    if(this.selectedFile != undefined) {
      return this.selectedFile.title;
    }
    return '';
  }
}
