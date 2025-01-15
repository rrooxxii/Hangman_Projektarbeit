Nach einigem herumspielen bin ich auf ein sequenzielles Design gestoßen. Angelehnt and den Ablauf in der Command Line Version wird nun ein View nach dem Anderen aufgerufen. 
Das Mach Codewiederverwendung einfacher. 
Key Points: JavaFx arbeitet mit dem Model View Controller Designpattern. die FXML files übernehmen das View (mit JavaFX Framework), ein Controller handlet alle Events und bindet die Logik and den View.
In diesem Fall wurde ein Singleton Pattern für den Controller verwendet um States zu speichern dann wieder aufgenommen werden können (Game-States) 
Singleton bedeutet in dem Fall dass JAVAFX nicht wie üblich eine neue Controller-Klasse instanziert beim Aufruf eines Views/Scene sondern immer auf die gleiche Instanz zurückgreift. 
Das Model sind Die Game Klasse und die Spieler die die jeweilig relevanten Daten beinhalten. Eine Instanz der Klasse wird für den Controller instanziert. Auch ein Vorteil für das Singleton, da Controller und Model damit fix miteiander verknüpft sind. 
