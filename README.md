# USING THE GENERATED PROJECT:

A mobile app to organize events to collect litter and report the results from the beautiful archipelago of Finland.

## Building the app

Simple `mvn package` command will build the war. To pass the TestBench tests, which are executed along with `install` target, install [ChromeDriver](https://code.google.com/p/selenium/wiki/ChromeDriver) to your default path.

To just launch it locally, you can use embedded widlfy: `mvn package wildfly:run`.

