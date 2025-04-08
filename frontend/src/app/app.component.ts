import { Component } from '@angular/core';
import { RouterModule } from '@angular/router'; // Import RouterModule

@Component({
  selector: 'app-root',
  standalone: true, // Đánh dấu là standalone
  imports: [RouterModule], // Import RouterModule để sử dụng router-outlet
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'your-app-name'; // Thay đổi tên ứng dụng nếu cần
}
