import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService } from '../../../../core/auth/services/user.service';
import { User } from '../../../../core/auth/user.model';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SignalRService } from '../../../../core/auth/services/signalr.service';
import { Message } from '../../../../core/models/interfaces';

@Component({
  selector: 'app-current-chat',
  templateUrl: './current-chat.component.html',
  styleUrl: './current-chat.component.css',
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class CurrentChatComponent implements OnInit {
  currentChatUser: User | null = null;
  currentText: string = '';
  messages: Message[] = [];
  userId: string = '';
  pageSize: number = 25;
  page: number = 1;
  currentChatUserId: string | null = null;

  constructor(
    private userService: UserService,
    private signalRService: SignalRService
  ) {}

  ngOnInit(): void {
    this.signalRService.startConnection();

    this.userService.currentOpenedChatUser.subscribe((user) => {
      this.currentChatUser = user;

      const storedUserId = localStorage.getItem('userId');

      if (storedUserId && this.currentChatUser?.id) {
        this.userId = storedUserId;
        this.currentChatUserId = this.currentChatUser.id;
        console.log('currentChatUser.id is defined');
        this.loadMessages();
        this.signalRService.messages$.subscribe((messages) => {
          this.messages = messages;
        });
      } else if (!storedUserId) {
        console.error('No userId found in localStorage');
      } else {
        console.log('currentChatUser.id is not yet defined');
      }
    });
  }

  loadMessages(): void {
    if (this.userId && this.currentChatUserId) {
      this.userService
        .getMessages(
          this.userId,
          this.currentChatUserId,
          this.page,
          this.pageSize
        )
        .subscribe((response) => {
          const currentMessages = this.signalRService.messagesSubject.value;
          const combinedMessages = [...response.items.reverse()];
          this.signalRService.messagesSubject.next(combinedMessages);
        });
    }
  }

  getAvatarUrl(): string {
    return this.currentChatUser
      ? `http://212.23.203.37:5000/identity${this.currentChatUser.avatarUrl}`
      : '';
  }

  sendMessageHandler(): void {
    const receiverId = this.currentChatUserId;
    if (receiverId && this.currentText) {
      this.userService
        .sendMessage(receiverId, this.currentText)
        .subscribe(() => console.log('Message sent successfully'));
      console.log('sent: ' + this.currentText);
      this.currentText = '';
    }
    this.loadMessages();
  }

  onKeydown(event: Event): void {
    const keyboardEvent = event as KeyboardEvent;
    keyboardEvent.preventDefault();
    this.sendMessageHandler();
  }

  ngOnDestroy(): void {
    this.signalRService.stopConnection();
  }
}
