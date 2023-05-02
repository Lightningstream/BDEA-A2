import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TagCloud } from 'src/app/interfaces/tag-cloud';
import { TagCloudService } from 'src/app/services/tag-cloud.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

  fileName: String = '';
  uploadedFile: File | undefined = undefined;

  @Output() toSelect = new EventEmitter<String>();

  error: String = '';

  constructor(public dialogRef: MatDialogRef<FileUploadComponent>, 
    private tagCloudService: TagCloudService) {}

  onClose(): void {
    this.dialogRef.close();
  }

  onFileSelected(event: any) {

    this.error = '';
    this.uploadedFile = undefined;

    const file:File = event.target.files[0];

    if (file) {
      if (file.type == "text/plain") {
        this.fileName = file.name;

        let fileReader = new FileReader();
        fileReader.onload = () => {

          if (fileReader.result) {
            this.uploadedFile = file;
            console.log(fileReader.result.toString());
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
    if (this.uploadedFile) {
      this.tagCloudService.uploadFile(this.uploadedFile).subscribe(data => {
        if(data as TagCloud) {
          this.dialogRef.close({ title : (data as TagCloud).name });
        }
      });
    }
  }

}
