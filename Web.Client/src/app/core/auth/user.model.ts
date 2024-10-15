import { Status } from '../../core/models/status-state.model';

export class User {
    constructor(
      public id: string,
      public username: string,
      public passwordHash: string,
      public name: string,
      public bio: string,
      public birthDate: Date,  // Using Date instead of LocalDate
      public avatarUrl: string,
      public status: Status,
      public lastSeen: Date // Using Date for Timestamp
    ) {}
  }