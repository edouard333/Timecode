# Timecode
Librairie gérant le timecode.

# Comment l'utiliser ?
Instancier la classe :
```java
import com.phenix.timecode.Timecode;
```

Exemple :
```java
import com.phenix.timecode.Timecode;
import com.phenix.timecode.Framerate;

void main(String[] args) {
    // Un timecode à 25 i/s :
    Timecode tc = new Timecode("2:30:00:00", Framerate.FR25);

    // Définir le start TC pour une conversion :
    tc.setStartTimecode("01:00:00:00");

    // Convertir en TC 24.
    tc.changeFramerate(Framerate.F24);

    // Valider un timecode :
    Timecode.validation("00:00:00:00");
}
```