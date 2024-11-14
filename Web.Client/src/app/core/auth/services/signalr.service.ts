import { Injectable } from '@angular/core';
import { HubConnection, HubConnectionBuilder, LogLevel } from '@microsoft/signalr';
import { BehaviorSubject, Observable } from 'rxjs';
import { Message } from '../../models/paginatedContacts.model';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class SignalRService {
    private hubConnection!: HubConnection;
    private messagesSubject = new BehaviorSubject<Message[]>([]);
    public messages$ = this.messagesSubject.asObservable();
  
    constructor(private jwtService: JwtService) {}
  
    startConnection(): void {
      const token = this.jwtService.getToken();
      if (!token) {
        console.error('JWT token not found!');
        return;
      }
  
      this.hubConnection = new HubConnectionBuilder()
        .withUrl(`wss://localhost:5000/chat/api/chat-hub?access_token=${token}`)
        .withAutomaticReconnect()
        .build();
  
      this.hubConnection
        .start()
        .then(() => {
          console.log('SignalR connection started');
          this.getMessages();
        })
        .catch((err) => console.error('Error establishing connection:', err));
  
      this.hubConnection.on('ReceiveMessage', (message: Message) => {
        this.addMessage(message);
      });
    }
  
    sendMessage(senderId: string, receiverId: string, text: string): void {
      if (this.hubConnection && this.hubConnection.state === 'Connected') {
        const message = { senderId, receiverId, text };
        this.hubConnection
          .invoke('SendMessage', message)
          .catch((err) => console.error('Error sending message:', err));
      }
    }

    getMessages(): void {
      this.hubConnection
        .invoke('GetMessages')
        .then((messages: Message[]) => {
          this.messagesSubject.next(messages);
        })
        .catch((err) => console.error('Error fetching messages:', err));
    }
  

    private addMessage(message: Message): void {
      const currentMessages = this.messagesSubject.value;
      this.messagesSubject.next([...currentMessages, message]);
    }
  

    stopConnection(): void {
      if (this.hubConnection) {
        this.hubConnection
          .stop()
          .then(() => console.log('SignalR connection stopped'))
          .catch((err) => console.error('Error stopping connection:', err));
      }
    }
  }