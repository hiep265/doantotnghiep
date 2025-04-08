import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common'; // Import CommonModule
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true, // Đánh dấu là standalone
  imports: [CommonModule], // Cho *ngIf, async pipe
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  currentUser$: Observable<string | null>;

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    console.log('HomeComponent loaded');
  }

  logout(): void {
    this.authService.logout();
  }
}
