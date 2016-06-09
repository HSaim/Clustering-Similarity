# Clustering-Similarity
The repository contains the following projects:-


## SOM Project:
SOM algorithm is applied to cluster methods. Method tags are given to find the closeness among certain methods

## KMeans Project:
The same work is done here as done in SOM project but the algorithm is KMeans

### APIs and DB required for SOM and KMeans - if using WordNet

1. WordNet DB:

    https://wordnet.princeton.edu/wordnet/download/current-version/
2. WordNet Search for Java (WS4J) API:

      jawjaw-1.0.2.jar - https://code.google.com/p/jawjaw/downloads/list
      
      ws4j-1.0.1.jar - https://code.google.com/p/ws4j/downloads/list
3. Java WordNet API Search (JAWS)

      http://lyle.smu.edu/~tspell/jaws/

#### Placement of WordNet Libraries and DB in the project

1. WordNet DB:

   Place in the main folder of SOM/KMeans
2. Jar files:
	Place above mentioned jar files in 'lib' folder and  add them through the following way:
	Markup :	* Right click the project e.g SOM
				* Select 'Build Path' -> 'Configure Build Path' menu
				* Select 'Libraries' tab
				* Click 'Add Jars' button
				* Add the required jar files from the project's lib folder
				* Click 'ok' button

### SOM and KMeans startup files
	SOM: SOMMain.java
	KMeans: KMeansMain.java

### Comparison of KMeans and SOM:

Both algos give satisfactory results.

## WS4JSimilarityCalculation Project:
This project WS4J API to access WordNet to find similarity between two words.

It uses three ways to find the similarity between words.
Markup :	1. Calculate similarity between original words
			2. Calculate similarity between stemmed words. Stemming done through Porter Stemmer Algorithm
			3. Calculate similarity after converting morphological words into their base form with the help of JAWS API

 
### Prerequisites for WS4JSimilarityCalculation: 


1. WordNet DB:

    https://wordnet.princeton.edu/wordnet/download/current-version/
2. WordNet Search for Java (WS4J) API:

      jawjaw-1.0.2.jar - https://code.google.com/p/jawjaw/downloads/list
      
      ws4j-1.0.1.jar - https://code.google.com/p/ws4j/downloads/list
3. Java WordNet API Search (JAWS)

      http://lyle.smu.edu/~tspell/jaws/

### Instructions for WS4JSimilarityCalculation:

1. After downloading above APIs and database, place the jar files in 'Lib' folder and database in 'WordNet-2-1' folder, in the project’s folder.


2. If you are running the project in eclipse, In the build path, choose the option ‘Configure Build Path’ and add above mentioned JARs in the ‘Libraries’ tab.


3. 'SimilarityCalculation.java' is the starting point of execution of the project. Two words are hard coded in the main() function to find the similarity.

## JWIWordNetDemo Project: 

Displays word's ID, name, definition, and POS information

### Prerequisites for JWIWordNetDemo: 


1. WordNet DB:

    https://wordnet.princeton.edu/wordnet/download/current-version/
2. MIT Java Wordnet API:

      edu.mit.jwi_2.4.0.jar - http://projects.csail.mit.edu/jwi/
      
### Instructions for WS4JSimilarityCalculation:

1. After downloading above APIs and database, place the jar file in 'Lib' folder and database in 'WordNet-2-1' folder, in the project’s folder.


2. If you are running the project in eclipse, In the build path, choose the option ‘Configure Build Path’ and add above mentioned JARs in the ‘Libraries’ tab.










