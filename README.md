# Secure Bank Application using Android Studio (Kotlin)

Here is an application allowing a specific user to see his bank accounts.

This application has many requirements : 
- available offline
- a refresh button allows the user to update its accounts
- access to the application is restricted
- exchanges with API must be secure (TLS)

### How do you ensure that user is the right one starting the app ?

Like a real situation, a user receives a specific password from his bank to log on to the application. 
Here, a unique password only known by user is required.

### How do you securely save user's data on the phone ?

We have to manage the case where the device is not connected to internet. That's why I decided to create a database to store user's data. 
I create a Room database, which is a layer on top of an SQLite database. The stored data can't be read by a non-root user but is not encrypted. 
That's why I added an encryption layer. A secret key is used to encrypt the data which is stored in a C/C++ file (CMake).

### How did you hide the API URL ?

Like the database, I stored an encryted version of the URL in a C/C++ file.

### Screenshots of the application

![Alt text](https://github.com/Willipo99/SecureBankApp_TD3_Willipo99_IOS2/tree/main/app/src/screenshots/accueil.JPG)
![Alt text](https://github.com/Willipo99/SecureBankApp_TD3_Willipo99_IOS2/app/src/screenshots/displaydata.JPG)
