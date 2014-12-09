giggo

===============

1	Project Specification

Giggo is an Android Application. It was built as a college project by Ronan Frawley, a masters student in Computer Science at University College Dublin, Ireland. The basic premise of giggo is that it allows a user to locate music events in their area, on the fly. This is a demonstration version of the application and does not have the intended full range of functionality.

1.1	The Function
The intention is to develop an application that uses Google Maps to locate gigs in Dublin. This app would therefore have the following features: 
1.	A map fragment activity that focuses on Dublin. The user can zoom out, but there will only be Dublin gigs initially. The application should attempt to pull the users GPS information to allow for scalability beyond Dublin. 
2.	A small icon indicating the location of each gig. When tapped once, this icon should expand into a bigger icon with some basic information (i.e. name of the act, name of the venue and the date/time). 
3.	When the bigger icon is selected, a new activity will give the full details of the event. There should be user options here that will allow a connection to other APIs and phone applications – for example, if the user has the Facebook application – it will allow them to like the app or the venue. 

1.2	The Project
The focus of the project will be to get points 1 and 2 above working as a demonstration applocation. This is because the functionality specified for point 3 is broad and could end up as being a more comprehensive piece of work than points 1 & 2 combined.

Therefore, the goal will be to develop a fully operational mapping application that shows gigs in the Dublin area. Once this is working, the potential intricacies of the ‘ additional details activity’ can be explored and implemented. This will make sure the application is delivered with good functionality in the time allocated to the project completion.

===============

2	Google Maps Android API v2

The core functionaltiy of this application is the use of mapping geo-locations. The Google Maps Android API v2 (Google Developers, 2014) allows users to make thousands of requests per day for free and as such would suit my needs. However, there are alternative mapping APIs that could have been used for the mapping and location functionality. For example, the Open Street Map Android API provides the same functionality as the above stated Google Maps API.

In making a final decision here, I needed to factor in the knowledge that I had not used any APIs on the Android development platform previously. As such, the host of supporting documentation and tutorials on using the Google Maps Android API v2 made the difference in the end. It should be stated that if I had a greater depth of experience in this matter, I would have chosen the Open Street Map Android API instead (Openstreetmap.org, 2014).

To begin using the Google Maps API for Android, it was necesarry to register as a Google developer on the Google Developers website. Here, the range of Google APIs can be selected and applied to different web-based and mobile applications. The SHA1 key of the develorers machine, along with the root name of the mobile application, needs to be registered with Google to get the API key for Google Maps.

Once this API key has been obtained, it can combine with a number of other permissions in the Andoid Manifest XML file of the application. These permissions can combine to generate map fragments in the application’s activities.

Building upon this programatic infrastructure, two maps are used in the Giggo application. Both of these maps use map fragements in their respective layout files and separate Java classes. These activities are:
•	The Make Java class uses the activity_map xml file. When the application is initially launched and the user selects ‘find a gig’, this activity is brought up. This activity builds a map that populates the gigs on the remote database into a map fragment. These gigs appear as markers and are indicated by purple music notes. When the user clicks on these markers a new activity appears that allows the user to see the gig information
•	The Create Java class uses the activity_create xml file. When the application is initially launched and the user selects ‘make a gig’, this activity is brought up. This activity generates a map of Dublin and positions a blue marker in the city centre. This marker can be moved to the desired location of the gig by holding down and moving it around. When the user clicks the marker, a new activity is launched that takes the geo-location of the marker set by the user. In the Insert activity the user can insert event information and this is inserted to a remote database with the latitude and longitude values of the marker. 

===============

3	Remote Database

3.1	Current Database
In order to populate information in the ‘find a gig’ activity map, that dynamically took gig information entered by other users in the ‘make a gig’ activity, a remote database would be necessary. In this situation a simple MySQL database is used. Boilerplate PHP scripts were accessed by the application in order to insert and read gig information.

The Insert activity is called after the ‘Create’ mapping activity. In this activity the band name and event description, along with the latitude and longitude set by the user in the ‘Create’ activity map, is submitted to the remote database once the ‘make a gig’ button is clicked. A HTTP request is made to ‘http://ronanfrawley.com/test.php’. The information is then passed to the PHP script and inserted into the database.

The Make activity is called after the user selects to ‘find a gig’. Another HTTP request is made to ‘http://ronanfrawley.com/test.php’. The PHP then lists out each gig on the database. The webpage is then turned into a String on the application. That String looks for gigs using pattern matching. As such, each gig is stored on multidimensional arrays and turned into markers on the map.

3.2	Future Work
This database is only a working demonstration and requires additional functionality to support the application. For this reason, the bulk of the immediate future work will be on the interactions between the database and the application. These imporvements will include:
•	CRUD – As it stands, users can only ‘create’ and ‘read’ from the database. This is fine for demonstration purposes but is not sufficient for any form of distribution. Users should be able to ‘update’ and ‘delete’ the gigs that they created. Also, gigs should only show on the day of the event (or within a user specified time). This requires additional functionality to be added to the schema  
•	Accounts – As a result of adding ‘update’ and ‘delete’ functionality to the application, it will be necessary to make sure that gigs are linked to a specific account. Using Android Facebook Oauth (Facebook Developers, 2014), to log users in could be used, along with a native login service.
•	Encryption – The information needs ‘two-way’ encryption, to secure the login information and gig history. This is important from a consumer confidence perspective.

===============

4	Resources

4.1	Skills Gained
•	Android Studio (Android Studio, 2014) – The IDE used to develop Giggo was Google’s Android Studio. It seemed that, as a result of using Google libraries for mapping, that Android Studio would be convenient.
•	XML – I had not used the XML language before this module and this project crystalized its value to me. 
•	Activity Lifecycles – I found that activity lifecycles could be messy on an application if you are not being careful. In many cases this caused crashes when poorly executed.

4.2	Future Work
•	Database and Accounts – Stated in Section 3.2, there will be a number of works on the remote database and the application functionality.
•	Facebook Events (Facebook Developers, 2014) – Along with Facebook Oauth login, public event information should be populated into the application. Other vendors should be approached to seek premission to use their APIs. Ticketmaster have a private API that could be used to monitize the application.
•	SAP HANA (SAP, 2014) – Once there a large scale of data populated to the maps, the performance of the application could be slowed. It would be worth experimenting with moving the remote database on to SAP HANA to allow for greater speeds.

