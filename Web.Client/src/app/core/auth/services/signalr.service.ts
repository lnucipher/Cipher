import { Injectable } from '@angular/core';
import {
  HubConnection,
  HubConnectionBuilder,
  LogLevel,
} from '@microsoft/signalr';
import { BehaviorSubject, Observable } from 'rxjs';
import { Message } from '../../models/interfaces';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root',
})
export class SignalRService {
  private hubConnection!: HubConnection;
  private messagesSubject = new BehaviorSubject<Message[]>([]);
  messages$ = this.messagesSubject.asObservable();

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
        this.registerReceiveMessageHandler();
      })
      .catch((err) =>
        console.error('Error while starting SignalR connection:', err)
      );
  }

  stopConnection(): void {
    if (this.hubConnection) {
      this.hubConnection
        .stop()
        .then(() => console.log('SignalR connection stopped'))
        .catch((err) =>
          console.error('Error while stopping SignalR connection:', err)
        );
    }
  }

  private registerReceiveMessageHandler(): void {
    this.hubConnection.on('ReceiveMessage', (message: Message) => {
      console.log('Received message:', message);
      const currentMessages = this.messagesSubject.value;
      this.messagesSubject.next([...currentMessages, message]);
    });
  }
}
