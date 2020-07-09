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
