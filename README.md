# Smart Pantry Manager

Smart Pantry Manager is a Java based Android software that allows users to keep track of their digital pantry and explore recipes that may be made with the goods they have on hand.

## Main Features

- Add pantry ingredients
- View ingredients in a RecyclerView
- Edit existing ingredients
- Delete individual ingredients
- Clear the entire pantry safely
- Record quantities, measurement units and expiry dates
- Highlight expired ingredients
- Store pantry data locally using SQLite
- Preserve pantry information when the app is closed
- Display recipes that match the available pantry ingredients
- Perform strict ingredient, quantity and unit matching
- Convert compatible units such as kilograms to grams and litres to millilitres
- Exclude expired ingredients from recipe matching
- Display recipe details, required ingredients and cooking instructions
- Navigate between Pantry, Recipes and Settings

## Application Screens

1. Pantry List
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Details
5. Settings

## Technologies Used

- Java
- Android Studio
- XML layouts
- SQLite
- RecyclerView
- Material Design components
- Git and GitHub

## Recipe Matching

The app has 15 built-in recipes, but they are only suggested when:

- Every required ingredient is present
- Each ingredient has a sufficient quantity
- Measurement units are compatible
- The pantry ingredients have not expired

The matching process supports typical singular and plural ingredient names and unit conversions:

- Kilograms and grams
- Litres and millilitres
- Item and items
- Slice and slices

## Local Database

SQLite does the following pantry operations:

- Create ingredients
- Read saved ingredients
- Update ingredients
- Delete ingredients
- Delete all pantry ingredients

## Running the Project

1. Clone or download the repository.
2. Open the project in Android Studio.
3. Allow Gradle to synchronise.
4. Start an Android emulator or connect an Android device.
5. Select the `app` run configuration.
6. Click Run.

## Demonstration Data

Example pantry data for the Rice and Beans recipe:

- Rice — 2 kg
- Beans — 200 g
- Salt — 10 g

## Version Control

The project was built progressively with Git and GitHub. Significant pushes were made for the major stages of development like the interface, navigation, SQLite CRUD capability, RecyclerView adapters, recipe matching, expiry handling and final code quality enhancements.