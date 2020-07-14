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
1. Checkout into an existing branch with `git checkout` OR create and checkout into a new branch with `git checkout -b`.
2. Create commits within a branch.
3. If you've added code that should be tested, add tests.
4. If you've changed APIs, update the documentation.
5. Merge branches with `git merge`.
6. Update branches from remotes with `git fetch`.
7. Merge updated remote branches with `git merge`.
8. Update and merge remote branches with `git pull`.
