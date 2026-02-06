# Dictionary-based-optimisation

A small Spring Boot project demonstrating fast dictionary lookup and optimisation data structures (BK-tree, Ternary Search Tree, Cuckoo Filter, and frequency tracking) for approximate matching and efficient membership checks.

**Features**
- **BK-tree**: approximate string matching (fuzzy lookups).
- **Ternary Search Tree**: compact, fast prefix and exact searches.
- **Cuckoo Filter**: probabilistic set membership with low memory footprint.
- **Frequency Tracker**: tracks word usage frequencies to prioritise suggestions.

**Repository Layout**
- **Project root**: contains this `README.md` and the `DictionaryOptimiser/` Maven project.
- **DictionaryOptimiser**: main Spring Boot app; see [DictionaryOptimiser/pom.xml](DictionaryOptimiser/pom.xml).
- **Data file**: default dictionary CSV is at [DictionaryOptimiser/src/main/resources/data/dictionary.csv](DictionaryOptimiser/src/main/resources/data/dictionary.csv).

**Getting Started**
Prerequisites: Java 11+ and Maven (or use the included Maven wrappers).

Quick run (Windows):

```powershell
cd DictionaryOptimiser
.\mvnw.cmd spring-boot:run
```

Or build and run the jar:

```powershell
cd DictionaryOptimiser
.\mvnw.cmd clean package
java -jar target/*.jar
```

Run tests:

```powershell
cd DictionaryOptimiser
.\mvnw.cmd test
```

**Configuration & Data**
- Edit the dictionary source at [DictionaryOptimiser/src/main/resources/data/dictionary.csv](DictionaryOptimiser/src/main/resources/data/dictionary.csv) to add or change words.
- Application properties live in [DictionaryOptimiser/src/main/resources/application.properties](DictionaryOptimiser/src/main/resources/application.properties).

**Development notes**
- The main application entry is [DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/DictionaryOptimiserApplication.java](DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/DictionaryOptimiserApplication.java).
- Core algorithms and models are under [DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/model](DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/model).
- Service layer: [DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/service/DictionaryService.java](DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/service/DictionaryService.java).

**Usage**
- The app exposes a simple API via the controller at [DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/controller/DictionaryController.java](DictionaryOptimiser/src/main/java/com/example/dictionary/DictionaryOptimiser/controller/DictionaryController.java). Start the app and use the endpoints for lookups and suggestions.

