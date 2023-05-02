import { Component, Input } from '@angular/core';
import { TagCloud } from 'src/app/interfaces/tag-cloud';

@Component({
  selector: 'app-tf-idf-view',
  templateUrl: './tf-idf-view.component.html',
  styleUrls: ['./tf-idf-view.component.scss']
})
export class TfIdfViewComponent {
  @Input() tfIdf_Image: String = '';
  @Input() selectedTagCloud: TagCloud | undefined = undefined;

  getTitle() {
    if(this.selectedTagCloud != undefined) {
      return this.selectedTagCloud.name;
    }
    return '';
  }
}
