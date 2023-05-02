import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TagCloudService {

  API_URL: String = "http://127.0.0.1:8080";

  constructor(private http: HttpClient) { }

  getTagClouds() {
    return this.http.get(this.API_URL + '/TagCloud');
  }

  uploadFile(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(this.API_URL + '/TagCloud', formData);
  }

  getTagCloud(name: String) {
    return this.http.get(this.API_URL + '/TagCloud/'+name);
  }

  getGlobalTagCloud() {
    return this.getTagCloud("global");
  }

  startBatchJob() {
    return this.http.get(this.API_URL + '/Batch', { responseType: 'text' });
  }

}
