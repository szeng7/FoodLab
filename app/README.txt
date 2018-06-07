User Interfaces and Mobile Applications
Team 7: Simon Zeng (szeng7), Cindy Yang (xyang73), Rebecca Nicacio (rnicaci1)

Differences from Design Document:
We chose not to implement a search function and hamburger menu in the action bar as shown in the recipe pages of our design. Instead, the user can navigate up to the search list using the devices back button or the action bar back button, or ther can navigate directly to the home screen by clicking the logo in the app bar. We will have only one scrollable list in our search(main) screen, as populating each list requires calls to the API each time. We chose not to implement sharing with user accounts or social media accounts, as it would have taken too long to integrate on top of our other functionality. We changed which options we have to sort our searches, specifically we changed “sort by calories” to “sort by rating”, as Spoonacular doesn’t provide calorie info for their recipes. This also means that the information provided in the recipe listview is now slightly differently in terms of format and what information it contains (ratings instead of calories). A minor change is that when entering a tip the user if given a pop up dialog instead of just entering in a text field using the keyboard, do to the amount of space left in the screen. “Culinary Preferences” in Settings for now is a choice of 3 (Italian, Chinese, and Mexican) versus the full page of cuisine types - we hope to implement this more in the future as well. 

Testing Notes:
In order for all test cases to work, the user needs to first log out of the app.

Usernames/Passwords:

Name: CINDY
User: cindy@jhu.edu
Password: 123321124312

Name: Judy
User: whatismy@jhu.com
Password: 123321

