Steps
============================

Generate maven project https://cloud.google.com/appengine/docs/standard/java/using-maven appengine-standard-archetype (select Java, Standard Environment)

Add example from: https://github.com/gsuitedevs/hangouts-chat-samples/tree/master/java/basic-async-bot

* Remember to add your own GCP service account key --> Make a folder under src/main called "resources" then add your key to the folder as a file called "service-acct.json"

## Maven
### Running locally

    mvn clean package appengine:run
    
    // If you get something similar to web.xml not found in target/../.., restart the VM :(

### Deploying

    mvn clean package appengine:deploy -Dapp.deploy.projectId=YOUR_PROJECT_ID

## Contributing
1. Use `git pull` to make sure your local repo is up to date with the remote repo.
2. Checkout into an existing branch with `git checkout` OR create and checkout into a new branch with `git checkout -b`.
3. Create commits within a branch.
4. If you've added code that should be tested, add tests.
5. If you've changed APIs, update the documentation.
6. Merge branches with `git merge`.
7. Update branches from remotes with `git fetch`.
8. Merge updated remote branches with `git merge`.
9. Update and merge remote branches with `git pull`.