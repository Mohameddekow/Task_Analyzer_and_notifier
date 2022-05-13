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

### How it's built and

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

## Main Screen

   * Light
    
      shimmer loading effect | Home | Add task
      --- | --- | ---
      <img src="https://user-images.githubusercontent.com/61431856/167199440-5cc513e2-77e6-4a10-9bd6-5f2d08392093.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167199512-0c2e2090-bc38-4d04-ab2a-53792e0f42a0.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167199616-ba9bfc07-4d01-4199-8197-fe9c78236965.jpg" width="280"/>


   * Dark
    
      shimmer loading effect | Home | Add task
      --- | --- | ---
      <img src="https://user-images.githubusercontent.com/61431856/167199800-71eb150c-7fff-4de9-b7fb-fb479314d3e7.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167199838-d7bf2bf5-4621-4549-b927-a3bbd678c95e.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167199884-95444029-1eef-442f-9cea-68622e999427.jpg" width="280"/>
      




## Analysis and notification

   * Light
    
      analysis of tasks | notification
      --- | --- 
      <img src="https://user-images.githubusercontent.com/61431856/168285946-7c419926-d45d-49ab-b929-65029c7d31cc.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/168285964-ba737a2b-a1b1-42a5-ae4a-72b26eb5aee3.jpg" width="280"/> 
      



## Authentication

   * Light
    
      login | forgot password | register
      --- | --- | ---
      <img src="https://user-images.githubusercontent.com/61431856/167200796-1a95f264-0a86-4186-9a5b-33eaf58f1976.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167200841-f396d639-0a49-42da-b64b-a6ed52f8e4d7.jpg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167203489-49e03286-dffc-4f0d-a747-eb56a228dcfe.jpeg" width="280"/>



## Profile

   * Light
    
      profile | ediit profile 
      --- | --- 
      <img src="https://user-images.githubusercontent.com/61431856/167203986-e6ddaf51-064e-491a-8bde-4589158daf32.jpeg" width="280"/> | <img src="https://user-images.githubusercontent.com/61431856/167204032-d4bac542-3640-497b-b437-17b0030d6815.jpeg" width="280"/> 



<!-- Light | Dark | GIF
--- | --- | ---
<img src="tttight.png" width="280"/> | <img src="rrrher_dark.png" width="280"/> | <img src="httttts/weather.gif" width="280"/>

 -->
