import { Component } from '@angular/core';
import * as json5 from 'json5';
import { LevelObject } from './LevelObject';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  levelObjects: Array<LevelObject>;

  public constructor() {
    this.levelObjects = new Array<LevelObject>();
    this.levelObjects.push({
      class: 'Enemy',
      x: 800,
      y: 400,
      time: 8.9,
      type: 'Drone'
    });
  }

  public onFileChange(fileEvent: any): void {
    const file = fileEvent.target.files[0];
    const fileReader = new FileReader();
    fileReader.onload = () => {
      this.levelObjects = JSON.parse(fileReader.result as string);
    };
    fileReader.readAsText(file);
  }

  public addObj(): void {
    this.levelObjects.push({
      class: 'Enemy',
      time: 0.0,
      x: 0.0,
      y: 0.0,
      type: 'Drone'
    });
    this.sortObjectsByTime();
  }

  public export(): void {
    const newFile = JSON.stringify(this.levelObjects);
    saveAs(new Blob([newFile]), 'level.json');
  }

  public delete(index: number): void {
    this.levelObjects.splice(index, 1);
  }

  public sortObjectsByTime(): void {
    this.levelObjects.sort((a, b) => {
      const aNum = a.time as number;
      const bNum = b.time as number;
      return aNum - bNum;
    });
  }
}
