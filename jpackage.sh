export PATH="/usr/local/opt/openjdk/bin:$PATH"

jpackage --input build/libs/ \
         --name vocabulair \
         --main-jar vocabulair-0.0.1-SNAPSHOT.jar \
         --type dmg \
         --main-class nl.sander.vocabulair.BootApplication
