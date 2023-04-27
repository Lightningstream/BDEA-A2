import { Component, ViewChild } from '@angular/core';
import { TextFile } from '../../interfaces/text-file';
import { FileService } from 'src/app/services/file.service';
import { MatDialog } from '@angular/material/dialog';
import { FileUploadComponent } from '../file-upload/file-upload.component';

@Component({
  selector: 'app-document-view',
  templateUrl: './document-view.component.html',
  styleUrls: ['./document-view.component.scss']
})
export class DocumentViewComponent {

  @ViewChild('uploadDialog') uploadDialog: any;

  files: TextFile[] = [];
  titles: String[] = [];

  constructor(private fileService: FileService, private dialog: MatDialog) {
    this.updateFiles();
  }

  updateFiles() {
    this.fileService.getFiles().subscribe((data: Object) => {
      this.files = data as TextFile[];
      this.updateTitles();
    });
  }

  updateTitles() {
    this.files.forEach(file => {
      this.titles.push(file.title);
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(FileUploadComponent);

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
