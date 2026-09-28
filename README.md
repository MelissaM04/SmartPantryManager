------- Smart Pantry Manager -------

--- Application Description ---
The Smart Pantry Manager is an Android application designed in order to help users reduce food waste. This is by tracking the ingredients they currently have at home. 
The core feature of the application is the strict recipe-matching engine which can suggest meals that can be cooked using strictly the ingredients available in the user's pantry. 
A recipe is only suggested if the user possesses all the required ingredients in at least the required quantity, entirely eliminating the need for mid-recipe grocery runs. 
It can also suggest recipes that is "almost there". Meaning recipes that require a certain ingredient. 

--- Database Choice & Justification ---
Database Chosen: PostgreSQL (Hosted via Supabase)

I chose to implement PostgreSQL via Supabase accessed through a REST API (Retrofit).
For the database schema, I use a NoSQL hybrid approach by storing the recipe ingredients as a `jsonb` array rather than using a traditional many-to-many relational linking table. 
This choice provides mobile performance by allowing the application to fetch complete recipes in a single network request. 
The relational matching between the user's pantry items and the recipe requirements is then given dynamically by a custom algorithm on the client side in Java. 
This removes the need for excessive backend load. it also ensures offline type of speed when switching screens, and demonstrates robust client-side data manipulation.

--- Core Features ---
Pantry Management: Full CRUD functionality to Add, Read, Update, and Delete ingredients.
Strict-Matching Engine: An algorithm that calculates exact inventory quantities against recipe requirements in order to suggest only that which can be cooked at that moment.
Dynamic Recipe Previews: A collection of pre-loaded recipes that parse and format database strings into readable UI elements.
Recipe Details: A clean, detailed view of preparation steps and exact ingredient measurements.

--- Setup and Run Instructions ---
--- Prerequisites ---
Android Studio (Koala or newer recommended)
Java Development Kit (JDK) 17 or higher
An Android Emulator or physical Android device running API 24 or higher.

--- Installation Steps ---
1. Clone the Repository:
   Open your terminal or command prompt and run:
   ```bash
   git clone [https://github.com/MelissaM04/SmartPantryManager.git](https://github.com/MelissaM04/SmartPantryManager.git)

--- Running the application Steps ---
1. Open in Android Studio:
Launch Android Studio.
Click Open and select the SmartPantryManager folder you just cloned.

2. Sync Gradle:
Allow Android Studio a few moments to build the project and download all necessary dependencies (such as Retrofit and RecyclerView libraries).
If prompted, click Sync Project with Gradle Files.

3. Run the Application:
Select your preferred emulator or plug in a physical Android device.
Click the green Run (Play) button in the top toolbar (or press Shift + F10).
The application will compile, install, and launch the Pantry List screen on your device.




