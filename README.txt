

USING THE GENERATED PROJECT:
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

The project is pretty standard Maven web app project and should thus
be compatible with practically all IDE's. 

Packaging/installing the project
--------------------------------

mvn vaadin:clean vaadin:compile
generates widgetsets

mvn install
packages the project as a WAR file

Running the app in development server
-------------------------------------

Just deploy the app on Wildfly 8 in eclipse.
