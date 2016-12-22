CHANGELOG
=========

1.0.5
 - Added a lint option to not abort on errors, since now that I add several external .jar files (needed to extend Volley) lint fails because
it can't be sure if they're part of the Android system or not.

1.0.4
 - Reverted some changes:
	- removed all code allowing SDK to access parent's kids since this is not in the scope
 - Added user creation through the SDK, with relevant error messages (created, duplicate, invalid email, invalid password, network error)
 - SDK scope is at this point:
	- Create user (and validate through email)
	- Auth user (with login & logout and 24h store)
	- Get parent details
	- Update parent details

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
