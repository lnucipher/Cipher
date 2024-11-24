import { Injectable } from '@angular/core';
import {
  HubConnection,
  HubConnectionBuilder,
  HttpTransportType,
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
  messagesSubject = new BehaviorSubject<Message[]>([]);
  messages$ = this.messagesSubject.asObservable();

  constructor(private jwtService: JwtService) {}

  startConnection(): void {
    this.hubConnection = new HubConnectionBuilder()
      .withUrl('http://212.23.203.37:5001/chat/api/chat-hub', {
        accessTokenFactory: () => {
          const token = this.jwtService.getToken();
          if (!token) {
            console.log('JWT token not found for socket!');
          }
          console.log('jwt token was found for socket!');
          return token;
        },
        skipNegotiation: true, // Required when directly using WebSockets
      transport: HttpTransportType.WebSockets, // Direct WebSocket transport
      })
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
