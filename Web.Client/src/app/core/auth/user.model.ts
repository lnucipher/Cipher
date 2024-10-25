import { Status } from '../../core/models/status-state.model';

export interface User {
  id: string;
  username: string;
  token: string;
  name: string;
  bio: string;
  birthDate: string;
  avatarUrl: string;
  status: Status;
  lastSeen: Date;
}
