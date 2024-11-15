import { User } from '../auth/user.model';
export interface PaginatedContacts {
  items: User[];
  hasNextPage: boolean;
  hasPreviousPage: boolean;
  pageNumber: number;
}

export interface SearchedContacts{
  users: User[];
  isContact: boolean;
}

export interface Message {
  id: string;
  senderId: string;
  receiverId: string;
  text: string;
  createdAt: Date;
}

export interface PaginatedMessages {
  pageNumber: number;
  pageSize: number;
  totalCount: number;
  totalPages: number;
  hasPreviousPage: boolean;
  hasNextPage: boolean;
  items: Message[];
}