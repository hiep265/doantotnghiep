import { Component, OnInit, AfterViewInit, OnDestroy, ElementRef, ViewChild, Renderer2 } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true, // Đánh dấu là standalone
  imports: [
    CommonModule, // Cho *ngIf, *ngFor, async pipe...
    ReactiveFormsModule, // Cho formGroup, formControlName...
    RouterModule // Cho routerLink (nếu có trong template)
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, AfterViewInit, OnDestroy {
  loginForm: FormGroup;
  isLoading = false;
  loginError: string | null = null;
  loginSubscription: Subscription | null = null;
  mouseMoveListener: (() => void) | null = null;
  focusListener: (() => void) | null = null; // Lưu hàm hủy listener focus
  blurListener: (() => void) | null = null; // Lưu hàm hủy listener blur


  @ViewChild('passwordInput') passwordInputRef!: ElementRef<HTMLInputElement>;
  @ViewChild('eyesContainer') eyesContainerRef!: ElementRef<HTMLDivElement>;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private renderer: Renderer2
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
      rememberMe: [false]
    });
  }

  ngOnInit(): void { }

  ngAfterViewInit(): void {
    this.initEyeEffect();
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
    // Hủy các listener đã thêm bằng renderer
    if (this.mouseMoveListener) this.mouseMoveListener();
    if (this.focusListener) this.focusListener();
    if (this.blurListener) this.blurListener();
  }

  onSubmit(): void {
    this.loginError = null;
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    const credentials = {
      phoneNumber: this.loginForm.value.username,
      password: this.loginForm.value.password
    };
    console.log('Sending login request with credentials:', credentials);
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
    this.loginSubscription = this.authService.login(credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.router.navigate(['/home']);
      },
      error: (error) => {
        this.isLoading = false;
        this.loginError = 'Tên đăng nhập hoặc mật khẩu không đúng.';
        console.error('Login failed in component:', error);
      }
    });
  }

  // --- Logic Hiệu ứng Mắt ---
  initEyeEffect(): void {
    const eyes = this.eyesContainerRef?.nativeElement.querySelectorAll('.eye');
    const passwordInputEl = this.passwordInputRef?.nativeElement;

    if (!eyes || eyes.length === 0 || !passwordInputEl) {
      console.warn('Eye elements or password input not found for effect.');
      return;
    }

    const eyeToBlinkIndex = 1;

    // Nhắm/Mở mắt khi focus/blur password input
    this.focusListener = this.renderer.listen(passwordInputEl, 'focus', () => {
      if (eyes[eyeToBlinkIndex]) {
        this.renderer.addClass(eyes[eyeToBlinkIndex], 'eye-closed');
      }
    });

    this.blurListener = this.renderer.listen(passwordInputEl, 'blur', () => {
      if (eyes[eyeToBlinkIndex]) {
        this.renderer.removeClass(eyes[eyeToBlinkIndex], 'eye-closed');
      }
    });

    // Di chuyển con ngươi theo chuột
    this.mouseMoveListener = this.renderer.listen('document', 'mousemove', (event: MouseEvent) => {
      const pupils = this.eyesContainerRef.nativeElement.querySelectorAll('.pupil');
      const mouseX = event.clientX;
      const mouseY = event.clientY;

      pupils.forEach((pupil) => {
        const pupilElement = pupil as HTMLElement;
        const eye = pupilElement.parentElement as HTMLElement;
        if (!eye) return;
        if (eye.classList.contains('eye-closed')) return;

        const eyeRect = eye.getBoundingClientRect();
        const eyeCenterX = eyeRect.left + eyeRect.width / 2;
        const eyeCenterY = eyeRect.top + eyeRect.height / 2;
        const deltaX = mouseX - eyeCenterX;
        const deltaY = mouseY - eyeCenterY;
        const angleRad = Math.atan2(deltaY, deltaX);
        const lazyOffsetRad = 0.9;
        const finalAngleRad = angleRad + lazyOffsetRad;
        const maxPupilOffset = (eyeRect.width / 2) - (pupilElement.offsetWidth / 2) - 2;
        const distToMouse = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        const pupilOffsetX = Math.cos(finalAngleRad) * Math.min(maxPupilOffset, distToMouse * 0.15);
        const pupilOffsetY = Math.sin(finalAngleRad) * Math.min(maxPupilOffset, distToMouse * 0.15);

        this.renderer.setStyle(pupilElement, 'transform', `translate(calc(-50% + ${pupilOffsetX}px), calc(-50% + ${pupilOffsetY}px))`);
      });
    });
  }

  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }
}
