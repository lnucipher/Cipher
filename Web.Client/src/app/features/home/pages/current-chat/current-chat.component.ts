import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService } from '../../../../core/auth/services/user.service';
import { User } from '../../../../core/auth/user.model';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SignalRService } from '../../../../core/auth/services/signalr.service';
import { Message } from '../../../../core/models/paginatedContacts.model';

@Component({
  selector: 'app-current-chat',
  templateUrl: './current-chat.component.html',
  styleUrl: './current-chat.component.css',
  standalone: true,
  imports: [CommonModule,FormsModule],
})
export class CurrentChatComponent implements OnInit {
  currentChatUser: User | null = null;
  currentText: string = '';
  messages: Message[] = [];

  constructor(private userService: UserService,private signalRService: SignalRService) {}

  ngOnInit(): void {
    // Start SignalR connection
    this.signalRService.startConnection();

    // Subscribe to the selected chat user
    this.userService.currentOpenedChatUser.subscribe((user) => {
      this.currentChatUser = user;
      this.signalRService.getMessages();  // Fetch initial messages when chat user changes
    });

    // Subscribe to messages stream
    this.signalRService.messages$.subscribe((messages) => {
      this.messages = messages;
    });
  }

  get userId(): string | null {
    return localStorage.getItem('userId');
  }
  
  getAvatarUrl(): string {
    return this.currentChatUser
      ? `https://localhost:5000/identity/${this.currentChatUser.avatarUrl}`
      : '';
  }

  sendMessage(): void {
    const senderId = localStorage.getItem('userId');
    const receiverId = this.currentChatUser ? this.currentChatUser.id : null;
    if (senderId && receiverId && this.currentText.trim()) {
      this.signalRService.sendMessage(senderId, receiverId, this.currentText);
      this.currentText = ''; // Clear input field after sending
    }
  }

  ngOnDestroy(): void {
    // Disconnect SignalR connection
    this.signalRService.stopConnection();
  }

}
