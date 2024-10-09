import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
selector: 'app-sign-in',
standalone: true,
templateUrl:'./sign-in.component.html',
styleUrl: './sign-in.component.css',
imports: [RouterLink]
})

export class SignInComponent {}
