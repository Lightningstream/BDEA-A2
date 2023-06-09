import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-tag-cloud',
  templateUrl: './tag-cloud.component.html',
  styleUrls: ['./tag-cloud.component.scss']
})
export class TagCloudComponent {
  @Input() title: String = '';
  @Input() heading: String = '';
  @Input() image: String = '';
  @Input() notice: String = '';
}
