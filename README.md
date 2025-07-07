# WorkBuddy

A lightweight Android app to help you stay productive with task management and Pomodoro-style timers.  
Manage your to-do list, track Pomodoro sessions per task, and view daily/weekly productivity stats — *all in one place*.

---

## 📖 App Description & Purpose

WorkBuddy helps you:
- **Add, complete, and delete tasks** with due times.
- **Track Pomodoro sessions** per task.
- **View daily and weekly stats** of completed Pomodoros.
- **Stay motivated** with random productivity quotes.

Perfect for students, developers, or anyone who wants to structure their workday into focused intervals and keep track of progress.

---
## ⚙️ Installation & Setup


1.  ***For Android Studio:*** **VCS**  
  File → New → Project From Version Control  
  URL: https://github.com/bene232023F/WorkBuddy.git  
  Clone
    - Ensure you have Android SDK 24+ installed.    
    
    Click Sync Project with Gradle Files, then Run on an emulator or device.
3.  ***For terminal installation:*** **Clone the repo**  
     ```bash
     git clone https://github.com/your-username/workbuddy.git
     cd workbuddy

---
## 🚀 Features  

**Task Management**
- Add new tasks with a title and due time
- Mark tasks complete via checkbox
- Swipe left/right to delete tasks
  
<img 
  src="https://github.com/user-attachments/assets/8dbda5ef-1bdc-499c-ac3c-57136728ebfc" 
  alt="empty tasks" 
  width="200" 
/>
<img 
  src="https://github.com/user-attachments/assets/e21adca7-e5ad-4882-af77-b34aec8f1598" 
  alt="filled tasks" 
  width="200" 
/>

**Pomodoro Timer**
- Select session length (5–60 min) via dropdown
- Start, pause, and reset timer per task
- Automatically increments task’s Pomodoro count on completion
     
<img 
  src="https://github.com/user-attachments/assets/06ef08ed-668a-4139-97b1-d3ae28302b73" 
  alt="home" 
  width="200" 
/>

**Statistics & Motivation**
- View Today’s and This Week’s Pomodoro totals
- See a random motivational quote each time
    
<img 
  src="https://github.com/user-attachments/assets/17c4c8c6-5ac6-43f6-869a-dedaf659e685" 
  alt="empty tasks" 
  width="200" 
/>

---
## 🛠️ Technology Stack

- **Language:** Kotlin
- **Architecture:** MVVM (ViewModels, LiveData)
- **UI:**
  - AndroidX Fragments, ConstraintLayout, RecyclerView
  - Material Components (FloatingActionButton, Dropdown, Slider)
- **Data Persistence:** Room (SQLite) with
  - Task entity + DAO
  - PomodoroSession entity + DAO
- **Navigation:** Jetpack Navigation (Bottom Navigation & Dialogs)
- **Concurrency:** Kotlin Coroutines, Flow
- **Dependency Injection:** ViewModelFactory
- **Build:** Gradle Kotlin DSL, KSP for Room compiler
- **Version Control:** Git (GitHub)

---  
## 🎥 Demo  

https://github.com/user-attachments/assets/3c3f3898-e2d9-4f50-86b8-a209d9fefcab

---  
## 🔭 Future Development Roadmap
- **Custom themes:** Light/dark mode toggle
- **Reflections:** Write your thoughts about a task
- **Notifications:** Remind when Pomodoro completes
- **Task categories & priorities**
- **Persistence of user settings**
- **Statistics chart** in the stats screen
- **Cloud sync** & account support

---
## 🤝 Contact  
**Project Lead:** Ben Edelstein  
**Email:** bene23@vt.edu
