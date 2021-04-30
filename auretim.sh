cd "$(dirname "$0")"
java --module-path ./jfx/lib/ --add-modules javafx.base,javafx.graphics,javafx.fxml,javafx.controls,javafx.media,javafx.web,javafx.swing -Dcom.sun.javafx.isEmbedded=true -Dcom.sun.javafx.touch=true -Dcom.sun.javafx.virtualKeyboard=javafx --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-exports javafx.controls/com.sun.javafx.scene.control.skin=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED -jar target/AuReTim-1.3-SNAPSHOT.jar

