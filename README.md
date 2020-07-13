Steps
============================

Generate maven project https://cloud.google.com/appengine/docs/standard/java/using-maven appengine-standard-archetype (select Java, Standard Environment)

Add example from: https://github.com/gsuitedevs/hangouts-chat-samples/tree/master/java/basic-async-bot

* Remember to add your own GCP service account key --> Make a folder under src/main called "resources" then add your key to the folder as a file called "service-acct.json"

## Maven
### Running locally

    mvn appengine:run

### Deploying

    mvn clean package appengine:deploy -Dapp.deploy.projectId=YOUR_PROJECT_ID

## Contributing
1. Make a new branch for your repository with `git branch`.
2. Checkout a branch with `git checkout`.
3. Create and checkout a new branch with `git checkout -b`.
4. Create commits within a branch.
5. If you've added code that should be tested, add tests.
6. If you've changed APIs, update the documentation.
7. Merge branches with `git merge`.
8. Update branches from remotes with `git fetch`.
9. Merge updated remote branches with `git merge`.
10. Update and merge remote branches with `git pull`.
