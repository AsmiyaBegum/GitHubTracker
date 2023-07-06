# GitHubTracker

**Track Github Repositories**

**Purpose :**
This application allows users to conveniently track their favourite GitHub repositories. Users can add repositories of their choice and easily view the list within the app. Additionally, they can effortlessly share the repository list with other applications such as WhatsApp and Gmail. 

**Goals :**
● Allow users to add new GitHub repositories by providing the owner's name and repository URL. 

● Display a list of added repositories on the home screen, including their descriptions. 

● Provide an option to share the repository list with other applications.

● Enable users to click on a repository to open it directly in the browser. 

**Proposed Flow :**

The application will have 2 screen landing screen and github repo adding screen.

**Landing Screen** : 

● When no repositories are added, the landing screen will display a label saying "Track your favourite repositories" along with an "Add Now" button. 
● Added repositories will be listed on the screen, showing the repository name, a short description, and an option to share the repository through apps like WhatsApp and Gmail. 
● Users can click on a repository to open it directly in the browser. 
● To add new repositories, there will be a "+" button in the action bar for easy access. 
● The repository details will be stored locally, allowing users to access and view the list of repositories the next time they use the app. 
● Clicking the "Add Repo" button will open the screen for adding GitHub repositories. 
**Github Repo Adding Screen :** 
● The screen should provide two input fields to enter the owner name and repository name. 
● Upon entering the repository name, the application should fetch the repository details using the GitHub API.
● The fetched repository should be added to the landing screen, including the repository name, description, and any other relevant information. 
● The repository details should be stored in the local storage, ensuring that it can be accessed and viewed on the landing screen the next time the user opens the application.
