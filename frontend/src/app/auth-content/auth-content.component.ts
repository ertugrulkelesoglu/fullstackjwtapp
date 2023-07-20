import { Component, OnInit } from '@angular/core';
import { MessagesService } from '../services/messages.service';

@Component({
  selector: 'app-auth-content',
  templateUrl: './auth-content.component.html',
  styleUrls: ['./auth-content.component.css']
})
export class AuthContentComponent implements OnInit {

  messages : string[] = [];

  constructor(private messagesService:MessagesService) { }

  ngOnInit(): void {

    this.messagesService.getMessages().subscribe((response: string[]) => this.messages = response)
  }

}
