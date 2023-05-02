import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DocumentViewComponent } from './components/document-view/document-view.component';
import { TfIdfViewComponent } from './components/tf-idf-view/tf-idf-view.component';
import { GlobalTfIdfViewComponent } from './components/global-tf-idf-view/global-tf-idf-view.component';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { TagCloudComponent } from './components/tag-cloud/tag-cloud.component';

import {MatButtonModule} from '@angular/material/button';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCardModule} from '@angular/material/card';
import {MatDividerModule} from '@angular/material/divider';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatDialogModule} from '@angular/material/dialog';
import {MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';

@NgModule({
  declarations: [
    AppComponent,
    DocumentViewComponent,
    TfIdfViewComponent,
    GlobalTfIdfViewComponent,
    TagCloudComponent,
    FileUploadComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatGridListModule,
    MatCardModule,
    MatDividerModule,
    MatSidenavModule,
    HttpClientModule,
    MatDialogModule,
    MatIconModule,
    MatFormFieldModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
