import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TextFile } from 'src/app/interfaces/text-file';
import { FileService } from 'src/app/services/file.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

  fileName: String = '';
  uploadedTextFile: TextFile | undefined = undefined;

  error: String = '';

  constructor(public dialogRef: MatDialogRef<FileUploadComponent>, private fileService: FileService) {}

  onClose(): void {
    this.dialogRef.close();
  }

  onFileSelected(event: any) {

    this.error = '';
    this.uploadedTextFile = undefined;

    const file:File = event.target.files[0];

    if (file) {
      if (file.type == "text/plain") {
        this.fileName = file.name;

        let fileReader = new FileReader();
        fileReader.onload = () => {

          if (fileReader.result) {
            this.uploadedTextFile = {
              id: null,
              title: file.name,
              content: fileReader.result.toString()
            }
            console.log(this.uploadedTextFile);
          }
          else {
            this.fileName = '';
            this.error = "The selected file was empty. Please choose another file!";
          }

        }
        fileReader.readAsText(file);
      }
      else {
        this.error = "You can only upload files with the data type 'plain/text'!";
      }
    }
    else {
      console.log("No file was chosen!");
    }
  }

  uploadDocument() {
    if (this.uploadedTextFile) {
      this.fileService.uploadFile(this.uploadedTextFile);
      this.onClose();
    }
  }

}
