import { Component, Output, ViewChild, EventEmitter } from '@angular/core';
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

  currentlySelected: TextFile | undefined = undefined;
  @Output() selectedFile = new EventEmitter<TextFile>();

  @ViewChild('uploadDialog') uploadDialog: any;

  files: TextFile[] = [];

  constructor(private fileService: FileService, private dialog: MatDialog) {
    this.updateFiles();
  }

  updateFiles() {
    this.fileService.getFiles().subscribe((data: Object) => {
      this.files = data as TextFile[];
    });
  }

  selectFile(file: TextFile) {
    this.currentlySelected = file;
    this.selectedFile.emit(file);
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(FileUploadComponent);

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
