import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TextFile } from '../interfaces/text-file';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  API_URL: String = "https://stoplight.io/mocks/bdeaf/bdea-tagclouds/167859272";

  constructor(private http: HttpClient) { }

  getFiles() {
    const headers = new HttpHeaders().set('Prefer', 'code=200, example=Bücher Example');
    return this.http.get(this.API_URL + '/files', { headers });
  }

  uploadFile(file: TextFile) {
    const data: Object = {
      'title': file.title,
      'content': file.content
    }

    this.http.post(this.API_URL + '/files', data).subscribe(data => {
      console.log(data);
    });
  }

  getDF() {
    const headers = new HttpHeaders().set('Prefer', 'code=200, example=Example Image');
    return this.http.get(this.API_URL + '/df', { headers });
  }

  getTF(file: TextFile) {
    const headers = new HttpHeaders().set('Prefer', 'code=200, example=Example Image');
    return this.http.get(this.API_URL + '/tf/' + file.id, { headers });
  }

}
