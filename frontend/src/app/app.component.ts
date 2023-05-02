import { Component, OnInit, ViewChild } from '@angular/core';
import {BreakpointObserver} from '@angular/cdk/layout';
import { TagCloud } from './interfaces/tag-cloud';
import { TagCloudService } from './services/tag-cloud.service';
import { DocumentViewComponent } from './components/document-view/document-view.component';

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

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  selectedTagCloud: TagCloud | undefined = undefined;
  tfIdf_Image: String = '';
  globalTfIdf_Image: String = '';

  @ViewChild(DocumentViewComponent) documentView!: DocumentViewComponent;;

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

  constructor(breakpointObserver: BreakpointObserver, private tagCloudservice: TagCloudService) {
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
    this.updateGlobalTfIdf();
  }

  changeSelectedTagCloud(tagCloud: TagCloud) {
    this.selectedTagCloud = tagCloud;
    this.tagCloudservice.getTagCloud(tagCloud.name).subscribe(data => {
      this.tfIdf_Image = 'data:image/png;base64,' + (data as TagCloud).base64Image;
    });
  }

  updateGlobalTfIdf() {
    console.log("Global TF-IDF is updated");
    this.tagCloudservice.getGlobalTagCloud().subscribe(data => {
      this.globalTfIdf_Image = 'data:image/png;base64,' + (data as TagCloud).base64Image;
    });
  }

}
