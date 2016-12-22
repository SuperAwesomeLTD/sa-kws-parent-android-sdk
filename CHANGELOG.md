CHANGELOG
=========

1.0.3
 - Added methods to get a parent's children:
	- all the children
	- only pending children
	- active children
	- inactive children

1.0.2
 - Added methods to get user data & to update user data
 - For the update user, since it's a "PATCH" type request, I had to add several dependencies:
	- Volley 1.0.0 through Gradle
	- okhttp, okio and okhttp-urlconnection as .jar libs
   - In the "PATCH" request type, the url connection is at this moment handled by a custom Volley implementation, but that should change in the future

1.0.1
 - Added Login & Logout methods to the Parent SDK. Now, based on a user created through the Parent Portal flow, a publisher can use the mobile SDK to auth parents.

1.0.0
 - First version of the Kids Web Services Parents SDK
