import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DocumentViewComponent } from './document-view/document-view.component';
import { TermFrequencyViewComponent } from './term-frequency-view/term-frequency-view.component';
import { DocumentFrequencyViewComponent } from './document-frequency-view/document-frequency-view.component';
import { TagCloudComponent } from './tag-cloud/tag-cloud.component';

import {MatButtonModule} from '@angular/material/button';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCardModule} from '@angular/material/card';
import {MatDividerModule} from '@angular/material/divider';
import {MatSidenavModule} from '@angular/material/sidenav';

@NgModule({
  declarations: [
    AppComponent,
    DocumentViewComponent,
    TermFrequencyViewComponent,
    DocumentFrequencyViewComponent,
    TagCloudComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatGridListModule,
    MatCardModule,
    MatDividerModule,
    MatSidenavModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
