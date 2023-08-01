import { Component, OnInit } from '@angular/core';
import { MessagesService } from '../services/messages.service';
import { AxiosService } from '../services/axios.service';

@Component({
  selector: 'app-auth-content',
  templateUrl: './auth-content.component.html',
  styleUrls: ['./auth-content.component.css']
})
export class AuthContentComponent implements OnInit {

  messages : string[] = [];

  constructor(private axiosService: AxiosService) { }

    ngOnInit(): void {
    this.axiosService.request(
        "GET",
        "/messages",
        {}).then(
        (response) => {
            this.messages = response.data;
        }).catch(
        (error) => {
            if (error.response.status === 401) {
                this.axiosService.setAuthToken(null);
            } else {
                this.messages = error.response.code;
            }

        }
    );
  }

}