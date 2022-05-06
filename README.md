# Task Analyzer and notifier

### #The Project's objective was developed to help people around the world easily analyze, keep and remind their tasks with notifications
* The app can:
    * catch the task
    * sync your data(task) with any device
    * analyze your task in graphs and pie chart
    * remider notifications
    * Remind you about your todo task some 15 minutes(remind time depends on your preference) before the actual time
    * keep the history of your task


Feel free to use however you think best in the app.
- Use any libraries for Android that you want, but we will need to be able to build your code to evaluate it.

### Prerequisites - Unit Tests

### How it's built

* Technologies used
    * [Kotlin](https://kotlinlang.org/)
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    * [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
    * [DaggerHilt](https://dagger.dev/hilt/)
    * [Jetpack](https://developer.android.com/jetpack)
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

* Architecture
    * MVVM - Model View View Model

### Database
* Room
    * [to store data](https://developer.android.com/training/data-storage/room) 
    * and cach data

* Firebase
  * Auth
      * [Firebase Authentication](https://firebase.google.com/docs/auth/android/start)  
  * Images Storege  
      *  [Firebase Storage](https://firebase.google.com/docs/storage/android/start)   
  * Data Storage
      * [Fiarebase Firestore](https://firebase.google.com/docs/firestore/quickstart)   


* Tests
    * [JUnit5](https://junit.org/junit5/)

   
## Others
  * Coroutines
  * liveData
  * jetpack navigation components
  * [picasso image loading lib](https://github.com/square/picasso)
  * [hdodenhof CircleImageView](https://github.com/hdodenhof/CircleImageView)

### Screenshots

I added some screenshots in the `screenshots` folder, in the root directory of the project. Added some GIFs to also show end to end test on the app

Light | Dark | GIF
--- | --- | ---
<img src="tttight.png" width="280"/> | <img src="rrrher_dark.png" width="280"/> | <img src="httttts/weather.gif" width="280"/>

