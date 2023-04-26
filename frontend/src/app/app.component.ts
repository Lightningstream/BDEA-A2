import { Component } from '@angular/core';
import {BreakpointObserver} from '@angular/cdk/layout';

export interface Tile {
  cols: number;
  rows: number;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  portrait: boolean = false;

  constructor(breakpointObserver: BreakpointObserver) {
    const layoutChanges = breakpointObserver.observe([
      '(orientation: portrait)',
      '(orientation: landscape)',
    ]);
    
    layoutChanges.subscribe(result => {
      this.portrait = result['breakpoints']['(orientation: portrait)'];
    });
  }

}
