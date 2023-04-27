import { Component, OnInit } from '@angular/core';
import {BreakpointObserver} from '@angular/cdk/layout';
import { TextFile } from './interfaces/text-file';
import { FileService } from './services/file.service';

export interface Format {
  gridList: {
    cols: number,
    rowheight: string
  },
  gridTiles: [
    [number, number],
    [number, number],
    [number, number]
  ],
  borderClass: string
}

export interface ImageResponse {
  image: string
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  selectedFile: TextFile | undefined = undefined;
  tfImage: String = '';
  dfImage: String = '';

  landscape: Format = {
    gridList: {
      cols: 5,
      rowheight: '100%'
    },
    gridTiles: [
      [1, 1],
      [2, 1],
      [2, 1]
    ],
    borderClass: 'border-right'
  };
  portrait: Format = {
    gridList: {
      cols: 1,
      rowheight: '33%'
    },
    gridTiles: [
      [1, 1],
      [1, 1],
      [1, 1]
    ],
    borderClass: 'border-bottom'
  };

  format: Format = this.landscape;

  constructor(breakpointObserver: BreakpointObserver, private fileService: FileService) {
    const layoutChanges = breakpointObserver.observe([
      '(orientation: portrait)',
      '(orientation: landscape)',
    ]);

    layoutChanges.subscribe(result => {
      const isPortrait = result['breakpoints']['(orientation: portrait)'];
      if (isPortrait) {
        this.format = this.portrait;
      }
      else {
        this.format = this.landscape;
      }
    });
  }

  ngOnInit(): void {
    this.updateDF();
  }

  changeSelectedFile(file: TextFile) {
    this.selectedFile = file;
    this.fileService.getTF(file).subscribe(data => {
      console.log(data);
      this.tfImage = (data as ImageResponse).image;
    });
  }

  updateDF() {
    console.log("Document Frequency is updated");
    this.fileService.getDF().subscribe(data => {
      console.log(data);
      this.dfImage = (data as ImageResponse).image;
    });
  }

}
