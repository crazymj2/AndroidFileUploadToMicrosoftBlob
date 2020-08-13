Getting Started

Download:

Option 1: Source Zip

To download a copy of the source code, click "Download ZIP" on the right side of the page or click here. Unzip and navigate to the DemoBlobUpload folder.


Option 2: Source Via Git

To get the source code of the SDK via git just type:
```
git clone https://github.com/crazymj2/AndroidFileUploadToMicrosoftBlob.git

cd ./DemoBlobUpload
```

For A New Project:

To get the binaries of this library as distributed by Microsoft, ready for use within your project, you can use Gradle.

First, add mavenCentral to your repositories by adding the following to your gradle build file:
```
repositories {
    mavenCentral()
}
```
Then, add a dependency by adding the following to your gradle build file:

```
dependencies {
    compile 'com.microsoft.azure.android:azure-storage-android:2.0.0@aar'
}
```
To use Microsoft Azure storage services, you need to first create an account.(https://account.windowsazure.com/signup)


Working:
1. Open the Project in Android Studio.
2. Add the Connection String & The Container Name
3. Run the Application
4. Click on the Button to select a image to upload to blob.
5. The Image will be uploaded in the background
