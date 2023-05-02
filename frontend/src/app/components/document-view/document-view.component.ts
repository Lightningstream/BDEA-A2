import { Component, Output, ViewChild, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { TagCloud } from '../../interfaces/tag-cloud';
import { TagCloudService } from 'src/app/services/tag-cloud.service';
import { MatDialog } from '@angular/material/dialog';
import { FileUploadComponent } from '../file-upload/file-upload.component';

@Component({
  selector: 'app-document-view',
  templateUrl: './document-view.component.html',
  styleUrls: ['./document-view.component.scss']
})
export class DocumentViewComponent {

  currentlySelected: TagCloud | undefined = undefined;
  @Output() selectedTagCloud = new EventEmitter<TagCloud>();

  @ViewChild('uploadDialog') uploadDialog: any;

  tagClouds: TagCloud[] = [];

  constructor(private tagCloudservice: TagCloudService, private dialog: MatDialog, public cdr: ChangeDetectorRef) {
    this.updateTagClouds();
  }

  updateTagClouds() {
    this.tagCloudservice.getTagClouds().subscribe((data: Object) => {
      this.tagClouds = data as TagCloud[];
    });
  }

  updateAndSelectTagCloud(title: string) {
    this.tagCloudservice.getTagClouds().subscribe((data: Object) => {
      this.tagClouds = data as TagCloud[];

      this.tagClouds.forEach(tagCloud => {
        if (tagCloud.name == title) {
          this.selectTagCloud(tagCloud);
        }
      });
    });
  }

  selectTagCloud(tagCloud: TagCloud) {
    this.currentlySelected = tagCloud;
    this.selectedTagCloud.emit(tagCloud);
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(FileUploadComponent, {data: {caller: this}});

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result.title) {
        this.updateAndSelectTagCloud(result.title);
      }
    });
  }

  startBatch() {
    this.tagCloudservice.startBatchJob().subscribe(data => {
      console.log(data);
    });
  }
}
