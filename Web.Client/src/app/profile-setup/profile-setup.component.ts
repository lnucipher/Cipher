import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
selector: 'app-profile-setup',
standalone: true,
templateUrl:'./profile-setup.component.html',
styleUrl: './profile-setup.component.css',
imports: [RouterLink]
})

export class ProfileSetupComponent {}
