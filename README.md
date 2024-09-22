# LocaNotify

*Overview*  
LocaNotify is a location-based Android application that helps users stay informed when they are near specific locations of interest. The app uses background location tracking to monitor the user's movements and sends notifications when predefined locations are approached. Built using Kotlin, the app combines modern Android development techniques with efficient background processing to deliver timely and relevant location-based notifications.

## Key Features
- *User Authentication*: Implemented secure sign-in and sign-up functionalities using Firebase Authentication, ensuring streamlined and safe user access.
- *Notification Management*: Users can add and delete notifications they wish to receive when near certain locations. Room Database is utilized to efficiently store and retrieve notification data.
- *Background Location Tracking*: Continuously monitors the user's location in the background and triggers notifications as the user approaches a predefined location.
- *Offline Access*: Caches notification data for quick and efficient retrieval, even when the user is offline, using Room Database.

## Technologies Used
- *Kotlin*: For modern and concise development, leveraging object-oriented and functional paradigms.
- *Firebase Authentication*: Integrated for secure and easy-to-use authentication.
- *Room Database*: Used for local data storage, enabling the caching of notifications.
- *Kotlin Coroutines*: For asynchronous location tracking and efficient background tasks.
- *MVVM Architecture*: Ensures a clean separation of concerns.

## Screenshots

|  |  |
|----------------------------|-------------------------------|
| ![Login Screen](https://github.com/user-attachments/assets/68499082-c16b-4905-828b-f223e9240cad) | ![Notification Screen](https://github.com/user-attachments/assets/778bdf21-42f4-46d0-b631-f92241875629) |
