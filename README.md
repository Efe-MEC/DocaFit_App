Docafit App - Fitness Tracking Application
📱 Overview
Docafit is a comprehensive fitness tracking application that helps users monitor their workouts, body measurements, and nutrition. The app provides exercise suggestions, body fat calculations, workout progress visualization, and meal recommendations.

✨ Key Features
Activity Tracking: Log workouts with exercise type, weight used, and reps

Body Fat Calculator: Calculate body fat percentage using US Navy method

Exercise Suggestions: Get random exercise recommendations by muscle group

Workout Progress: Visualize your strength progress with interactive charts

Meal Advice: Receive randomized healthy meal suggestions

User Profile: Manage account settings, theme, and language preferences

🛠️ Technical Implementation
Architecture
MVVM (Model-View-ViewModel) pattern

Room Database for local data persistence

Firebase Authentication for user management

Firebase Realtime Database for cloud storage

Core Components
Welcome Page – This screen greets the user and checks if the user is already logged in via Firebase Authentication. If so, the user is redirected automatically, avoiding the need to log in again.
Login Page – Users enter their email and password. They can also navigate to the Sign Up page or initiate password recovery.
Sign Up Page – New users can register here. User data is saved to Firebase.
Home Page – Displays a graph showing the user's exercise records and progress.
Exercise Suggestion Page – Users select a body area, and the app suggests relevant exercises. They can navigate to the timer from this page.
Timer Page – A timer to track exercise duration.
Activity Tracking Page – Gathers data from the user to generate the progress graph (seen on the Home page). It also allows users to calculate their body fat rate.
Meal Suggestions Page – Provides meal suggestions based on user goals.
Profile Page – Displays user information (profile picture, name, gender). Users can change the language, theme, log out, delete their account, or navigate to the Edit Profile page.
Edit Profile Page – Allows users to update their profile image, name, and gender.

Libraries Used
Room Persistence Library

MPAndroidChart for data visualization

Glide for image loading

Firebase Authentication

Firebase Realtime Database
