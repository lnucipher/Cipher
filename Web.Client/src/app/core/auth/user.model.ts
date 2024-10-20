import { Status } from '../../core/models/status-state.model';

//user model expected
export interface User {
  id: string;
  username: string;
  passwordHash: string;
  token: string;
  name: string;
  bio: string;
  avatarUrl: string;
  status: Status;
  lastSeen: Date;
}
