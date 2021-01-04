# HackerNews-Kotlin
An application that displays the front page stories from hn.algolia.com.
The application retrieves data using retrofit and transforms in to objects with Moshi. 
It uses the MVVM architecture pattern and persists data using a Room database.


## Features

This project displays a RecyclerView of urls that a user can open in to WebViewFragment. 
![HomeActivity image](https://github.com/chrisdholmes/HackerNews-Kotlin/blob/main/HomeActivity-image.png)

When the WebViewFragment is open the user can use the ActionBar to share or save the article.

![WebView image](https://github.com/chrisdholmes/HackerNews-Kotlin/blob/main/WebView-image.png)

![Sharesheet image](https://github.com/chrisdholmes/HackerNews-Kotlin/blob/main/ShareSheet-image.png)


The BookMarksFragment allows users to review and delete saved BookMarks. Bookmarks are
persisted locally in a Room DB.

![alt text](https://github.com/chrisdholmes/HackerNews-Kotlin/blob/main/Bookmarks-image.png)
