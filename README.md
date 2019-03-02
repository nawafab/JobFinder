# JobFinder

JobFinder Android application written in java that search for jobs from different providers such as GitHub and search.gov .

### Structure
The project main packages is : 
* `/retrofit` - contains retrofit classes , parsers , data source class.
* `/models` - the java model class related to the jobs and provider responses
* `/ui` - activities ,fragments and adapters
* `/commons` - utils , interfaces and app constants

 

### Libraries
The external libraries used in the app : 
* [Fresco](https://frescolib.org/) - For image downloading
* [Retrofit 2](https://square.github.io/retrofit/) - HTTP client for Android 
* [gson](https://github.com/google/gson) - json to java converter  
* [Google places](https://developers.google.com/places/web-service/intro) - used for Autocomplete location.

### Run the app
You need to add your own google places api key OR the one i provide via email
to `strings.xml` key `google_places_key`

### Adding new provider
For adding new provider to the app 
1. Add the api call in the retrofit `/retrofit/webservice.java` interface with callback `Call<ArrayList<JobModel>>` do not to enter full url path at the `Get()` annotation for retrofit.
2. When you create the class for new provider response handling make it extend `JobModel` and override the getter for it and math the needed parameters.
2. Modify `/retrofit/JobDeserializer.java` based on a condition in the new provider response and return Deserialization of you new added response class.
3. Add your provider as enum value in `/models/Providers.java`
4. Add your API Call in `/retrofit/DataSource.java` and add you call in the case condition based on provider in `getJobsByProvider()`method .

