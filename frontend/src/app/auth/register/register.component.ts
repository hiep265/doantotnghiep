import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms'; // Import ReactiveFormsModule và AbstractControl
import { Router, RouterModule } from '@angular/router'; // Import RouterModule
import { CommonModule } from '@angular/common'; // Import CommonModule
import { Subscription } from 'rxjs';
import { AuthService } from '../auth.service';

// Custom validator để kiểm tra mật khẩu khớp nhau (giữ nguyên)
function passwordsMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    // Nếu confirmPassword chưa được chạm vào hoặc chưa nhập thì không báo lỗi
    if (!password || !confirmPassword || !confirmPassword.dirty) {
        return null;
    }

    if (password.value !== confirmPassword.value) {
        return { passwordsNotMatching: true };
    }
    return null;
}

@Component({
  selector: 'app-register',
  standalone: true, // Đánh dấu là standalone
  imports: [
    CommonModule, // Cho *ngIf
    ReactiveFormsModule, // Cho formGroup, formControlName...
    RouterModule // Cho routerLink
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {
  registerForm: FormGroup;
  isLoading = false;
  registerError: string | null = null;
  registerSuccess = false;
  registerSubscription: Subscription | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: passwordsMatchValidator });
  }

  ngOnInit(): void { }

  ngOnDestroy(): void {
    if (this.registerSubscription) {
      this.registerSubscription.unsubscribe();
    }
  }

  onSubmit(): void {
    this.registerError = null;
    this.registerSuccess = false;

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const userData = {
      username: this.registerForm.value.username,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password
    };

    if (this.registerSubscription) {
      this.registerSubscription.unsubscribe();
    }

    this.registerSubscription = this.authService.register(userData).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.registerSuccess = true;
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (error) => {
        this.isLoading = false;
        this.registerError = 'Đăng ký thất bại. Tên đăng nhập hoặc email có thể đã tồn tại.';
        console.error('Registration failed in component:', error);
      }
    });
  }

  get username() { return this.registerForm.get('username'); }
  get email() { return this.registerForm.get('email'); }
  get password() { return this.registerForm.get('password'); }
  get confirmPassword() { return this.registerForm.get('confirmPassword'); }
}
