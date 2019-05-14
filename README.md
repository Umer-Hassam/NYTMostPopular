# NYTMostPopular

As per the reqirement NYTMostPopular displays the most popular articles from the New York Times API in a traditionl master-detail view.

###### Best practices used in the app
- OOP approach to coding.
- MVVM Architecture for handling data.
- LiveData to maintain coherence with app/activity lifcycle
- Observer pattern to handle data changes to avoid creating strong reference/dependencies.
- All the data is loaded Asynchronously to ensure a smooth user experience
- Images are loaded Asynchronously saperately to ensure further performance improvement
- use of Material Design for UI


###### How to RUN the app
1. Install and configure the Android Studio (This app was built using AS3.4 however any recent version will do the trick)
2. Make sure both gradle files are synced
3. Make sure the *"app"* module is selected next to the run button
4. Connect an Android device that has both Developer Options and USB Debugging enabled.
5. Run the app using the run button.

###### How to TEST the app
1. open up the *"ExampleUnitTest"* file inside the *com.umerhassam.android.nytmostpopular (test)* package
2. due to lack of time I could only write one unit test so I decided to write one for the most important method in the app *"articlesFromJSONString"*. hopefully this will be enough to show my understanding of Tests.
3. Next to the method name there should be a greeb play button... press that button to test the method or press the green button next to the class name to test the whole class
   - If the green button isn't there (which sometimes happens when AS is performing background tasks) the test can be run by right-clicking on the file and selecting "Run 'ExampleUnitTest' "
4. **Coverage Report** Test can be run with *coverage* to read the coverage report as reqired
   - The test report will appear in a saperate pane on the left side of the code editor.
